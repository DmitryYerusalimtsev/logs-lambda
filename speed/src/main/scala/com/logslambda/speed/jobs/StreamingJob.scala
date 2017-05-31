package com.logslambda.speed.jobs

import com.logslambda.speed.config.Settings
import com.logslambda.utils.SparkUtils._
import org.apache.spark.SparkContext
import org.apache.spark.streaming.{Duration, Seconds, StreamingContext}

object StreamingJob {
  def main(args: Array[String]): Unit = {
    val sc = getSparkContext("Logs lambda speed")

    implicit val sqlContext = getSQLContext(sc)

    val batchDuration = Seconds(Settings.batchDuration)

    val ssc = getStreamingContext(streamingApp, sc,batchDuration)
    ssc.start()
    ssc.awaitTermination()
  }

  def streamingApp(sc: SparkContext, batchDuration: Duration) = {
    val ssc = new StreamingContext(sc, batchDuration)

    val inputPath = isIDE match {
      case true => Settings.inputPathIDE
      case false => Settings.inputPath
    }

    val textDStream = ssc.textFileStream(inputPath)
    textDStream.print()

    ssc
  }
}
