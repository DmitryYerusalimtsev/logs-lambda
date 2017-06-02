package com.logslambda.speed.kafka

import com.logslambda.speed.config.Settings
import kafka.serializer.StringDecoder
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.kafka.KafkaUtils

trait KafkaConsumer {

  val ssc: StreamingContext

  private val kafkaParams = Map(
    "metadata.broker.list" -> Settings.Kafka.broker,
    "group.id" -> Settings.Kafka.groupId,
    "auto.offset.reset" -> Settings.Kafka.offsetReset
  )

  val kafkaStream = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](
    ssc, kafkaParams, Set(Settings.Kafka.topic)
  )
}
