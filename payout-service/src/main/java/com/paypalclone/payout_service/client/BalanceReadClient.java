package com.paypalclone.payout_service.client;

import com.paypalclone.payout_service.dtos.BalanceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class BalanceReadClient {

    private final RestTemplate restTemplate;

    public BalanceResponse getBalance(
            Long ledgerAccountId,
            String currency
    ) {
        String url =
                "http://localhost:8095/balances/"
                        + ledgerAccountId
                        + "?currency="
                        + currency;

        return restTemplate.getForObject(
                url,
                BalanceResponse.class
        );
    }
}