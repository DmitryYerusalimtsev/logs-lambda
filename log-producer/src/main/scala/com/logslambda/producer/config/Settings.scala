package com.logslambda.producer.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()

  object Stream {
    private val logGen = config.getConfig("stream")

    lazy val records = logGen.getInt("records")
    lazy val timeMultiplier = logGen.getInt("time_multiplier")
    lazy val pages = logGen.getInt("pages")
    lazy val visitors = logGen.getInt("visitors")
    lazy val productsFile = logGen.getString("products_file")
    lazy val referrersFile = logGen.getString("referrers_file")
  }

  object Kafka {
    private val kafka = config.getConfig("kafka")

    lazy val topic = kafka.getString("topic")
    lazy val server = kafka.getString("server")
    lazy val keySerializer = kafka.getString("key_serializer")
    lazy val valueSerializer = kafka.getString("value_serializer")
    lazy val acks = kafka.getString("acks")
    lazy val clientId = kafka.getString("client_id")
  }

}
