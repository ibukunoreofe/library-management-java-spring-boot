package com.eaproc.tutorials.librarymanagement.config;

import com.eaproc.tutorials.librarymanagement.config.providers.CustomAuthenticationProvider;
import com.eaproc.tutorials.librarymanagement.filter.CustomAppFilterManager;
import com.eaproc.tutorials.librarymanagement.service.UserService;
import com.eaproc.tutorials.librarymanagement.web.exception.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;
    private final CustomAppFilterManager customAppFilterManager;
    private final CustomAuthenticationProvider customAuthenticationProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(UserService userService, CustomAppFilterManager customAppFilterManager, CustomAuthenticationProvider customAuthenticationProvider, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.userService = userService;
        this.customAppFilterManager = customAppFilterManager;
        this.customAuthenticationProvider = customAuthenticationProvider;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF protection for simplicity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/actuator/health", "/api/auth/login", "/api/auth/register").permitAll() // Allow public access to specific endpoints
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler) // Use custom access denied handler
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Use stateless session (no sessions)
                .addFilterBefore(customAppFilterManager, UsernamePasswordAuthenticationFilter.class); // Add main filter before UsernamePasswordAuthenticationFilter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .authenticationProvider(customAuthenticationProvider)
                .userDetailsService(userService);
        return authenticationManagerBuilder.build();
    }
}
