package com.paypalclone.auth_service.unit.service;


import com.paypalclone.auth_service.repository.RefreshTokenRepository;
import com.paypalclone.auth_service.service.RefreshTokenService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.User;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void shouldCreateRefreshTokenSuccessfully() {
        // GIVEN
        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");

        when(refreshTokenRepository.save(any(RefreshToken.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Instant beforeCreation = Instant.now();

        // WHEN
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // THEN
        assertThat(refreshToken).isNotNull();
        assertThat(refreshToken.getToken()).isNotBlank();
        assertThat(refreshToken.getUser()).isEqualTo(user);
        assertThat(refreshToken.getExpiryDate())
                .isAfterOrEqualTo(beforeCreation);

        // VERIFY
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

}
