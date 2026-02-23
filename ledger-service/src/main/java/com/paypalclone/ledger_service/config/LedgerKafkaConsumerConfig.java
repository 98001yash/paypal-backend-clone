package com.paypalclone.ledger_service.config;


import com.paypalclone.payout.PayoutCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class LedgerKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, PayoutCompletedEvent>
    payoutCompletedConsumerFactory() {

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "ledger-service");

        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        // Error handling (REQUIRED in real systems)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                ErrorHandlingDeserializer.class);

        props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS,
                StringDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS,
                JsonDeserializer.class);

        // JSON config (THIS IS THE KEY PART)
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE,
                "com.paypalclone.payout.PayoutCompletedEvent");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "com.paypalclone");
        props.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PayoutCompletedEvent>
    payoutCompletedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PayoutCompletedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(payoutCompletedConsumerFactory());
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }
}