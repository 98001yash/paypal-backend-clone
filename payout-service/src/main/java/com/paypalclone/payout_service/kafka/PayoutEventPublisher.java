package com.paypalclone.payout_service.kafka;

import com.paypalclone.payout.PayoutCompletedEvent;
import com.paypalclone.payout_service.entity.Payout;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PayoutEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishCompleted(Payout payout) {

        PayoutCompletedEvent event =
                PayoutCompletedEvent.builder()
                        .payoutId(payout.getId())
                        .ledgerAccountId(payout.getLedgerAccountId())
                        .amount(payout.getAmount())
                        .currency(payout.getCurrency())
                        .build();

        log.info(
                "Publishing PayoutCompletedEvent payoutId={}, accountId={}, amount={}",
                payout.getId(),
                payout.getLedgerAccountId(),
                payout.getAmount()
        );

        kafkaTemplate.send(
                KafkaTopics.PAYOUT_COMPLETED,
                payout.getId().toString(),
                event
        );
    }
}