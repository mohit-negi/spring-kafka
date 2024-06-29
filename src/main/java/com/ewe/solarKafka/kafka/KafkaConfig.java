package com.ewe.solarKafka.kafka;

import com.ewe.solarKafka.model.InverterData;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class KafkaConfig {
//    @Value("${bootstrap-servers}")
    private static final String kafkaUrl = "localhost:9092";

    @Bean
    public NewTopic topic (){

        return TopicBuilder
                .name("inverterData")
//                .partitions()
//                .replicas()
                .build();
    }

    @Bean
    public ConsumerFactory<Long, InverterData> msgCosummerFactory() {
        JsonDeserializer<InverterData> deserializer = new JsonDeserializer<>(InverterData.class, false);
        deserializer.setRemoveTypeHeaders(false);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "group-1");
        return new DefaultKafkaConsumerFactory<>(config, new LongDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, InverterData> msgListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<Long, InverterData> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(msgCosummerFactory());
        return factory;
    }
}
