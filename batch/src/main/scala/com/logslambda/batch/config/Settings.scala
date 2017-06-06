package com.logslambda.batch.config

import com.typesafe.config.ConfigFactory

object Settings {
  private val config = ConfigFactory.load()
  private val batch = config.getConfig("batch")

  lazy val hdfsPath = batch.getString("hdfs_path")
  lazy val hdfsLogsPath = batch.getString("hdfs_logs_path")

  object Cassandra {
    private val cassandra = config.getConfig("cassandra")

    lazy val keyspace = cassandra.getString("keyspace")
    lazy val visitorsByProductTable = cassandra.getString("visitors_by_product_table")
    lazy val activityByProductTable = cassandra.getString("activity_by_product_table")
  }

}
