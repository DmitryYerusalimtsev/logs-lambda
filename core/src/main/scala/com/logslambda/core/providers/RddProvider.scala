package com.logslambda.core.providers

import com.logslambda.core.domain.Activity
import org.apache.spark.rdd.RDD

object RddProvider {

  private val numberOfFields = 7

  def getActivityRDD(input: RDD[String]): RDD[Activity] = {
    val activityRDD = input.flatMap { line =>
      val record = line.split("\\t")
      val MS_IN_HOUR = 1000 * 60 * 60

      if (record.length == numberOfFields) {
        val timestamp = record(0).toLong / MS_IN_HOUR * MS_IN_HOUR
        val activity = Activity(timestamp, record(1), record(2), record(3), record(4), record(5), record(6))
        Some(activity)
      }
      else
        None
    }
    activityRDD
  }
}
