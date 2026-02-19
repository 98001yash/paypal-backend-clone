package com.paypalclone.merchant_service.kafka;

import com.paypalclone.base.BaseEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantEventPublisher {

    private final EventPublisher eventPublisher;

    public void publish(String topic, String key, BaseEvent event) {

        log.info(
                "Publishing merchant event: type={}, version={}, topic={}, key={}",
                event.getEventType(),
                event.getEventVersion(),
                topic,
                key
        );

        eventPublisher.publish(topic, key, event);

        log.debug(
                "Merchant event published successfully: type={}, topic={}",
                event.getEventType(),
                topic
        );
    }
}
