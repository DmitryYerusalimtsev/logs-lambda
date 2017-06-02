package com.logslambda.speed.jobs

import com.logslambda.speed.config.Settings
import com.logslambda.utils.SparkUtils._
import org.apache.spark.streaming.{Seconds, StreamingContext}

object StreamingJob {
  def main(args: Array[String]): Unit = {
    val sc = getSparkContext("Logs lambda speed")

    implicit val sqlContext = getSQLContext(sc)

    val batchDuration = Seconds(Settings.Speed.batchDuration)
    implicit val ssc = new StreamingContext(sc, batchDuration)

    val activityJob = new ActivityJob()
    activityJob.start()

    ssc.start()
    ssc.awaitTermination()
  }
}
