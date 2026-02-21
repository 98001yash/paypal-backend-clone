package com.paypalclone.order_service.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderLineItemRequest {

    @NotNull
    private String productId;

    @Positive
    private int quantity;

    @Positive
    private BigDecimal unitPrice;
}
