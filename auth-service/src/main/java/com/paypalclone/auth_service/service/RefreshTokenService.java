package com.paypalclone.auth_service.service;


import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-token-validity-ms}")
    private long refreshTokenValidityMs;

    public RefreshToken createRefreshToken(User user) {
        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(Instant.now().plusMillis(refreshTokenValidityMs));
        token.setRevoked(false);
        return refreshTokenRepository.save(token);
    }

    public RefreshToken verifyRefreshToken(String tokenValue) {
        RefreshToken token = refreshTokenRepository.findByToken(tokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.isRevoked() || token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired or revoked");
        }
        return token;
    }

    public void revokeTokens(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}