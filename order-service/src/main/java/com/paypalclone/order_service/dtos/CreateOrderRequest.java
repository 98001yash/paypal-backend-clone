package com.paypalclone.order_service.dtos;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {


    @NotNull
    private Long merchantId;

    @NotNull
    private String currency;

    @NotEmpty
    private List<OrderLineItemRequest> items;
}
