package com.paypalclone.api_gateway.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtValidator {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

    public Claims validate(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(secret)
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
