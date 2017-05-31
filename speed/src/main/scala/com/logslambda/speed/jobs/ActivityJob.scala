package com.logslambda.speed.jobs

import com.logslambda.core.domain.ActivityByProduct
import com.logslambda.core.providers.RddProvider
import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.dstream.DStream

class ActivityJob(textDStream: DStream[String])(implicit sqlContext: SQLContext) {

  val activityStream = textDStream.transform(input => RddProvider.getActivityRDD(input))

  import sqlContext.implicits._

  def start(): Unit = {
    activityStream.transform(rdd => {
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
    }).print()
  }
}
