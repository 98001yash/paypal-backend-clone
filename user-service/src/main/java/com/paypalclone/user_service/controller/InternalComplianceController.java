package com.paypalclone.user_service.controller;


import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;
import com.paypalclone.user_service.service.UserComplianceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalComplianceController {

    private final UserComplianceService complianceService;

    // Called by KYC Service
    @PutMapping("/{authUserId}/kyc")
    public ResponseEntity<Void> updateKyc(
            @PathVariable String authUserId,
            @RequestParam KycLevel level
    ) {
        complianceService.updateKycLevel(authUserId, level);
        return ResponseEntity.noContent().build();
    }

    // Called by Risk Engine
    @PutMapping("/{authUserId}/risk")
    public ResponseEntity<Void> updateRisk(
            @PathVariable String authUserId,
            @RequestParam RiskState state
    ) {
        complianceService.updateRiskState(authUserId, state);
        return ResponseEntity.noContent().build();
    }
}
