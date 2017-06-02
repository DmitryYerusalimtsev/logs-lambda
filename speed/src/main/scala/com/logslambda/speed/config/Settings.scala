package com.logslambda.speed.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()

  object Speed {
    private val logGen = config.getConfig("speed")

    lazy val batchDuration = logGen.getInt("batch_duration")
  }

  object Kafka {
    private val kafka = config.getConfig("kafka")

    lazy val topic = kafka.getString("topic")
    lazy val zookeper = kafka.getString("zookeeper")
    lazy val groupId = kafka.getString("group_id")
    lazy val offsetReset = kafka.getString("offset_reset")
  }

}
