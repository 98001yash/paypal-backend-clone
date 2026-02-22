package com.paypalclone.payout_service.config;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
import com.paypalclone.ledger.LedgerTransactionCompletedEvent;
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

    private Map<String, Object> baseConsumerProps() {
        Map<String, Object> props = new HashMap<>();

        // ---- Kafka connection ----
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        // ---- REQUIRED ----
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "payout-service"
        );

        // ---- Key deserializer ----
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        // ---- Offset & commit control ----
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
    public ConsumerFactory<String, PaymentIntentCapturedEvent>
    paymentIntentCapturedConsumerFactory() {

        JsonDeserializer<PaymentIntentCapturedEvent> deserializer =
                new JsonDeserializer<>(PaymentIntentCapturedEvent.class);

        deserializer.addTrustedPackages("com.paypalclone");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentIntentCapturedEvent>
    paymentIntentCapturedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PaymentIntentCapturedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                paymentIntentCapturedConsumerFactory()
        );

        factory.setConcurrency(3);
        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.MANUAL);

        return factory;
    }

    @Bean
    public ConsumerFactory<String, LedgerTransactionCompletedEvent>
    ledgerTransactionCompletedConsumerFactory() {

        JsonDeserializer<LedgerTransactionCompletedEvent> deserializer =
                new JsonDeserializer<>(LedgerTransactionCompletedEvent.class);

        deserializer.addTrustedPackages("com.paypalclone");

        return new DefaultKafkaConsumerFactory<>(
                baseConsumerProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, LedgerTransactionCompletedEvent>
    ledgerTransactionCompletedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, LedgerTransactionCompletedEvent>
                factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                ledgerTransactionCompletedConsumerFactory()
        );

        factory.setConcurrency(3);
        factory.getContainerProperties()
                .setAckMode(
                        org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL
                );

        return factory;
    }
}