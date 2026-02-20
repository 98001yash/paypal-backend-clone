package com.paypalclone.balance_project_service.service;


import com.paypalclone.balance_project_service.entity.BalanceProjection;
import com.paypalclone.balance_project_service.repository.BalanceProjectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BalanceReadService {


    private final BalanceProjectionRepository repository;

    public BalanceProjection getBalance(Long ledgerAccountId, String currency){

        return repository.findByLedgerAccountIdAndCurrency(ledgerAccountId,currency)
                .orElseThrow(()->
                        new IllegalArgumentException("Balance not found for account: "+
                                ledgerAccountId + "and currency "+currency));

    }

    public List<BalanceProjection> getAllBalance(Long ledgerAccountId){
        return repository.findByLedgerAccountId(ledgerAccountId);
    }
}
