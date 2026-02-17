package com.paypalclone.auth_service.service;


import com.paypalclone.auth_service.dtos.LoginRequest;
import com.paypalclone.auth_service.dtos.TokenResponse;
import com.paypalclone.auth_service.entity.RefreshToken;
import com.paypalclone.auth_service.entity.Role;
import com.paypalclone.auth_service.entity.User;
import com.paypalclone.auth_service.exceptions.InvalidCredentialsException;
import com.paypalclone.auth_service.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;

    public TokenResponse login(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch (JwtException ex) {
            log.warn("Invalid login attempt for {}", request.getEmail());
            throw new InvalidCredentialsException();
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        String accessToken = jwtService.generateAccessToken(
                user.getEmail(),
                Map.of("roles",
                        user.getRoles().stream()
                                .map(Role::getName)
                                .toList()
                )
        );

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        log.info("Login successful for {}", request.getEmail());

        return new TokenResponse(accessToken, refreshToken.getToken());
    }

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

    public void logout(String refreshTokenValue) {
        log.info("Logout requested");
        RefreshToken token =
                refreshTokenService.verifyRefreshToken(refreshTokenValue);
        token.setRevoked(true);
    }
}
