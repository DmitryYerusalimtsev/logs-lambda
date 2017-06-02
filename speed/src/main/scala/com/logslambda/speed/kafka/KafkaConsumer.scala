package com.logslambda.speed.kafka

import com.logslambda.speed.config.Settings
import kafka.serializer.StringDecoder
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils

trait KafkaConsumer {

  val ssc: StreamingContext

  val kafkaParams = Map(
    "zookeeper.connect" -> Settings.Kafka.zookeper,
    "group.id" -> Settings.Kafka.groupId,
    "auto.offset.reset" -> Settings.Kafka.offsetReset
  )

  val kafkaStream = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaParams, Map(Settings.Kafka.topic -> 1), StorageLevel.MEMORY_AND_DISK
  ).map(_._2)
}
