package com.paypalclone.user_service.service;

import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;

public interface UserComplianceService {

    void updateKycLevel(String externalAuthId, KycLevel kycLevel);

    void updateRiskState(String externalAuthId, RiskState riskState);
}
