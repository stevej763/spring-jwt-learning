package com.example.springsecurityjwttutorial.repository;

import com.example.springsecurityjwttutorial.authentication.PersistedUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<PersistedUser, UUID> {
    PersistedUser findByUserName(String username);
}