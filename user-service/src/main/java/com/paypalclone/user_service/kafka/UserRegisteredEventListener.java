package com.paypalclone.user_service.kafka;


import com.paypalclone.auth.UserRegisteredEvent;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {


    private final UserRepository userRepository;

    @KafkaListener(
            topics = "auth.user.registered",
            groupId = "user-service"
    )
    public void handle(UserRegisteredEvent event) {

        log.info("Received UserRegisteredEvent for authUserId={}", event.getUserId());

        //  Idempotency check (CRITICAL)
        if (userRepository.existsByExternalAuthId(event.getUserId().toString())) {
            log.warn("User already exists for authUserId={}, ignoring event", event.getUserId());
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
        log.info("User created in User Service for authUserId={}", event.getUserId());
    }
}
