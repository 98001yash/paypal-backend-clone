package com.paypalclone.balance_project_service.controller;

import com.paypalclone.balance_project_service.dtos.BalanceResponse;
import com.paypalclone.balance_project_service.service.BalanceReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceReadService readService;


    @GetMapping("/{ledgerAccountId}")
    public BalanceResponse getBalance(
            @PathVariable Long ledgerAccountId,
            @RequestParam String currency
    ) {
        return BalanceResponse.from(
                readService.getBalance(ledgerAccountId, currency)
        );
    }


    @GetMapping("/{ledgerAccountId}/all")
    public List<BalanceResponse> getAllBalances(
            @PathVariable Long ledgerAccountId
    ) {
        return readService.getAllBalance(ledgerAccountId)
                .stream()
                .map(BalanceResponse::from)
                .toList();
    }
}