package com.ajay.confidace.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service // Marks this as service layer bean
public class CustomUserDetailsService implements UserDetailsService {

    // Spring calls this during authentication


    @Autowired
    private UserRepository userRepository;

    /*   @Autowired
       private PasswordEncoder passwordEncoder;*/
    @Override
    public UserDetails loadUserByUsername(String username) {


/*
        // 🔥 STEP 1: FETCH USER FROM DB
        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found: " + username));

        // 🔥 STEP 2: CONVERT ROLES STRING → AUTHORITIES
        List<SimpleGrantedAuthority> authorities =
                Arrays.stream(appUser.getRoles().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .toList();

        // 🔥 STEP 3: RETURN SPRING SECURITY USER
        return new org.springframework.security.core.userdetail s.User(
                appUser.getUsername(),
                appUser.getPassword(),
                authorities
        );*/

        if (username.equals("ajay")) {
            return User.withUsername("ajay")
                    .password("{noop}1234") // ✅ simple fix
                    .roles("USER")
                    .build();
        }

        if (username.equals("admin")) {
            return User.withUsername("admin")
                    .password("{noop}1234") // ✅ simple fix
                    .roles("ADMIN")
                    .build();
        }

        return User.withUsername("HOD")
                .password("{noop}1234") // ✅ simple fix
                .roles("HOD")
                .build();

    }

}

