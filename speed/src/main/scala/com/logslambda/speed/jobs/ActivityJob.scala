package com.logslambda.speed.jobs

import com.logslambda.core.domain.{ActivityByProduct, VisitorsByProduct}
import com.logslambda.core.providers.RddProvider
import com.logslambda.core.functions._
import com.logslambda.speed.config.Settings
import com.logslambda.speed.kafka.KafkaConsumer
import com.logslambda.speed.storage.{CassandraWriter, HdfsWriter}
import com.twitter.algebird.HyperLogLogMonoid
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Minutes, Seconds, StateSpec, StreamingContext}

class ActivityJob(implicit val ssc: StreamingContext, val sqlContext: SQLContext)
  extends KafkaConsumer with HdfsWriter with CassandraWriter[ActivityByProduct] {

  import sqlContext.implicits._

  val activityStream = kafkaStream
    .transform(input => RddProvider.getActivityRDD(input))
    .cache()

  writeToHdfs()

  def start(): Unit = {

    val activityStateSpec = StateSpec
      .function(mapActivityStateFunc)
      .timeout(Minutes(120))

    val statefulActivityByProduct = activityStream.transform(rdd => {
      val df = rdd.toDF()
      df.registerTempTable("activity")

      val activityByProduct = sqlContext.sql(
        """SELECT product, timestamp_hour,
          |sum(case when action = 'purchase' then 1 else 0 end) as purchase_count,
          |sum(case when action = 'add_to_cart' then 1 else 0 end) as add_to_cart_count,
          |sum(case when action = 'page_view' then 1 else 0 end) as page_view_count
          |FROM activity
          |GROUP BY product, timestamp_hour
        """.stripMargin)

      activityByProduct.map {
        r => {
          val key = (r.getString(0), r.getLong(1))
          val value = ActivityByProduct(r.getString(0), r.getLong(1), r.getLong(2), r.getLong(3), r.getLong(4))
          (key, value)
        }
      }
    }).mapWithState(activityStateSpec)

    val activityStateSnapshot = statefulActivityByProduct.stateSnapshots()
    val actByProduct = activityStateSnapshot.reduceByKeyAndWindow((_, b) => b, (x, _) => x, Seconds(30 / 4 * 4))
      .map(sr => ActivityByProduct(sr._1._1, sr._1._2, sr._2._1, sr._2._2, sr._2._3))
    saveToCassandra(actByProduct, Settings.Cassandra.activityByLogTable)

    // Unique visitors by product
    val visitorStateSpec = StateSpec
      .function(mapVisitorsStateSpec)
      .timeout(Minutes(120))

    val hll = new HyperLogLogMonoid(12)
    val statefulVisitorsByProduct = activityStream.map(a => {
      ((a.product, a.timestamp_hour), hll(a.visitor.getBytes))
    }).mapWithState(visitorStateSpec)

    val visitorStateSnapshot = statefulVisitorsByProduct.stateSnapshots()
    visitorStateSnapshot
      .reduceByKeyAndWindow((_, b) => b, (x, _) => x, Seconds(30 / 4 * 4))
      .foreachRDD(rdd => rdd.map(sr =>
        VisitorsByProduct(sr._1._1, sr._1._2, sr._2.approximateSize.estimate))
        .toDF().registerTempTable("visitors_by_product"))
  }
}
