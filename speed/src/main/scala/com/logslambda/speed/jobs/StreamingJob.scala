package com.logslambda.speed.jobs

import com.logslambda.speed.config.Settings
import com.logslambda.utils.SparkUtils._
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object StreamingJob {
  def main(args: Array[String]): Unit = {
    val sc = getSparkContext("Logs lambda speed")

    implicit val sqlContext = getSQLContext(sc)

    val batchDuration = Seconds(Settings.Speed.batchDuration)
    implicit val ssc = getStreamingContext(streamingApp, sc, batchDuration)

    val activityJob = new ActivityJob()

    ssc.start()
    ssc.awaitTermination()
  }

  def streamingApp(sc: SparkContext, batchDuration: Duration) = {
    val ssc = new StreamingContext(sc, batchDuration)

    val textDStream = ssc.textFileStream(inputPath)
    textDStream.print()

    ssc
  }
}
