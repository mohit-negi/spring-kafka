package com.ewe.solarKafka.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class OrderConsumeService {

    private final KafkaListenerEndpointRegistry registry;
    private final KafkaListenerContainerFactory<?> kafkaListenerContainerFactory;

    public OrderConsumeService(KafkaListenerEndpointRegistry registry,
                               KafkaListenerContainerFactory<?> kafkaListenerContainerFactory) {
        this.registry = registry;
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
    }

    public String registerListener(Set<String> topics) {
        String id = "dynamicListener-" + UUID.randomUUID().toString();

        MethodKafkaListenerEndpoint<String, String> endpoint = new MethodKafkaListenerEndpoint<>();
        endpoint.setId(id);
        endpoint.setTopics(topics.toArray(new String[0]));
        endpoint.setBean(this);
        endpoint.setMethod(ReflectionUtils.findMethod(getClass(), "listen", ConsumerRecord.class));
        endpoint.setMessageHandlerMethodFactory(new DefaultMessageHandlerMethodFactory());
        endpoint.setGroup("dynamic-group");

        registry.registerListenerContainer(endpoint, kafkaListenerContainerFactory, true);

        return id;
    }

    public void deregisterListener(String listenerId) {
        MessageListenerContainer listenerContainer = registry.getListenerContainer(listenerId);
        if (listenerContainer != null) {
            listenerContainer.stop();
            registry.unregisterListenerContainer(listenerId);
        }
    }

    public void startAllListeners() {
        registry.getListenerContainers().forEach(MessageListenerContainer::start);
    }

    public void stopAllListeners() {
        registry.getListenerContainers().forEach(MessageListenerContainer::stop);
    }

    // This is the actual listener method
//    public void listen(ConsumerRecord<String, String> record) {
//        log.info("Order listened : {}", record);
//    }


    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received message: " + record.value() + " from topic: " + record.topic());
    }
}