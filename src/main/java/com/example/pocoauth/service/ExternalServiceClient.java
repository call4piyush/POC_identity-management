package com.example.pocoauth.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class ExternalServiceClient {

    private final WebClient webClient;
    private final String externalBaseUrl;

    public ExternalServiceClient(WebClient webClient,
                                 @Value("${external.service.base-url:https://httpbin.org}") String externalBaseUrl) {
        this.webClient = webClient;
        this.externalBaseUrl = externalBaseUrl;
    }

    @CircuitBreaker(name = "externalService", fallbackMethod = "fallbackStatus")
    public Mono<Map<String, Object>> fetchStatus() {
        return webClient.get()
                .uri(externalBaseUrl + "/status/200")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .timeout(Duration.ofSeconds(2))
                .map(body -> Map.of("status", "ok", "body", body));
    }

    private Mono<Map<String, Object>> fallbackStatus(Throwable throwable) {
        return Mono.just(Map.of(
                "status", "fallback",
                "message", "external service unavailable"
        ));
    }
}


