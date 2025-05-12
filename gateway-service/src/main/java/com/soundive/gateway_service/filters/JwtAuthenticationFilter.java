package com.soundive.gateway_service.filters;

import com.netflix.discovery.converters.Auto;
import com.soundive.gateway_service.dto.TokenValidationRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class JwtAuthenticationFilter implements GlobalFilter, Ordered {

    @Autowired
    private final RestTemplate restTemplate;

    public JwtAuthenticationFilter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        log.info("Incoming request path: {}", path);

        if (path.startsWith("/auth")) {
            log.info("JWT filter skipped for auth endpoint: {}", path);
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Authorization header: {}", authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);

        // Extragem doar subject-ul din token, fără verificare semnătură (e făcută în auth-service)
        String email = extractEmailWithoutVerification(token);
        if (email == null) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        try {
            TokenValidationRequest request = new TokenValidationRequest(token, email);
            ResponseEntity<Void> response = restTemplate.postForEntity(
                    "http://auth-service/validate-token",
                    request,
                    Void.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

        } catch (Exception e) {
            log.error("Token validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return chain.filter(exchange);
    }

    private String extractEmailWithoutVerification(String token) {
        try {
            return Jwts.parserBuilder()
                    .build()
                    .parseClaimsJwt(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            log.warn("Failed to extract subject: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}