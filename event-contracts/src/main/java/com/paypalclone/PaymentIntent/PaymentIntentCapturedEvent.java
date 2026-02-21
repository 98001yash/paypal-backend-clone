package com.paypalclone.PaymentIntent;

import com.paypalclone.base.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentCapturedEvent extends BaseEvent {

    private Long paymentIntentId;
    private Long orderId;
}