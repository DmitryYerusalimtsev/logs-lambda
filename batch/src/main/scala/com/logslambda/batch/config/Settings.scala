package com.logslambda.batch.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()
  private val batch = config.getConfig("batch")

  lazy val hdfsPath = batch.getString("hdfs_path")
  lazy val hdfsLogsPath = batch.getString("hdfs_logs_path")
}
