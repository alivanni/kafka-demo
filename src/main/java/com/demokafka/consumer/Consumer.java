package com.demokafka.consumer;

import com.demokafka.utils.Utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer {

    private static final Logger logger = Logger.getLogger(Consumer.class.getName());

    public static void main(String[] args) throws IOException {
        Properties props = null;
        try {
            props = Utils.readProperties("consumer.properties");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to read Consumer config");
            throw e;
        }

        ConsumerClient consumerClient = new ConsumerClient(props, "metrics");
        consumerClient.consume();
    }
}
