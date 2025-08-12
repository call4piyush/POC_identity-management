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
package com.example.pocoauth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/public")
@Tag(name = "public", description = "Public endpoints that do not require authentication")
public class PublicController {

    @GetMapping("/success")
    @Operation(summary = "Public success", description = "Health-like public endpoint")
    public ResponseEntity<Map<String, String>> success() {
        return ResponseEntity.ok(Map.of("status", "success", "message", "Public success endpoint is reachable"));
    }

    @GetMapping("/fallback")
    @Operation(summary = "Public fallback", description = "Demonstration fallback response")
    public ResponseEntity<Map<String, String>> fallback() {
        return ResponseEntity.ok(Map.of("status", "fallback", "message", "This is the fallback response"));
    }
}


