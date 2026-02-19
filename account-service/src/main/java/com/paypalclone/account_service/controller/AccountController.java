package com.paypalclone.account_service.controller;

import com.paypalclone.account_service.entity.Account;
import com.paypalclone.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;


    // 1️ Get all accounts for an owner
    @GetMapping("/owner/{ownerId}")
    public List<Account> getAccountsForOwner(
            @PathVariable Long ownerId
    ) {
        return accountRepository.findAllByOwnerId(ownerId);
    }


    // 2⃣ Get account by accountId
    @GetMapping("/{accountId}")
    public Account getAccount(
            @PathVariable Long accountId
    ) {
        return accountRepository.findById(accountId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Account not found: " + accountId
                        )
                );
    }
}
