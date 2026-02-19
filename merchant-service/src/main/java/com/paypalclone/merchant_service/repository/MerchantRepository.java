package com.paypalclone.merchant_service.repository;

import com.paypalclone.merchant_service.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository<Merchant,Long> {

    Optional<Merchant>  findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
