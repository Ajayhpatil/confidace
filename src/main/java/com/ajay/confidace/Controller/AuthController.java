package com.ajay.confidace.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.ajay.confidace.security.AuthRequest;
import com.ajay.confidace.security.JwtUtil;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
@RestController // Marks this as REST controller
public class AuthController {

  /*
   field injection
   @Autowired
    private AuthenticationManager authManager; // Handles authentication*/

    //constructor injection
    private final AuthenticationManager authManager;


    public AuthController(AuthenticationManager authManager) {
        this.authManager = authManager;
    }
    @Autowired
    private JwtUtil jwtUtil; // Token utility

    // Login API
    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request) {

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // Authenticate user (username + password check)
       /* authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), // username from request
                        request.getPassword()  // password from request
                )
        );
*/
        List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // If authentication successful → generate token
        return jwtUtil.generateToken(request.getUsername(),roles);
    }
}
