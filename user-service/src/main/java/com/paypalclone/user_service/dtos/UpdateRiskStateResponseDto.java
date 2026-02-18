package com.paypalclone.user_service.dtos;

import com.paypalclone.user_service.enums.RiskState;

public record UpdateRiskStateResponseDto(

        RiskState riskState
) {
}
