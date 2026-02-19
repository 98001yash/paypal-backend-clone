package com.paypalclone.merchant_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchant_user_mapping")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MerchantUserMapping {

    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String externalAuthId;
}
