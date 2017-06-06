package com.logslambda.speed.storage

import com.datastax.spark.connector.streaming._
import com.logslambda.speed.config.Settings
import org.apache.spark.streaming.dstream.DStream

trait CassandraWriter[T] {

  def saveToCassandra(stream: DStream[T], tableName: String): Unit = {
    stream.saveToCassandra(Settings.Cassandra.keyspace, tableName)
  }
}
