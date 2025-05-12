package com.soundive.gateway_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping(value = "/auth", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> authFallback() {
        return ResponseEntity.status(503).body("Auth Service temporarily unavailable");
    }

    @RequestMapping(value = "/user", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> userFallback() {
        return ResponseEntity.status(503).body("User Service temporarily unavailable");
    }
}