package com.paypalclone.merchant_service.config;

import com.paypalclone.user.UserKycUpdatedEvent;
import com.paypalclone.user.UserRiskUpdatedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {


    private Map<String, Object> baseConsumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }


    @Bean
    public ConsumerFactory<String, UserKycUpdatedEvent>
    userKycUpdatedConsumerFactory() {

        JsonDeserializer<UserKycUpdatedEvent> deserializer =
                new JsonDeserializer<>(UserKycUpdatedEvent.class);

        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps("merchant-service"),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserKycUpdatedEvent>
    userKycKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserKycUpdatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userKycUpdatedConsumerFactory());
        factory.setConcurrency(1); // keep 1 while debugging

        return factory;
    }

    @Bean
    public ConsumerFactory<String, UserRiskUpdatedEvent>
    userRiskUpdatedConsumerFactory() {

        JsonDeserializer<UserRiskUpdatedEvent> deserializer =
                new JsonDeserializer<>(UserRiskUpdatedEvent.class);

        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps("merchant-service"),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserRiskUpdatedEvent>
    userRiskKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserRiskUpdatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userRiskUpdatedConsumerFactory());
        factory.setConcurrency(1);

        return factory;
    }
}
