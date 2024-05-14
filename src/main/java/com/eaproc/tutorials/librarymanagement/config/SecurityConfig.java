package com.eaproc.tutorials.librarymanagement.config;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomAuthenticationProvider;
import com.eaproc.tutorials.librarymanagement.filter.JwtAuthenticationFilter;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService UserService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final CustomAuthenticationProvider customAuthenticationProvider;

    public SecurityConfig(UserService UserService, JwtAuthenticationFilter jwtAuthenticationFilter, CustomAuthenticationProvider customAuthenticationProvider) {
        this.UserService = UserService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customAuthenticationProvider = customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for simplicity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/actuator/health", "/api/auth/login").permitAll() // Allow public access to health, info, and authentication endpoints
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session (no sessions)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .authenticationProvider(customAuthenticationProvider)
                .userDetailsService(UserService);
        return authenticationManagerBuilder.build();
    }
}
