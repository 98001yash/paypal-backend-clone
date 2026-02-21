package com.paypalclone.PaymentIntent;


import com.paypalclone.base.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentCreatedEvent extends BaseEvent {

    private Long paymentIntentId;
    private Long orderId;
    private Long buyerId;
    private Long merchantId;
    private BigDecimal amount;
    private String currency;
}
