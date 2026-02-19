package com.paypalclone.merchant_service.kafka;

import com.paypalclone.base.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(String topic, String key, BaseEvent event) {

        log.info(
                "Publishing event: topic={}, type={}, version={}, key={}",
                topic,
                event.getEventType(),
                event.getEventVersion(),
                key
        );

        kafkaTemplate.send(topic, key, event);
    }
}