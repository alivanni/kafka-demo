package com.demokafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class ProducerClient implements IMetricProcessor {

    private KafkaProducer<String, String> kafkaProducer = null;
    private String topic = null;

    public ProducerClient(Properties props, String topic) {
        this.kafkaProducer = new KafkaProducer<String, String>(props);
        this.topic = topic;
    }

    public void sendMessage(String message) {
        this.kafkaProducer.send(new ProducerRecord<String, String>(this.topic, message));
    }

    public void close() {
        this.kafkaProducer.close();
    }
}
