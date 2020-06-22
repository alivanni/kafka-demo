package com.demokafka.producer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;

public class MetricsObserverTest {

    private MetricsObserver metricsObserver = null;
    private FakeProcessor metricProcessor = null;

    private class FakeProcessor implements IMetricProcessor {

        AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void sendMessage(String message) {
            counter.incrementAndGet();
        }
    }

    @Before
    public void setUp() throws Exception {
        metricProcessor = new FakeProcessor();
        metricsObserver = new MetricsObserver(metricProcessor);
    }

    @After
    public void tearDown() throws Exception {
        metricsObserver.stop();
        metricsObserver = null;
        metricProcessor = null;
    }

    @Test
    public void start() throws InterruptedException {
        metricsObserver.start();
        Thread.sleep(1000);
        assertEquals(2, metricProcessor.counter.get());
    }
}
