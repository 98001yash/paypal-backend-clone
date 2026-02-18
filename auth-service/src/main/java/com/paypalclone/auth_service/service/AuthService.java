package com.paypalclone.auth_service.service;


import com.paypalclone.auth.UserLoggedInEvent;
import com.paypalclone.auth_service.dtos.LoginRequest;
import com.paypalclone.auth_service.dtos.TokenResponse;
import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.Role;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.exceptions.InvalidCredentialsException;
import com.paypalclone.auth_service.kafka.EventPublisher;
import com.paypalclone.auth_service.kafka.KafkaTopics;
import com.paypalclone.auth_service.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public TokenResponse login(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        // 1️⃣ Authenticate credentials
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (AuthenticationException ex) {
            log.warn("Invalid login attempt for email={}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        // 2️⃣ Load user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        // 3️⃣ Build ACCESS token claims (CRITICAL)
        Map<String, Object> claims = new java.util.HashMap<>();

        claims.put("token_type", "ACCESS"); // REQUIRED by Gateway

        claims.put(
                "scope",
                user.getRoles().stream()
                        .map(Role::getName)
                        .toList()
        );

        claims.put("email", user.getEmail()); // optional, useful

        // 4️⃣ Generate ACCESS token
        String accessToken = jwtService.generateAccessToken(
                String.valueOf(user.getId()),   // ✅ SUBJECT = USER ID
                claims
        );

        // 5️⃣ Create refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // 6️⃣ Publish LOGIN event
        UserLoggedInEvent event =
                UserLoggedInEvent.builder()
                        .eventType("USER_LOGGED_IN")
                        .eventVersion(1)
                        .userId(user.getId())
                        .email(user.getEmail())
                        .build();

        eventPublisher.publish(
                KafkaTopics.USER_LOGGED_IN,
                user.getId().toString(),
                event
        );

        log.info("Login successful for userId={}, email={}", user.getId(), user.getEmail());

        // 7️⃣ Return tokens
        return new TokenResponse(
                accessToken,
                refreshToken.getToken()
        );
    }


    @Transactional
    public TokenResponse refresh(String refreshTokenValue) {

        log.info("Refreshing token");

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(refreshTokenValue);

        User user = refreshToken.getUser();

        String newAccessToken = jwtService.generateAccessToken(
                user.getEmail(),
                Map.of("roles",
                        user.getRoles().stream()
                                .map(Role::getName)
                                .toList()
                )
        );

        return new TokenResponse(newAccessToken, refreshTokenValue);
    }

    @Transactional
    public void logout(String refreshTokenValue) {
        log.info("Logout requested");
        RefreshToken token =
                refreshTokenService.verifyRefreshToken(refreshTokenValue);
        token.setRevoked(true);
    }
}
