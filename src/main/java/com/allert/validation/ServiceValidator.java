package com.allert.validation;

import lombok.*;

import java.util.*;

/**
 * Utility class for validating service-related operations.
 */
@RequiredArgsConstructor
public class ServiceValidator {
    private final Map<String, Set<String>> serviceDependencies;

    public void validateServiceName(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
    }

    public void validateServiceExists(String serviceName) {
        if (!serviceDependencies.containsKey(serviceName)) {
            throw new IllegalArgumentException("Service " + serviceName + " does not exist in the network");
        }
    }

    public void validateServicesSet(Set<String> services) {
        if (services == null || services.isEmpty()) {
            throw new IllegalArgumentException("Services set cannot be null or empty");
        }
        
        for (String service : services) {
            validateServiceExists(service);
        }
    }
} 
