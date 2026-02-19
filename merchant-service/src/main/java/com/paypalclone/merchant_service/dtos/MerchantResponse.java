package com.paypalclone.merchant_service.dtos;

import com.paypalclone.merchant_service.entity.Merchant;
import com.paypalclone.merchant_service.enums.MerchantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.Instant;


@Getter
    @AllArgsConstructor
    public class MerchantResponse {

        private Long merchantId;
        private String userId;
        private String businessName;
        private String businessType;
        private String country;
        private MerchantStatus status;
        private Instant createdAt;
        private Instant updatedAt;

        public static MerchantResponse from(Merchant merchant) {
            return new MerchantResponse(
                    merchant.getMerchantId(),
                    merchant.getUserId(),
                    merchant.getBusinessName(),
                    merchant.getBusinessType(),
                    merchant.getCountry(),
                    merchant.getStatus(),
                    merchant.getCreatedAt(),
                    merchant.getUpdatedAt()
            );
        }
}
