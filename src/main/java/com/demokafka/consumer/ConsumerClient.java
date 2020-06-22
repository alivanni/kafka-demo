package com.demokafka.consumer;

import com.demokafka.model.MetricEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsumerClient {

    private final Logger logger = Logger.getLogger(ConsumerClient.class.getName());

    private KafkaConsumer<String, String> kafkaConsumer = null;
    private String topic = null;
    private PostgreSink postgreSink = null;
    private ObjectMapper objectMapper = new ObjectMapper();


    public ConsumerClient(Properties props, String topic) {
        this.kafkaConsumer = new KafkaConsumer<String, String>(props);
        this.topic = topic;

        String dbUri = props.getProperty("db.uri");
        String dbUser = props.getProperty("db.user");
        String dbPassword = props.getProperty("db.password");
        postgreSink = new PostgreSink(dbUri, dbUser, dbPassword);
    }

    public void consume() {
        this.kafkaConsumer.subscribe(Arrays.asList(this.topic));

        while (true) {
            ConsumerRecords<String, String> records = this.kafkaConsumer.poll(1000);
            for (ConsumerRecord<String, String> record : records) {
                String json = record.value();
                try {
                    logger.log(Level.INFO, json);
                    MetricEvent metricEvent = objectMapper.readValue(json, MetricEvent.class);
                    postgreSink.storeEvent(metricEvent);
                } catch (JsonProcessingException e) {
                    logger.log(Level.WARNING, "Unable to deserialize json: " + json, e);
                }

                this.kafkaConsumer.commitAsync();
            }
        }
    }
}
