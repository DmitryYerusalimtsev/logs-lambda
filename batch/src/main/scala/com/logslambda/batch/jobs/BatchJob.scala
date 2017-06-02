package com.logslambda.batch.jobs

import com.logslambda.batch.config.Settings
import com.logslambda.core.providers.RddProvider
import com.logslambda.utils.SparkUtils._

object BatchJob {

  def main(args: Array[String]): Unit = {
    val sc = getSparkContext("Logs lambda batch")
    implicit val sqlContext = getSQLContext(sc)

    // Job runs every 6 hours.
    val inputDF = sqlContext.read.parquet(Settings.hdfsLogsPath)
      .where("unix_timestamp() - timestamp_hour / 1000 <= 60 * 60 * 6")

    val activityJob = new ActivityJob(inputDF, Settings.hdfsPath)
    activityJob.start()
  }
}
