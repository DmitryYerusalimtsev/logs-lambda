package com.logslambda.core.providers

import com.logslambda.core.domain.Activity
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka.HasOffsetRanges

object RddProvider {

  private val numberOfFields = 7

  def getActivityRDD(input: RDD[(String, String)]): RDD[Activity] = {

    val offsetRanges = input.asInstanceOf[HasOffsetRanges].offsetRanges
    input.mapPartitionsWithIndex({ (index, it) =>
      val or = offsetRanges(index)
      it.flatMap { kv =>
        val line = kv._2
        val record = line.split("\\t")
        val MS_IN_HOUR = 1000 * 60 * 60

        if (record.length == numberOfFields) {
          val timestamp = record(0).toLong / MS_IN_HOUR * MS_IN_HOUR
          val activity = Activity(timestamp, record(1), record(2), record(3), record(4), record(5), record(6),
            Map(
              "topic" -> or.topic,
              "kafkaPartition" -> or.partition.toString,
              "fromOffset" -> or.fromOffset.toString,
              "untilOffset" -> or.untilOffset.toString
            ))
          Some(activity)
        }
        else
          None
      }
    })
  }
}
