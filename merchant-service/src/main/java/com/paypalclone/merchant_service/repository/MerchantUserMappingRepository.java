package com.paypalclone.merchant_service.repository;

import com.paypalclone.merchant_service.entity.MerchantUserMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantUserMappingRepository
        extends JpaRepository<MerchantUserMapping, Long> {

    Optional<MerchantUserMapping> findByExternalAuthId(String externalAuthId);

    boolean existsByExternalAuthId(String externalAuthId);
}
