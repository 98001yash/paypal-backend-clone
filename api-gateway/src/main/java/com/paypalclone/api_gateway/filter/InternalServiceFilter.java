package com.paypalclone.api_gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class InternalServiceFilter
        extends AbstractGatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            String serviceHeader =
                    exchange.getRequest()
                            .getHeaders()
                            .getFirst("X-Internal-Service");

            if (!"KYC-SERVICE".equals(serviceHeader)
                    && !"RISK-SERVICE".equals(serviceHeader)) {

                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }
}
