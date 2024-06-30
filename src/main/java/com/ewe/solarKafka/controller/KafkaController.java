package com.ewe.solarKafka.controller;

import com.ewe.solarKafka.service.KafkaManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/kafka")
public class KafkaController {

    private final KafkaManager kafkaManager;
    private final Map<String, Set<String>> listenerTopicMap = new ConcurrentHashMap<>();

    public KafkaController(KafkaManager kafkaManager) {
        this.kafkaManager = kafkaManager;
    }

    @PostMapping("/listeners")
    public ResponseEntity<Map<String, String>> createListener(@RequestBody Set<String> topics) {
        String listenerId = kafkaManager.addTopics(topics);
        listenerTopicMap.put(listenerId, topics);
        
        Map<String, String> response = new HashMap<>();
        response.put("listenerId", listenerId);
        response.put("message", "Listener created successfully");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/listeners/{listenerId}")
    public ResponseEntity<Map<String, String>> removeListener(@PathVariable String listenerId) {
        kafkaManager.removeListener(listenerId);
        listenerTopicMap.remove(listenerId);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Listener removed successfully");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/listeners/{listenerId}/topics")
    public ResponseEntity<Map<String, String>> addTopicsToListener(@PathVariable String listenerId, @RequestBody Set<String> newTopics) {
        Set<String> existingTopics = listenerTopicMap.get(listenerId);
        if (existingTopics == null) {
            return ResponseEntity.notFound().build();
        }
        
        kafkaManager.removeListener(listenerId);
        existingTopics.addAll(newTopics);
        String newListenerId = kafkaManager.addTopics(existingTopics);
        listenerTopicMap.put(newListenerId, existingTopics);
        
        Map<String, String> response = new HashMap<>();
        response.put("listenerId", newListenerId);
        response.put("message", "Topics added to listener successfully");
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/listeners/{listenerId}/topics")
    public ResponseEntity<Map<String, String>> removeTopicsFromListener(@PathVariable String listenerId, @RequestBody Set<String> topicsToRemove) {
        Set<String> existingTopics = listenerTopicMap.get(listenerId);
        if (existingTopics == null) {
            return ResponseEntity.notFound().build();
        }
        
        kafkaManager.removeListener(listenerId);
        existingTopics.removeAll(topicsToRemove);
        
        if (existingTopics.isEmpty()) {
            listenerTopicMap.remove(listenerId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "All topics removed. Listener deleted.");
            return ResponseEntity.ok(response);
        }
        
        String newListenerId = kafkaManager.addTopics(existingTopics);
        listenerTopicMap.put(newListenerId, existingTopics);
        
        Map<String, String> response = new HashMap<>();
        response.put("listenerId", newListenerId);
        response.put("message", "Topics removed from listener successfully");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/listeners")
    public ResponseEntity<Map<String, Set<String>>> getAllListeners() {
        return ResponseEntity.ok(listenerTopicMap);
    }

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startAllListeners() {
        kafkaManager.startAll();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All listeners started");
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stopAllListeners() {
        kafkaManager.stopAll();
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "All listeners stopped");
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("activeListeners", String.valueOf(listenerTopicMap.size()));
        
        return ResponseEntity.ok(status);
    }
}