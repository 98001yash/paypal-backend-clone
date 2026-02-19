package com.paypalclone.account_service.repository;

import com.paypalclone.account_service.entity.Account;
import com.paypalclone.account_service.enums.AccountType;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {


    Optional<Account> findByOwnerIdAndAccountType(Long ownerId, AccountType accounttype);
    boolean existsByOwnerIdAndAccountType(Long ownerId, AccountType accountType);
    List<Account>  findAllByOwnerId(Long ownerId);
}
