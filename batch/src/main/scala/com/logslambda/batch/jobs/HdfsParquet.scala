package com.logslambda.batch.jobs

import org.apache.spark.sql.{DataFrame, SaveMode}

trait HdfsParquet {

  def partitionColumn: String

  def save(df: DataFrame, path: String) = {
    df.write.partitionBy(partitionColumn).mode(SaveMode.Append).parquet(path)
  }
}
