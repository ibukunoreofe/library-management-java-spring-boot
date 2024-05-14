package com.eaproc.tutorials.librarymanagement.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean customHealthCheck = performCustomHealthCheck();
        if (customHealthCheck) {
            return Health.up().withDetail("Custom Health Check", "Service is up and running").build();
        } else {
            return Health.down().withDetail("Custom Health Check", "Service is down").build();
        }
    }

    private boolean performCustomHealthCheck() {
        // Add custom health check logic here
        return true;
    }
}
