package com.ewe.solarKafka.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class KafkaManager {
    private final OrderConsumeService orderConsumeService;

    public KafkaManager(OrderConsumeService orderConsumeService) {
        this.orderConsumeService = orderConsumeService;
    }

    public String addTopics(Set<String> topics) {
        return orderConsumeService.registerListener(topics);
    }

    public void removeListener(String listenerId) {
        orderConsumeService.deregisterListener(listenerId);
    }

    public void startAll() {
        orderConsumeService.startAllListeners();
    }

    public void stopAll() {
        orderConsumeService.stopAllListeners();
    }
}