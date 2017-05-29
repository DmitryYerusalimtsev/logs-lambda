package com.logslambda.batch.jobs

import com.logslambda.batch.config.Settings
import com.logslambda.batch.providers.RddProvider
import com.logslambda.utils.SparkUtils
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.functions._

object BatchJob {

  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.getSparkContext()
    implicit val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._

    val input = sc.textFile(Settings.filePath)
    val inputDF = RddProvider.getActivityRDD(input).toDF()

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
      """.stripMargin)

    visitorsByProduct.foreach(println)
    activityByProduct.foreach(println)
  }
}
