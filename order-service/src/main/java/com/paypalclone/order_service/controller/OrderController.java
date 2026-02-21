package com.paypalclone.order_service.controller;

import com.paypalclone.order_service.auth.UserContextHolder;
import com.paypalclone.order_service.dtos.CreateOrderRequest;
import com.paypalclone.order_service.dtos.OrderLineItemRequest;
import com.paypalclone.order_service.dtos.OrderResponse;
import com.paypalclone.order_service.entity.Order;
import com.paypalclone.order_service.entity.OrderLineItem;
import com.paypalclone.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse createOrder(
            @Valid @RequestBody CreateOrderRequest request
    ) {
        Long buyerId = UserContextHolder.getCurrentUserId();

        log.info("API: create order buyerId={} merchantId={}",
                buyerId, request.getMerchantId());

        Order order = orderService.createOrder(
                buyerId,
                request.getMerchantId(),
                request.getCurrency(),
                mapItems(request.getItems())
        );
        return mapToResponse(order);
    }

    @GetMapping("/{orderId}")
    public OrderResponse getOrder(@PathVariable Long orderId) {

        Long buyerId = UserContextHolder.getCurrentUserId();

        log.info("API: get order orderId={} buyerId={}", orderId, buyerId);
        Order order = orderService.getOrder(orderId);
        return mapToResponse(order);
    }


    @PostMapping("/{orderId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void confirmOrder(@PathVariable Long orderId) {

        Long buyerId = UserContextHolder.getCurrentUserId();
        log.info("API: confirm order orderId={} buyerId={}", orderId, buyerId);
        orderService.confirmOrder(orderId);
    }


    @PostMapping("/{orderId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable Long orderId) {

        Long buyerId = UserContextHolder.getCurrentUserId();
        log.info("API: cancel order orderId={} buyerId={}", orderId, buyerId);
        orderService.cancelOrder(orderId);
    }

    // - MAPPERS

    private List<OrderLineItem> mapItems(List<OrderLineItemRequest> items) {
        return items.stream()
                .map(i -> new OrderLineItem(
                        i.getProductId(),
                        i.getQuantity(),
                        i.getUnitPrice()
                ))
                .toList();
    }

    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .buyerId(order.getBuyerId())
                .merchantId(order.getMerchantId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .currency(order.getCurrency())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
