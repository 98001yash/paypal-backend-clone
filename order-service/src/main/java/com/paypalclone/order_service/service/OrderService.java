package com.paypalclone.order_service.service;


import com.paypalclone.order_service.entity.Order;
import com.paypalclone.order_service.entity.OrderLineItem;
import com.paypalclone.order_service.enums.OrderStatus;
import com.paypalclone.order_service.exceptions.InvalidOrderStateException;
import com.paypalclone.order_service.exceptions.OrderNotFoundException;
import com.paypalclone.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;


    // create a new order in CREATED state

    @Transactional
    public Order createOrder(Long buyerId, Long merchantId,
                             String currency, List<OrderLineItem> items){
        log.info("Creating a order for buyer={}, merchant={}, items={}",
                buyerId, merchantId, items.size());

        Order order = new Order(buyerId, merchantId, currency);

        items.forEach(order::addLineItem);
        Order saved = orderRepository.save(order);

        log.info("Order created with id={}, total={}",
                saved.getId(), saved.getTotalAmount());
        return saved;
    }

    // confirm order (CREATED -> CONFIRMED)

    @Transactional
    public void confirmOrder(Long orderId) {

        Order order = getOrder(orderId);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    orderId,
                    order.getStatus(),
                    "confirm"
            );
        }
        order.confirm();
        log.info("Order {} confirmed", orderId);
    }


     // Cancel order (CREATED → CANCELLED)

    @Transactional
    public void cancelOrder(Long orderId) {

        Order order = getOrder(orderId);

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateException(
                    orderId,
                    order.getStatus(),
                    "cancel"
            );
        }
        order.cancel();
        log.info("Order {} cancelled", orderId);
    }

     // Read-only access

    @Transactional(readOnly = true)
    public Order getOrder(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order {} not found", orderId);
                    return new OrderNotFoundException(orderId);
                });
    }
}
