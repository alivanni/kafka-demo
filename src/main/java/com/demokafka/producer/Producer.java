package com.demokafka.producer;

import com.demokafka.utils.Utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer {

    private static final Logger logger = Logger.getLogger(Producer.class.getName());

    public static void main(String[] args) throws IOException {
        Properties props = null;
        try {
            props = Utils.readProperties("producer.properties");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to read Producer config");
            throw e;
        }

        ProducerClient producerClient = new ProducerClient(props, "metrics");

        MetricsObserver metricsObserver = new MetricsObserver(producerClient);
        metricsObserver.start();
    }
}
