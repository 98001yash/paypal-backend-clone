package com.paypalclone.auth_service.service;


import com.paypalclone.auth.UserRegisteredEvent;
import com.paypalclone.auth_service.entity.Role;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.exceptions.UserAlreadyExistsException;
import com.paypalclone.auth_service.kafka.EventPublisher;
import com.paypalclone.auth_service.kafka.KafkaTopics;
import com.paypalclone.auth_service.repository.RoleRepository;
import com.paypalclone.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;


    @Transactional
    public User register(String email, String rawPassword){
        log.info("Registering new user: {}",email);

        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException(email);
        }

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(()-> new IllegalStateException("Default role USER  not found"));

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.getRoles().add(userRole);

        User savedUser = userRepository.save(user);

        UserRegisteredEvent event =
                UserRegisteredEvent.builder()
                        .eventType("USER_REGISTERED")
                        .eventVersion(1)
                        .userId(savedUser.getId())
                        .email(savedUser.getEmail())
                        .build();

        eventPublisher.publish(
                KafkaTopics.USER_REGISTERED,
                savedUser.getId().toString(),
                event
        );

        log.info("User registered successfully & event published: {}", email);

        return savedUser;
    }
}
