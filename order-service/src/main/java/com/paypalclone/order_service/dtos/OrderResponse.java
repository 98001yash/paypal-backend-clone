package com.paypalclone.order_service.dtos;

import com.paypalclone.order_service.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class OrderResponse {

    private Long orderId;
    private Long buyerId;
    private Long merchantId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String currency;
    private Instant createdAt;
}
