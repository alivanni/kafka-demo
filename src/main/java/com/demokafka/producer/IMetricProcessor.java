package com.demokafka.producer;

public interface IMetricProcessor {

    void sendMessage(String message);
}
