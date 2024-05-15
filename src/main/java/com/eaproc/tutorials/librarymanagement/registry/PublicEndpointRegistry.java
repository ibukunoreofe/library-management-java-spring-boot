package com.eaproc.tutorials.librarymanagement.registry;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class PublicEndpointRegistry {

    private final Set<String> publicEndpoints;

    public PublicEndpointRegistry() {
        publicEndpoints = new HashSet<>();

        // Add others unreachable
        publicEndpoints.add( "/actuator/health");
        publicEndpoints.add( "/error");
    }

    public void addPublicEndpoint(String endpoint) {
        publicEndpoints.add(endpoint);
    }

    public Set<String> getPublicEndpoints() {
        return new HashSet<>(publicEndpoints);
    }
}
