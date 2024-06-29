package com.ewe.solarKafka.controller;

import com.ewe.solarKafka.kafka.KafkaActivity;
import com.ewe.solarKafka.kafka.KafkaMsgListener;
import com.ewe.solarKafka.model.InverterData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class KafkaController {

    @Autowired
    private KafkaActivity kafkaActivity;

    @Autowired
    KafkaMsgListener kafkaMsgListener;

    @Autowired
    private KafkaTemplate<Long, InverterData> kafkaTemplate;

    @PostMapping("/startListener")
    public void start(@RequestParam String id) {
        kafkaActivity.startListener(id);
    }

    @PostMapping("/stopListener")
    public void stop(@RequestParam String id) {
        kafkaActivity.stopListener(id);
    }

    @PostMapping("/sendMessage")
    public void send(@RequestParam("message") String message,@RequestParam("topic") String topic) throws IOException {
        InverterData inverterData = new InverterData();
        inverterData.setName(message);
        System.out.println("----------------------------------------------------------------------------------sending message ");
        kafkaTemplate.send(topic,inverterData);
    }
}
