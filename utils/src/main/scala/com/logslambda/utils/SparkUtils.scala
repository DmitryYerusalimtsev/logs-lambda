package com.logslambda.utils

import java.lang.management.ManagementFactory

import org.apache.spark.sql.SQLContext
import org.apache.spark.streaming.{Duration, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {

  val isIDE = ManagementFactory.getRuntimeMXBean.getInputArguments.toString.contains("IntelliJ IDEA")

  private val checkpointDir = if (isIDE) "file:///c:/temp" else "hdfs://localhost:9000/spark/checkpoint"

  private def getSparkConfiguration(appName: String): SparkConf = {
    val conf = new SparkConf()
      .setAppName(appName)
      .set("spark.cassandra.connection.host", "localhost")

    if (isIDE) {
      System.setProperty("hadoop.home.dir", "C:\\Libraries\\WinUtils")
      conf.setMaster("local[*]")
    }

    conf
  }

  def getSparkContext(appName: String) = {
    val sc = SparkContext.getOrCreate(getSparkConfiguration(appName))
    sc.setCheckpointDir(checkpointDir)
    sc
  }

  def getSQLContext(sc: SparkContext) = {
    val sqlContext = SQLContext.getOrCreate(sc)
    sqlContext
  }

  def getStreamingContext(streamingApp: (SparkContext, Duration) => StreamingContext,
                          sc: SparkContext, batchDuration: Duration) = {

    val creatingFunc = () => streamingApp(sc, batchDuration)
    val ssc = sc.getCheckpointDir match {
      case Some(dir) => StreamingContext.getActiveOrCreate(dir, creatingFunc, sc.hadoopConfiguration, createOnError = true)
      case None => StreamingContext.getActiveOrCreate(creatingFunc)
    }
    sc.getCheckpointDir.foreach(cp => ssc.checkpoint(cp))
    ssc
  }
}
