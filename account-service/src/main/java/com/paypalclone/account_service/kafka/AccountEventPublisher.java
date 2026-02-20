package com.paypalclone.account_service.kafka;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, String key, Object event){

        log.info("Publishing account event : topic= {}, key={}, type={}",
                topic,
                key,
                event.getClass().getSimpleName());

        kafkaTemplate.send(topic, key, event);
    }
}
