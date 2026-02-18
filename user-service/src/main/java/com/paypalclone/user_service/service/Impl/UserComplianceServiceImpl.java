package com.paypalclone.user_service.service.Impl;


import com.paypalclone.user_service.entity.User;
import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;
import com.paypalclone.user_service.exceptions.UserNotFoundException;
import com.paypalclone.user_service.repository.UserRepository;
import com.paypalclone.user_service.service.UserComplianceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserComplianceServiceImpl implements UserComplianceService {


    private final UserRepository userRepository;


    @Override
    public void updateKycLevel(String externalAuthId, KycLevel newLevel) {

        log.info("KYC update requested: authUserId={}, newLevel={}",
                externalAuthId, newLevel);

        User user = userRepository.findByExternalAuthId(externalAuthId)
                .orElseThrow(()-> {
                    log.warn("User not found for KYC update: authUserId={}",externalAuthId);
                    return new UserNotFoundException(Long.valueOf(externalAuthId));
                });

        // idemptoency
        if(user.getKycLevel()== newLevel){
            log.info("Kyc alreeady at level={}, skipping update for authUserId={}",
                    newLevel,externalAuthId);
            return;
        }

        user.setKycLevel(newLevel);
        userRepository.save(user);

        log.info("Kyc updated successfully: authUserId={}, level={}",externalAuthId, newLevel);

        // TODO  later-> emit UserKycUpdatedEvent
    }

    @Override
    public void updateRiskState(String externalAuthId, RiskState newState) {


        log.info("Risk state update  requested: authUserId={}, newState={}",
                externalAuthId, newState);

        User user = userRepository.findByExternalAuthId(externalAuthId)
                .orElseThrow(()-> {
                    log.warn("User not found for risk update: authUserId={}",externalAuthId);
                    throw new UserNotFoundException(Long.valueOf(externalAuthId));
                });

        // Idempotency
        if(user.getRiskState()== newState){
            log.info("Risk state already={}, skipping update for authUserId={}",
                    newState, externalAuthId);
            return;
        }

        user.setRiskState(newState);
        userRepository.save(user);

        log.info("Risk state updated successfully: authUserId={}, state={}",
                externalAuthId, newState);


        // TODO  -> emit UserRiskUpdatedEvent
    }
}
