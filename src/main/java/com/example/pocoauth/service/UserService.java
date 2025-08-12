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


