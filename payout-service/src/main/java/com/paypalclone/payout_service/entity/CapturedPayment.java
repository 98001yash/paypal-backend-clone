package com.paypalclone.payout_service.entity;

import com.paypalclone.PaymentIntent.PaymentIntentCapturedEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "captured_payments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"payment_intent_id"})
        }
)
@Getter
@NoArgsConstructor
public class CapturedPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_intent_id", nullable = false)
    private Long paymentIntentId;

    @Column(name = "merchant_ledger_account_id", nullable = false)
    private Long merchantLedgerAccountId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    private Instant createdAt = Instant.now();

    public static CapturedPayment fromEvent(
            PaymentIntentCapturedEvent event
    ) {
        CapturedPayment cp = new CapturedPayment();
        cp.paymentIntentId = event.getPaymentIntentId();
        cp.merchantLedgerAccountId = event.getCreditExternalAccountId();
        cp.amount = event.getAmount();
        cp.currency = event.getCurrency();
        return cp;
    }
}