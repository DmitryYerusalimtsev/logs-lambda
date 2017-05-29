package com.logslambda.utils

import java.lang.management.ManagementFactory

import org.apache.spark.{SparkConf, SparkContext}

object SparkUtils {
  private def getSparkConfiguration(): SparkConf = {
    val conf = new SparkConf()
      .setAppName("Lambda Logs")

    if (ManagementFactory.getRuntimeMXBean.getInputArguments.toString.contains("IntelliJ IDEA")) {
      System.setProperty("hadoop.home.dir", "C:\\Libraries\\WinUtils")
      conf.setMaster("local[*]")
    }

    conf
  }

  def getSparkContext() = new SparkContext(getSparkConfiguration())
}
