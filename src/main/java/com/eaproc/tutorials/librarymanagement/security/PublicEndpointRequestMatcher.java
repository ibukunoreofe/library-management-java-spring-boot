package com.eaproc.tutorials.librarymanagement.security;

import com.eaproc.tutorials.librarymanagement.registry.PublicEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

@Component
public class PublicEndpointRequestMatcher implements RequestMatcher {


    private final PublicEndpointRegistry publicEndpointRegistry;

    @Autowired
    public PublicEndpointRequestMatcher(PublicEndpointRegistry publicEndpointRegistry) {
        this.publicEndpointRegistry = publicEndpointRegistry;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        String path = request.getRequestURI();
        return publicEndpointRegistry.getPublicEndpoints().contains(path);
    }
}
