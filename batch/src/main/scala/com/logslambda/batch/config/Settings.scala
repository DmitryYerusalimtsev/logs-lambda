package com.logslambda.batch.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()
  private val logGen = config.getConfig("batch")

  lazy val filePath = logGen.getString("file_path")
  lazy val hdfsPath = logGen.getString("hdfs_path")
}
