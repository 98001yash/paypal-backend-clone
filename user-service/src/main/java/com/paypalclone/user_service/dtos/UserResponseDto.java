package com.paypalclone.user_service.dtos;

import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;
import com.paypalclone.user_service.enums.UserStatus;
import com.paypalclone.user_service.enums.UserType;

import java.time.Instant;

public record UserResponseDto(

        Long id,
        String externalAuthId,
        String email,
        UserType userType,
        UserStatus status,
        KycLevel kycLevel,
        RiskState riskState,
        Instant createdAt,
        Instant updatedAt
) {
}
