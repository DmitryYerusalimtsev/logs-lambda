package com.logslambda.speed.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()
  private val logGen = config.getConfig("speed")

  lazy val batchDuration = logGen.getInt("batch_duration")
  lazy val inputPathIDE = logGen.getString("ide_input_path")
  lazy val inputPath = logGen.getString("input_path")
}
