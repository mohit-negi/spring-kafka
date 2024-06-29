package com.ewe.solarKafka.kafka;

import com.ewe.solarKafka.model.InverterData;
import org.json.JSONArray;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
public class KafkaMsgListener {
    static JSONArray jsonArray = new JSONArray();

    @KafkaListener(id = "id-1", groupId = "group-1", topics = "inverterData", containerFactory = "msgListenerFactory", autoStartup = "false")
    public void consumeMsg(InverterData inverterData) throws IOException {
        System.out.println("Message Received::" + inverterData);
        jsonArray.put("Name");
        jsonArray.put(inverterData.getName());
    }

}
