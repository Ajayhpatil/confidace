package com.ajay.confidace.security;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration // Configuration class
@EnableWebSecurity // Enable Spring Security
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter; // Inject JWT filter

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll() // Allow login without token
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/Video/download/**").hasRole("USER")
                        .requestMatchers("/Video/stream/**").hasRole("ADMIN")

                        .anyRequest().permitAll()
                          //  .anyRequest().authenticated() // All others need authentication
                ).httpBasic(Customizer.withDefaults());
                /*.sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No session
                );

        // Add JWT filter before default authentication filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);*/

        return http.build(); // Build config
    }

    // Expose AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

/*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

}
