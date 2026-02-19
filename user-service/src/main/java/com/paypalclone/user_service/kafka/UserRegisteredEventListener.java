package com.paypalclone.user_service.kafka;

import com.paypalclone.auth.UserRegisteredEvent;
import com.paypalclone.user.UserCreatedEvent;
import com.paypalclone.user_service.entity.User;
import com.paypalclone.user_service.enums.KycLevel;
import com.paypalclone.user_service.enums.RiskState;
import com.paypalclone.user_service.enums.UserStatus;
import com.paypalclone.user_service.enums.UserType;
import com.paypalclone.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRegisteredEventListener {

    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @KafkaListener(
            topics = "auth.user.registered",
            groupId = "user-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    @Transactional
    public void handle(UserRegisteredEvent event) {

        if (userRepository.existsByExternalAuthId(event.getUserId().toString())) {
            return;
        }

        User user = new User();
        user.setExternalAuthId(event.getUserId().toString());
        user.setEmail(event.getEmail());
        user.setUserType(UserType.PERSONAL);
        user.setStatus(UserStatus.ACTIVE);
        user.setKycLevel(KycLevel.NONE);
        user.setRiskState(RiskState.NORMAL);

        userRepository.save(user);

        log.info(
                "User created in user-service: authId={}, internalUserId={}",
                event.getUserId(),
                user.getId()
        );

        // 🔥 PUBLISH UserCreatedEvent
        UserCreatedEvent createdEvent =
                UserCreatedEvent.builder()
                        .eventType("USER_CREATED")
                        .eventVersion(1)
                        .userId(user.getId())
                        .externalAuthId(user.getExternalAuthId())
                        .build();

        eventPublisher.publish(
                "user.user.created",
                user.getId().toString(),
                createdEvent
        );
    }
}
