package com.paypalclone.user_service.repository;

import com.paypalclone.user_service.entity.User;
import com.paypalclone.user_service.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByExternalAuthId(String externalAuthId);

    Optional<User> findByEmail(String email);

    boolean existsByExternalAuthId(String externalAuthId);

    boolean existsByEmail(String email);

    long countByStatus(UserStatus status);
}
