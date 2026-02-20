package com.paypalclone.account_service.config;

import com.paypalclone.user.UserCreatedEvent;
import com.paypalclone.merchant.MerchantCreatedEvent;
import com.paypalclone.merchant.MerchantActivatedEvent;
import com.paypalclone.merchant.MerchantLimitedEvent;
import com.paypalclone.merchant.MerchantSuspendedEvent;
import com.paypalclone.merchant.MerchantRejectedEvent;
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


    @Bean
    public ConsumerFactory<String, UserCreatedEvent> userCreatedConsumerFactory() {

        JsonDeserializer<UserCreatedEvent> deserializer =
                new JsonDeserializer<>(UserCreatedEvent.class);

        deserializer.addTrustedPackages("com.paypalclone.*");
        deserializer.setUseTypeHeaders(false);

        return new DefaultKafkaConsumerFactory<>(
                baseProps("account-service"),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent>
    userCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userCreatedConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }


    private <T> ConcurrentKafkaListenerContainerFactory<String, T>
    merchantFactory(Class<T> clazz) {

        JsonDeserializer<T> deserializer = new JsonDeserializer<>(clazz);
        deserializer.addTrustedPackages("com.paypalclone.*");
        deserializer.setUseTypeHeaders(false);

        ConsumerFactory<String, T> consumerFactory =
                new DefaultKafkaConsumerFactory<>(
                        baseProps("account-service"),
                        new StringDeserializer(),
                        deserializer
                );

        ConcurrentKafkaListenerContainerFactory<String, T> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MerchantCreatedEvent>
    merchantCreatedKafkaListenerContainerFactory() {
        return merchantFactory(MerchantCreatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MerchantActivatedEvent>
    merchantActivatedKafkaListenerContainerFactory() {
        return merchantFactory(MerchantActivatedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MerchantLimitedEvent>
    merchantLimitedKafkaListenerContainerFactory() {
        return merchantFactory(MerchantLimitedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MerchantSuspendedEvent>
    merchantSuspendedKafkaListenerContainerFactory() {
        return merchantFactory(MerchantSuspendedEvent.class);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MerchantRejectedEvent>
    merchantRejectedKafkaListenerContainerFactory() {
        return merchantFactory(MerchantRejectedEvent.class);
    }
}
