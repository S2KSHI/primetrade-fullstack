package com.primetrade.service;

import com.primetrade.model.User;
import com.primetrade.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register Logic - Password Encode karke save karega
    public User register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        // Yahan ho rahi hai asali magic (Encoding)
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        
        // Default role set kar dete hain
        if (user.getRole() == null) {
            user.setRole("USER");
        }
        
        return userRepository.save(user);
    }

    // Login Logic helper
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Password verification logic
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}