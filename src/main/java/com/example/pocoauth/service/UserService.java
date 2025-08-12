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

import com.example.pocoauth.entity.UserEntity;
import com.example.pocoauth.model.CreateUserRequest;
import com.example.pocoauth.model.User;
import com.example.pocoauth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity entity = new UserEntity();
        entity.setId(UUID.randomUUID());
        entity.setName(request.name());
        entity.setEmail(request.email());
        entity.setCreatedAt(Instant.now());
        UserEntity saved = userRepository.save(entity);
        return new User(saved.getId(), saved.getName(), saved.getEmail(), saved.getCreatedAt());
    }

    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll().stream()
                .map(e -> new User(e.getId(), e.getName(), e.getEmail(), e.getCreatedAt()))
                .toList();
    }
}


