package com.ewe.solarKafka.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class KafkaActivity {

    @Autowired
    private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

    public boolean startListener(String id) {
        MessageListenerContainer container = kafkaListenerEndpointRegistry.getListenerContainer(id);
        container.start();
        System.out.println("Listener started with ----------------------------------------------------------------" + id);
        return true;
    }

    public boolean stopListener(String id) {
        MessageListenerContainer container = kafkaListenerEndpointRegistry.getListenerContainer(id);
        container.stop();
        System.out.println("Listener stopped with --------------------------------------------------------------------" + id);

        return true;
    }
}
