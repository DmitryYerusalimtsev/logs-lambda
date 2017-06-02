package com.logslambda.speed.storage

import com.logslambda.core.domain.Activity
import com.logslambda.speed.config.Settings
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.streaming.dstream.DStream

/*
  Simple saving to HDFS. Should be moved to separate application.
  Or Kafka should be configured to save data directly to HDFS.
 */
trait HdfsWriter {

  val activityStream: DStream[Activity]

  val sqlContext: SQLContext

  def writeToHdfs(): Unit = {

    import sqlContext.implicits._

    activityStream.foreachRDD { rdd =>
      val activityDF = rdd.toDF
        .selectExpr("timestamp_hour", "referrer", "action", "prevPage", "page",
          "visitor", "product", "inputProps.topic as topic", "inputProps.kafkaPartition as kafkaPartition",
          "inputProps.fromOffset as fromOffset", "inputProps.untilOffset as untilOffset")
      activityDF
        .write
        .partitionBy("topic", "kafkaPartition", "timestamp_hour")
        .mode(SaveMode.Append)
        .parquet(Settings.Speed.hdfsPath)
    }
  }
}