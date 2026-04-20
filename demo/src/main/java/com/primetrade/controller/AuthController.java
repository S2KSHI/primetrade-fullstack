package com.primetrade.controller;

import com.primetrade.model.User;
import com.primetrade.repository.UserRepository;
import com.primetrade.security.JwtUtil;
import com.primetrade.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            System.out.println("📝 Registration attempt for: " + request.getEmail());
            
            if (userRepository.existsByEmail(request.getEmail())) {
                return ResponseEntity.status(400).body("Email already exists");
            }
            
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            
            // Password encode karke save karo
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            user.setPassword(encodedPassword);
            user.setRole("USER");
            
            userRepository.save(user);
            
            System.out.println("✅ User registered successfully!");
            return ResponseEntity.ok("Registration successful! Please login.");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            System.out.println("🔑 Login attempt for: " + request.getEmail());
            
            // User find karo
            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            
            if (user == null) {
                System.out.println("❌ User not found!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            
            System.out.println("👤 User found: " + user.getEmail());
            System.out.println("🔐 Checking password...");
            
            // Password match karo
            boolean passwordMatches = passwordEncoder.matches(request.getPassword(), user.getPassword());
            
            if (!passwordMatches) {
                System.out.println("❌ Password does not match!");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
            }
            
            System.out.println("✅ Password matched! Generating token...");
            
            // Token generate karo
            String token = jwtUtil.generateToken(user.getEmail());
            
            System.out.println("🎫 Token generated successfully!");
            
            return ResponseEntity.ok(new AuthResponse("Login successful", token));
            
        } catch (Exception e) {
            System.out.println("💥 Error during login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Server error: " + e.getMessage());
        }
    }
}