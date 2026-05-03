package com.ajay.confidace.security;

import io.jsonwebtoken.*; // JWT library
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

// Create the token

// Validate token

// Check if token expired
@Component // Make this class available as Spring Bean
public class JwtUtil {

    private String secret = "mysecretkey"; // Secret key used to sign token

    // Generate token using username
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder() // Start building JWT
                .setSubject(username) // Store username inside token
                .claim("roles", roles)   // 👈 IMPORTANT
                .setIssuedAt(new Date()) // Token creation time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 )) // Expiry = 1 hour
                .signWith(SignatureAlgorithm.HS256, secret) // Sign with secret key
                .compact(); // Convert to final string token
    }



    // Extract username from token
    public String extractUsername(String token) {
        return getClaims(token).getSubject(); // Read subject (username)
    }


   /* public boolean isTokenValid(String token, String username) {
        return username.equals(extractUsername(token)) && !isExpired(token); // Check username + expiry
    }*/

    // Validate token
    public boolean isTokenValid(String token) {
        try {
            // If this doesn't throw exception → token is valid
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
    // Check if token expired
    private boolean isExpired(String token) {

        return getClaims(token)
                .getExpiration()
                .before(new Date());// Compare expiry with current time
    }

    // Decode token and get all claims
    private Claims getClaims(String token) {


        return Jwts.parser()
                .setSigningKey(secret) // Verify signature using secret
                .parseClaimsJws(token) // Parse token
                .getBody(); // Get data inside token
    }

    public List<SimpleGrantedAuthority> extractRoles(String token) {

        Claims claims = getClaims(token);

        List<String> roles = (List<String>) claims.get("roles");

        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

}

