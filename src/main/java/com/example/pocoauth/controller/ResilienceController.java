package com.example.pocoauth.controller;

import com.example.pocoauth.service.ExternalServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/resilience")
@Tag(name = "resilience", description = "Endpoints demonstrating circuit breaker and fallback")
public class ResilienceController {

    private final ExternalServiceClient externalServiceClient;

    public ResilienceController(ExternalServiceClient externalServiceClient) {
        this.externalServiceClient = externalServiceClient;
    }

    @GetMapping(value = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "External status via circuit breaker", description = "Calls an external service with circuit breaker and returns fallback on failure")
    public Mono<ResponseEntity<Map<String, Object>>> getStatus() {
        return externalServiceClient.fetchStatus().map(ResponseEntity::ok);
    }
}


