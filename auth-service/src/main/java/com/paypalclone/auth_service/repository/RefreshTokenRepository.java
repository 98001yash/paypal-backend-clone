package com.paypalclone.auth_service.repository;

import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


    Optional<RefreshToken>  findByToken(String token);
    void deleteByUser(User user);
    void deleteByExpiryDateBefore(Instant now);
}
