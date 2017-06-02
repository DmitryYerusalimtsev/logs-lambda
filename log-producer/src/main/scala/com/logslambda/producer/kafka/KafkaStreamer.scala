package com.logslambda.producer.kafka

import java.util.Properties

import com.logslambda.producer.config.Settings
import org.apache.kafka.clients.producer.{KafkaProducer, Producer, ProducerConfig, ProducerRecord}

trait KafkaStreamer {

  private val topic = Settings.Kafka.topic

  private lazy val config = {
    val props = new Properties()

    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Settings.Kafka.server)
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, Settings.Kafka.keySerializer)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, Settings.Kafka.valueSerializer)
    props.put(ProducerConfig.ACKS_CONFIG, Settings.Kafka..acks)
    props.put(ProducerConfig.CLIENT_ID_CONFIG, Settings.Kafka.clientId)

    props
  }

  private val kafkaProducer: Producer[Nothing, String] = new KafkaProducer[Nothing, String](config)

  def send(line: String): Unit = {
    val producerRecord = new ProducerRecord(topic, line)
    kafkaProducer.send(producerRecord)
  }

  def close(): Unit = {
    kafkaProducer.close()
  }
}
