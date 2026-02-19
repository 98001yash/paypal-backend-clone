package com.paypalclone.account_service.service.Impl;

import com.paypalclone.account_service.entity.Account;
import com.paypalclone.account_service.enums.AccountStatus;
import com.paypalclone.account_service.enums.AccountType;
import com.paypalclone.account_service.repository.AccountRepository;
import com.paypalclone.account_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;



    @Override
    public Account createAccount(Long ownerId, AccountType accountType) {

        //  Idempotency (CRITICAL)
        return accountRepository
                .findByOwnerIdAndAccountType(ownerId, accountType)
                .orElseGet(() -> {

                    Account account = Account.create(ownerId, accountType);
                    accountRepository.save(account);

                    log.info(
                            "Account created: accountId={}, ownerId={}, type={}",
                            account.getAccountId(),
                            ownerId,
                            accountType
                    );

                    return account;
                });
    }


    @Override
    public void activateAccount(Long ownerId, AccountType accountType) {

        Account account = accountRepository
                .findByOwnerIdAndAccountType(ownerId, accountType)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Account not found for ownerId=" + ownerId +
                                        ", type=" + accountType
                        )
                );
        if (account.getStatus() == AccountStatus.ACTIVE) {
            log.info(
                    "Account already ACTIVE: ownerId={}, type={}",
                    ownerId,
                    accountType
            );
            return;
        }
        account.activate();
        log.info(
                "Account activated: accountId={}, ownerId={}, type={}",
                account.getAccountId(),
                ownerId,
                accountType
        );
    }


    @Override
    public void suspendAccount(Long ownerId, AccountType accountType) {

        Account account = accountRepository
                .findByOwnerIdAndAccountType(ownerId, accountType)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Account not found for ownerId=" + ownerId +
                                        ", type=" + accountType
                        )
                );
        if (account.getStatus() == AccountStatus.SUSPENDED) {
            log.info(
                    "Account already SUSPENDED: ownerId={}, type={}",
                    ownerId,
                    accountType
            );
            return;
        }
        account.suspend();
        log.info(
                "Account suspended: accountId={}, ownerId={}, type={}",
                account.getAccountId(),
                ownerId,
                accountType
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<Account> getAccountsForOwner(Long ownerId) {
        return accountRepository.findAllByOwnerId(ownerId);
    }
}
