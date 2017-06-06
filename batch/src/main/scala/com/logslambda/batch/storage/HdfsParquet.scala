package com.logslambda.batch.storage

import org.apache.spark.sql.{DataFrame, SaveMode}

trait HdfsParquet {

  def partitionColumn: String

  def saveToHdfs(df: DataFrame, path: String) = {
    df.write.partitionBy(partitionColumn).mode(SaveMode.Append).parquet(path)
  }
}
