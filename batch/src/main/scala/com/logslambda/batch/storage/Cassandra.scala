package com.logslambda.batch.storage

import com.logslambda.batch.config.Settings
import org.apache.spark.sql.DataFrame

trait Cassandra {

  private var options = Map(
    "keyspace" -> Settings.Cassandra.keyspace
  )

  def saveToCassandra(df: DataFrame, tableName: String): Unit = {
    options += "table" -> tableName

    df.write.format("org.apache.spark.sql.cassandra")
      .options(options).save()
  }
}
