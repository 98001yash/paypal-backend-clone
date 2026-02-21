package com.paypalclone.ledger_service.config;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
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
public class PaymentIntentKafkaConsumerConfig {

    private Map<String, Object> baseProps() {

        Map<String, Object> props = new HashMap<>();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092"
        );

        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "ledger-payment-consumers"
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

    // -------- PAYMENT INTENT CAPTURED --------

    @Bean
    public ConsumerFactory<String, PaymentIntentCapturedEvent>
    paymentIntentCapturedConsumerFactory() {

        JsonDeserializer<PaymentIntentCapturedEvent> deserializer =
                new JsonDeserializer<>(PaymentIntentCapturedEvent.class);

        deserializer.addTrustedPackages("com.paypalclone.*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<
            String, PaymentIntentCapturedEvent>
    paymentIntentCapturedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<
                String, PaymentIntentCapturedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(
                paymentIntentCapturedConsumerFactory()
        );

        factory.getContainerProperties()
                .setAckMode(ContainerProperties.AckMode.RECORD);

        return factory;
    }
}