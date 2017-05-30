package com.logslambda.batch.jobs

import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.functions.{add_months, from_unixtime}

class ActivityJob(inputDF: DataFrame, pathToSave: String)
                 (implicit sqlContext: SQLContext) extends HdfsParquet {

  val df = inputDF.select(
    add_months(from_unixtime(inputDF("timestamp_hour") / 1000), 1).as("timestamp_hour"),
    inputDF("referrer"),
    inputDF("action"),
    inputDF("prevPage"),
    inputDF("page"),
    inputDF("visitor"),
    inputDF("product")
  ).cache()

  df.registerTempTable("activity")

  override def partitionColumn: String = "timestamp_hour"

  def start(): Unit = {
    val visitorsByProduct = sqlContext.sql(
      """SELECT product, timestamp_hour, COUNT(DISTINCT visitor) as unique_visitors
        | FROM activity
        | GROUP BY product, timestamp_hour
      """.stripMargin)

    val activityByProduct = sqlContext.sql(
      """SELECT product, timestamp_hour,
        |sum(case when action = 'purchase' then 1 else 0 end) as purchase_count,
        |sum(case when action = 'add_to_cart' then 1 else 0 end) as add_to_cart_count,
        |sum(case when action = 'page_view' then 1 else 0 end) as page_view_count
        |FROM activity
        |GROUP BY product, timestamp_hour
      """.stripMargin).cache()

    // Save results to HDFS.
    save(activityByProduct, pathToSave);

    visitorsByProduct.foreach(println)
    activityByProduct.foreach(println)
  }
}
