package com.ajay.confidace.security;


import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//Check the token
@Component // Make it Spring Bean
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Token utility

    @Autowired
    private CustomUserDetailsService userDetailsService; // Load user


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        System.out.println("3");


        // Get Authorization header
        String header = request.getHeader("Authorization");

        // Check if header is present and starts with Bearer
try{

        if (header != null && header.startsWith("Bearer ")) {

            // Extract token by removing "Bearer "
            String token = header.substring(7);


            // Validate token
            if (jwtUtil.isTokenValid(token)) {

                // Extract username from token
                String username = jwtUtil.extractUsername(token);

                //Extract roles
                var authorities = jwtUtil.extractRoles(token);

                System.out.println("4");
                // Create authentication object
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(
                                username, null,authorities );

                // Set authentication in Spring context
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        System.out.println("5");

        // Continue filter chain (very important)
        chain.doFilter(request, response);


    }catch (io.jsonwebtoken.ExpiredJwtException e) {

            response.setStatus(401);
            response.getWriter().write("TOKEN_EXPIRED");

        }catch (Exception e) {

            response.setStatus(401);
            response.getWriter().write("INVALID_TOKEN");
        }
    }
}



