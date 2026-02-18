package com.paypalclone.user_service.dtos;

import com.paypalclone.user_service.enums.KycLevel;

public record UpdateKycLevelRequestDto(

        KycLevel kycLevel
) {
}
