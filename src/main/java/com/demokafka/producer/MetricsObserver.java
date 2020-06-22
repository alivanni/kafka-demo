package com.demokafka.producer;

import com.demokafka.model.MetricEvent;
import com.demokafka.model.MetricType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class MetricsObserver {

    private final Logger logger = Logger.getLogger(MetricsObserver.class.getName());

    private final int COLLECT_INTERVAL = 10; // Periodicity of metrics ping, in seconds
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private IMetricProcessor metricProcessor;

    private ScheduledFuture<?> scheduledFutureCpu = null;
    private ScheduledFuture<?> scheduledFutureMemory = null;

    public MetricsObserver(IMetricProcessor metricProcessor) {
        this.metricProcessor = metricProcessor;
    }

    public void start() {
        final OperatingSystemMXBean operatingSystemMXBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        final ObjectMapper objectMapper = new ObjectMapper();

        final Runnable cpuMetric = new Runnable() {
            public void run() {
                double cpuLoad = operatingSystemMXBean.getSystemLoadAverage();
                MetricEvent metricEvent = new MetricEvent(MetricType.CPU, String.valueOf(cpuLoad));
                try {
                    String metricEventJson = objectMapper.writeValueAsString(metricEvent);
                    metricProcessor.sendMessage(metricEventJson);
                } catch (JsonProcessingException e) {
                    logger.log(Level.WARNING, "Unable to serialize object", e);
                }
            }
        };

        final Runnable memoryMetric = new Runnable() {
            public void run() {
                float freeMemory = (float)operatingSystemMXBean.getFreePhysicalMemorySize() /
                        (float)operatingSystemMXBean.getTotalPhysicalMemorySize() * 100;
                MetricEvent metricEvent = new MetricEvent(MetricType.MEMORY, String.valueOf(freeMemory));
                try {
                    String metricEventJson = objectMapper.writeValueAsString(metricEvent);
                    metricProcessor.sendMessage(metricEventJson);
                } catch (JsonProcessingException e) {
                    logger.log(Level.WARNING, "Unable to serialize object", e);
                }
            }
        };

        this.scheduledFutureCpu = scheduler.scheduleAtFixedRate(cpuMetric, 0, COLLECT_INTERVAL, SECONDS);
        this.scheduledFutureMemory = scheduler.scheduleAtFixedRate(memoryMetric, 0, COLLECT_INTERVAL, SECONDS);
    }

    public void stop() {
        if (this.scheduledFutureCpu != null) {
            this.scheduledFutureCpu.cancel(true);
        }
        if (this.scheduledFutureMemory != null) {
            this.scheduledFutureMemory.cancel(true);
        }
    }
}
