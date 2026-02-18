package com.paypalclone.api_gateway.filter;

import com.paypalclone.api_gateway.Jwt.JwtValidator;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
public class AuthenticationFilter
        extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final JwtValidator jwtValidator;

    public AuthenticationFilter(JwtValidator jwtValidator) {
        super(Config.class);
        this.jwtValidator = jwtValidator;
    }

    @Override
    public GatewayFilter apply(Config config) {

        return (exchange, chain) -> {

            log.debug("[GATEWAY-AUTH] Incoming request: {} {}",
                    exchange.getRequest().getMethod(),
                    exchange.getRequest().getURI());

            String authHeader = exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null) {
                log.warn("[GATEWAY-AUTH] Missing Authorization header");
                return unauthorized(exchange);
            }

            if (!authHeader.startsWith("Bearer ")) {
                log.warn("[GATEWAY-AUTH] Invalid Authorization header format");
                return unauthorized(exchange);
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = jwtValidator.validate(token);

                log.info("[GATEWAY-AUTH] JWT validated successfully: sub={}, tokenType={}, scopes={}",
                        claims.getSubject(),
                        claims.get("token_type"),
                        claims.get("scope"));

                ServerHttpRequest request = exchange.getRequest()
                        .mutate()
                        .headers(headers -> {
                            headers.set("X-User-Id", claims.getSubject());
                            headers.set("X-Scopes",
                                    String.join(",", claims.get("scope", List.class)));
                            headers.set("X-User-Type",
                                    claims.get("user_type", String.class));
                        })
                        .build();

                log.debug("[GATEWAY-AUTH] Injecting headers: X-User-Id={}, X-Scopes={}",
                        claims.getSubject(),
                        claims.get("scope"));

                return chain.filter(exchange.mutate().request(request).build());

            } catch (Exception ex) {
                log.error("[GATEWAY-AUTH] Authentication failed: {}", ex.getMessage());
                return unauthorized(exchange);
            }
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

    public static class Config {
        // reserved for future use
    }
}
