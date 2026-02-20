package com.paypalclone.ledger_service.config;

import com.paypalclone.account.AccountActivatedEvent;
import com.paypalclone.account.AccountCreatedEvent;
import com.paypalclone.account.AccountSuspendedEvent;
import com.paypalclone.account.AccountClosedEvent;
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
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    private <T> ConcurrentKafkaListenerContainerFactory<String, T>
    ledgerFactory(Class<T> clazz) {

        JsonDeserializer<T> deserializer = new JsonDeserializer<>(clazz);
        deserializer.addTrustedPackages("com.paypalclone.*");
        deserializer.setUseTypeHeaders(false);

        ConsumerFactory<String, T> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        baseProps("ledger-service"),
                        new StringDeserializer(),
                        deserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountCreatedEvent>
    accountCreatedKafkaListenerContainerFactory() {
        return ledgerFactory(AccountCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountActivatedEvent>
    accountActivatedKafkaListenerContainerFactory() {
        return ledgerFactory(AccountActivatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountSuspendedEvent>
    accountSuspendedKafkaListenerContainerFactory() {
        return ledgerFactory(AccountSuspendedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, AccountClosedEvent>
    accountClosedKafkaListenerContainerFactory() {
        return ledgerFactory(AccountClosedEvent.class);
    }
}
