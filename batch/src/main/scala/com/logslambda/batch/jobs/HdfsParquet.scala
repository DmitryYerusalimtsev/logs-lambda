package com.logslambda.batch.jobs

import org.apache.spark.sql.{DataFrame, SaveMode}

trait HdfsParquet[T <: DataFrame] {

  private var df: DataFrame = null

  implicit def fromDF(otherDf: DataFrame) = {
    df = otherDf
  }

  def save(implicit path: String) = {
    df.write.mode(SaveMode.Append).parquet(path)
  }
}
