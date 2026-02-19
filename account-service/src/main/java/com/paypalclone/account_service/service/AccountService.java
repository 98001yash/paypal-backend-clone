package com.paypalclone.account_service.service;

import com.paypalclone.account_service.entity.Account;
import com.paypalclone.account_service.enums.AccountType;

import java.util.List;

public interface AccountService {


    Account createAccount(Long ownerId, AccountType accountType);

    void activateAccount(Long ownerId, AccountType accountType);

    void suspendAccount(Long ownerId, AccountType accountType);

    List<Account>  getAccountsForOwner(Long ownerId);
}
