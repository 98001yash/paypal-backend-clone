package com.paypalclone.api_gateway.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtValidator {

    private final Key signingKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    public JwtValidator(@Value("${jwt.secret}") String secret) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims validate(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        if (!issuer.equals(claims.getIssuer())) {
            throw new RuntimeException("Invalid issuer");
        }

        if (!audience.equals(claims.getAudience())) {
            throw new RuntimeException("Invalid audience");
        }

        if (!"ACCESS".equals(claims.get("token_type"))) {
            throw new RuntimeException("Invalid token type");
        }

        return claims;
    }
}
