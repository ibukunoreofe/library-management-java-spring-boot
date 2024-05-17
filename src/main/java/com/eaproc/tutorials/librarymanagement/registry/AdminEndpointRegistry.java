package com.eaproc.tutorials.librarymanagement.registry;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class AdminEndpointRegistry {

    private final Set<String> adminEndpoints;

    public AdminEndpointRegistry() {
        adminEndpoints = new HashSet<>();
    }

    public void registerAdminEndpoint(String endpoint) {
        adminEndpoints.add(endpoint);
    }

    public boolean isAdminEndpoint(String endpoint) {
        return adminEndpoints.contains(endpoint);
    }
}
