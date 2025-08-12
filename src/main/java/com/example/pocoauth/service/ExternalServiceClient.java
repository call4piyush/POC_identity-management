/*
 * Copyright 2025 Piyush Joshi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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


