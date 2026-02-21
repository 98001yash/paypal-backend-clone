package com.paypalclone.PaymentIntent;

import com.paypalclone.base.BaseEvent;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentIntentCapturedEvent {

    private Long paymentIntentId;
    private Long orderId;

    // ✅ External account IDs (ledger-facing)
    private Long debitExternalAccountId;
    private Long creditExternalAccountId;

    private BigDecimal amount;
    private String currency;
}