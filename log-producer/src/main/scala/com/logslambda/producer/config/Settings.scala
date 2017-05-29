package com.logslambda.producer.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()
  private val logGen = config.getConfig("stream")

  lazy val records = logGen.getInt("records")
  lazy val timeMultiplier = logGen.getInt("time_multiplier")
  lazy val pages = logGen.getInt("pages")
  lazy val visitors = logGen.getInt("visitors")
  lazy val filePath = logGen.getString("file_path")
  lazy val productsFile = logGen.getString("products_file")
  lazy val referrersFile = logGen.getString("referrers_file")
}
