package com.paypalclone.orders;

import com.paypalclone.base.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent extends BaseEvent {

    private Long orderId;
    private Long buyerId;
    private Long merchantId;
    private BigDecimal totalAmount;
    private String currency;
}
