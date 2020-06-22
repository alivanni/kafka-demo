package com.demokafka.model;

import java.time.Instant;

public class MetricEvent {

    private MetricType metricType;
    private String value;
    private long timestamp;

    public MetricEvent() {
        // For jackson
    }

    public MetricEvent(MetricType metricType, String value) {
        this.metricType = metricType;
        this.value = value;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public void setMetricType(MetricType metricType) {
        this.metricType = metricType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
