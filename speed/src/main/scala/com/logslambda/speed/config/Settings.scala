package com.logslambda.speed.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()

  object Speed {
    private val speed = config.getConfig("speed")

    lazy val batchDuration = speed.getInt("batch_duration")
    lazy val hdfsPath = speed.getString("hdfs_path")
  }

  object Kafka {
    private val kafka = config.getConfig("kafka")

    lazy val topic = kafka.getString("topic")
    lazy val broker = kafka.getString("broker")
    lazy val groupId = kafka.getString("group_id")
    lazy val offsetReset = kafka.getString("offset_reset")
  }

  object Cassandra {
    private val cassandra = config.getConfig("cassandra")

    lazy val keyspace = cassandra.getString("keyspace")
    lazy val activityByLogTable = cassandra.getString("activity_by_log_table")
  }

}
