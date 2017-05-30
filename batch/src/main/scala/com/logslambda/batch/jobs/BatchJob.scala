package com.logslambda.batch.jobs

import com.logslambda.batch.config.Settings
import com.logslambda.batch.providers.RddProvider
import com.logslambda.utils.SparkUtils
import org.apache.spark.sql.SQLContext

object BatchJob {

  def main(args: Array[String]): Unit = {
    val sc = SparkUtils.getSparkContext()
    implicit val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._

    val input = sc.textFile(Settings.filePath)
    val inputDF = RddProvider.getActivityRDD(input).toDF()

    val activityJob = new ActivityJob(inputDF, Settings.hdfsPath)
    activityJob.start();
  }
}
