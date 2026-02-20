package com.paypalclone.balance_project_service.config;

import com.paypalclone.ledger.LedgerEntryPostedEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private Map<String, Object> baseProps(String groupId) {

        Map<String, Object> props = new HashMap<>();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                groupId
        );
        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );
        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                false
        );

        return props;
    }

    @Bean
    public ConsumerFactory<String, LedgerEntryPostedEvent>
    ledgerEntryPostedConsumerFactory() {

        JsonDeserializer<LedgerEntryPostedEvent> deserializer =
                new JsonDeserializer<>(LedgerEntryPostedEvent.class);

        deserializer.addTrustedPackages("com.paypalclone.*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseProps("balance-projection-service"),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String, LedgerEntryPostedEvent>
    ledgerEntryPostedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String, LedgerEntryPostedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                ledgerEntryPostedConsumerFactory()
        );

        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }
}