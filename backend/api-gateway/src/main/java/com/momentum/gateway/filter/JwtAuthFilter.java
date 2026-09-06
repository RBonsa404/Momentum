package com.momentum.gateway.filter;

import com.momentum.common.tracing.CorrelationIds;
import com.momentum.gateway.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    private static final List<String> PUBLIC_PREFIXES = List.of(
            "/api/auth/login",
            "/api/auth/refresh",
            "/actuator"
    );

    private final JwtProperties jwtProperties;

    public JwtAuthFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return chain.filter(exchange);
        }
        String path = request.getURI().getPath();
        if (PUBLIC_PREFIXES.stream().anyMatch(path::startsWith)) {
            return chain.filter(withCorrelation(exchange));
        }
        String auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(auth.substring(7))
                    .getPayload();
            String userId = claims.getSubject();
            UUID.fromString(userId);
            ServerWebExchange mutated = withCorrelation(exchange).mutate()
                    .request(builder -> builder.header(CorrelationIds.USER_HEADER, userId))
                    .build();
            return chain.filter(mutated);
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private ServerWebExchange withCorrelation(ServerWebExchange exchange) {
        String existing = exchange.getRequest().getHeaders().getFirst(CorrelationIds.HTTP_HEADER);
        String cid = existing == null || existing.isBlank() ? UUID.randomUUID().toString() : existing;
        return exchange.mutate()
                .request(builder -> builder.header(CorrelationIds.HTTP_HEADER, cid))
                .build();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
