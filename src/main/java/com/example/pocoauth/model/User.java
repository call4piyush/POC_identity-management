package com.example.pocoauth.model;

import java.time.Instant;
import java.util.UUID;

public record User(
        UUID id,
        String name,
        String email,
        Instant createdAt
) {}


