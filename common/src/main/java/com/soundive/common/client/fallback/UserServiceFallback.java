package com.soundive.common.client.fallback;

import com.soundive.common.dto.UserValidationRequest;
import com.soundive.common.dto.UserValidationResponse;
import com.soundive.common.client.UserServiceFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
public class UserServiceFallback implements UserServiceFeignClient {
    
    @Override
    public ResponseEntity<UserValidationResponse> validateUser(UserValidationRequest request) {
        log.error("Fallback executed for validateUser with email: {}", request.getEmail());
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Error-Source", "circuit-breaker");
        
        return ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .headers(headers)
            .body(new UserValidationResponse("User Service not responding."
                )
            );
    }
}