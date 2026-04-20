package com.primetrade.repository;

import com.primetrade.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    // Ye do methods missing the, isliye error aa raha tha
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
}