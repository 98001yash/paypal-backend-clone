package com.paypalclone.order_service.kafka;


import com.paypalclone.order_service.entity.Order;
import com.paypalclone.orders.OrderConfirmedEvent;
import com.paypalclone.orders.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public void publishOrderCreated(Order order) {

        OrderCreatedEvent event =
                OrderCreatedEvent.builder()
                        .orderId(order.getId())
                        .buyerId(order.getBuyerId())
                        .merchantId(order.getMerchantId())
                        .totalAmount(order.getTotalAmount())
                        .currency(order.getCurrency())
                        .build();

        log.info("Publishing OrderCreatedEvent orderId={}", order.getId());

        kafkaTemplate.send(
                KafkaTopics.ORDER_CREATED,
                order.getId().toString(),
                event
        );
    }

        public void publishOrderConfirmed(Long orderId) {

            OrderConfirmedEvent event =
                    OrderConfirmedEvent.builder()
                            .orderId(orderId)
                            .build();

            log.info("Publishing OrderConfirmedEvent orderId={}", orderId);

            kafkaTemplate.send(
                    KafkaTopics.ORDER_CONFIRMED,
                    orderId.toString(),
                    event
            );
        }
}
