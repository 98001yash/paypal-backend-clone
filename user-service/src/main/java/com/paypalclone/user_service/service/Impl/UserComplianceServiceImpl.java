package com.paypalclone.user_service.service.Impl;

import com.paypalclone.user.UserKycUpdatedEvent;
import com.paypalclone.user.UserRiskUpdatedEvent;
import com.paypalclone.user_service.entity.User;
import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;
import com.paypalclone.user_service.exceptions.UserNotFoundException;
import com.paypalclone.user_service.kafka.EventPublisher;
import com.paypalclone.user_service.kafka.KafkaTopics;
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
    private final EventPublisher eventPublisher;

    @Override
    public void updateKycLevel(String externalAuthId, KycLevel newLevel) {

        log.info("KYC update requested: authUserId={}, newLevel={}",
                externalAuthId, newLevel);

        User user = userRepository.findByExternalAuthId(externalAuthId)
                .orElseThrow(() -> {
                    log.warn("User not found for KYC update: authUserId={}", externalAuthId);
                    return new UserNotFoundException(Long.valueOf(externalAuthId));
                });

        KycLevel oldLevel = user.getKycLevel();

        // Idempotency
        if (oldLevel == newLevel) {
            log.info("KYC already at level={}, skipping update for authUserId={}",
                    newLevel, externalAuthId);
            return;
        }

        user.setKycLevel(newLevel);
        userRepository.save(user);

        log.info("KYC updated successfully: authUserId={}, {} -> {}",
                externalAuthId, oldLevel, newLevel);

        com.paypalclone.user.enums.KycLevel eventOldLevel =
                com.paypalclone.user.enums.KycLevel.valueOf(oldLevel.name());

        com.paypalclone.user.enums.KycLevel eventNewLevel =
                com.paypalclone.user.enums.KycLevel.valueOf(newLevel.name());

        UserKycUpdatedEvent event = UserKycUpdatedEvent.builder()
                .eventType("USER_KYC_UPDATED")
                .eventVersion(1)
                .userId(user.getId())                    //  ADD
                .externalAuthId(externalAuthId)
                .oldLevel(eventOldLevel)
                .newLevel(eventNewLevel)
                .build();

        eventPublisher.publish(
                KafkaTopics.USER_KYC_UPDATED,
                user.getId().toString(),
                event
        );
    }

    @Override
    public void updateRiskState(String externalAuthId, RiskState newState) {

        log.info("Risk state update requested: authUserId={}, newState={}",
                externalAuthId, newState);

        User user = userRepository.findByExternalAuthId(externalAuthId)
                .orElseThrow(() -> {
                    log.warn("User not found for risk update: authUserId={}", externalAuthId);
                    return new UserNotFoundException(Long.valueOf(externalAuthId));
                });

        RiskState oldState = user.getRiskState();

        // Idempotency
        if (oldState == newState) {
            log.info("Risk state already={}, skipping update for authUserId={}",
                    newState, externalAuthId);
            return;
        }

        user.setRiskState(newState);
        userRepository.save(user);

        log.info("Risk state updated successfully: authUserId={}, {} -> {}",
                externalAuthId, oldState, newState);

        com.paypalclone.user.enums.RiskState eventOldState =
                com.paypalclone.user.enums.RiskState.valueOf(oldState.name());

        com.paypalclone.user.enums.RiskState eventNewState =
                com.paypalclone.user.enums.RiskState.valueOf(newState.name());


        UserRiskUpdatedEvent event = UserRiskUpdatedEvent.builder()
                .eventType("USER_RISK_UPDATED")
                .eventVersion(1)
                .userId(user.getId())                    //  ADD
                .externalAuthId(externalAuthId)
                .oldState(eventOldState)
                .newState(eventNewState)
                .build();

        eventPublisher.publish(
                KafkaTopics.USER_RISK_UPDATED,
                user.getId().toString(),
                event
        );
    }
}
