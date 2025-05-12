package com.soundive.common.client;

import com.soundive.common.client.fallback.UserServiceFallback;
import com.soundive.common.config.FeignClientConfig;
import com.soundive.common.dto.UserValidationRequest;
import com.soundive.common.dto.UserValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;

@FeignClient(
    name = "user-service",
    path = "/users",
    configuration = FeignClientConfig.class,
    fallback = UserServiceFallback.class

)
public interface UserServiceFeignClient {

    @PostMapping("/validate")
    ResponseEntity<UserValidationResponse> validateUser(@RequestBody UserValidationRequest request);
}