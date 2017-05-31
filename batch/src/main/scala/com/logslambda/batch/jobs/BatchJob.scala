package com.logslambda.batch.jobs

import com.logslambda.batch.config.Settings
import com.logslambda.core.providers.RddProvider
import com.logslambda.utils.SparkUtils._

object BatchJob {

  def main(args: Array[String]): Unit = {
    val sc = getSparkContext("Logs lambda batch")
    implicit val sqlContext = getSQLContext(sc)

    import sqlContext.implicits._

    val input = sc.textFile(Settings.filePath)
    val inputDF = RddProvider.getActivityRDD(input).toDF()

    val activityJob = new ActivityJob(inputDF, Settings.hdfsPath)
    activityJob.start()
  }
}
