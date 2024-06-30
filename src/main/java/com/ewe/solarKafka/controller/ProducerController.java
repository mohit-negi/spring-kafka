package com.ewe.solarKafka.controller;

import com.ewe.solarKafka.service.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/api")
public class ProducerController {
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @PostMapping("/produce")
    public ResponseEntity<Map<String, String>> produceMessage(@RequestParam String topic, @RequestBody String message) {
        kafkaProducerService.sendMessage(topic, message);

        Map<String, String> response = new HashMap<>();
        log.info("Message sent successfully to topic: " + topic);
        response.put("message", "Message sent successfully to topic: " + topic);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/produce-batch")
    public ResponseEntity<Map<String, String>> produceMessages(@RequestBody Map<String, String> topicMessages) {
        for (Map.Entry<String, String> entry : topicMessages.entrySet()) {
            kafkaProducerService.sendMessage(entry.getKey(), entry.getValue());
        }

        Map<String, String> response = new HashMap<>();
        log.info("Batch messages sent successfully");

        response.put("message", "Batch messages sent successfully");

        return ResponseEntity.ok(response);
    }
}
