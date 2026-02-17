package com.paypalclone.auth_service.unit.service;

import com.paypalclone.auth_service.dtos.LoginRequest;
import com.paypalclone.auth_service.dtos.TokenResponse;
import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.Role;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.exceptions.InvalidCredentialsException;
import com.paypalclone.auth_service.kafka.EventPublisher;
import com.paypalclone.auth_service.kafka.KafkaTopics;
import com.paypalclone.auth_service.repository.UserRepository;
import com.paypalclone.auth_service.service.AuthService;
import com.paypalclone.auth_service.service.JwtService;
import com.paypalclone.auth_service.service.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully_andPublishKafkaEvent() {

        LoginRequest request = new LoginRequest(
                "user@test.com",
                "password123"
        );

        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setEmail("user@test.com");
        user.setRoles(Set.of(role));

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token-123");

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(eq(user.getEmail()), anyMap()))
                .thenReturn("access-token-123");

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(
                        "user@test.com",
                        null,
                        Set.of()
                ));

        TokenResponse response = authService.login(request);


        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("access-token-123");
        assertThat(response.getRefreshToken()).isEqualTo("refresh-token-123");

        // ---------- VERIFY ----------
        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository).findByEmail(request.getEmail());
        verify(jwtService).generateAccessToken(eq(user.getEmail()), anyMap());
        verify(refreshTokenService).createRefreshToken(user);

        verify(eventPublisher).publish(
                eq(KafkaTopics.USER_LOGGED_IN),
                eq(user.getId().toString()),
                any()
        );
    }


    @Test
    void shouldFailLogin_whenAuthenticationFails_andNotPublishKafkaEvent() {
        // ---------- GIVEN ----------
        LoginRequest request = new LoginRequest(
                "user@test.com",
                "wrong-password"
        );

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // ---------- WHEN + THEN ----------
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        // ---------- VERIFY ----------
        verify(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        verify(userRepository, never()).findByEmail(anyString());
        verify(jwtService, never()).generateAccessToken(anyString(), anyMap());
        verify(refreshTokenService, never()).createRefreshToken(any());

        verify(eventPublisher, never()).publish(any(), any(), any());
    }


}
