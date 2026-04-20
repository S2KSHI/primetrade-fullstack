package com.primetrade.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    
    // 🔥 FIXED: Using a proper 512-bit key for HS512
    private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private long expiration = 86400000; // 24 hours
    
    public String generateToken(String email) {
        System.out.println("🎫 Generating token for: " + email);
        
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }
    
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Token validation failed: " + e.getMessage());
            return false;
        }
    }
}