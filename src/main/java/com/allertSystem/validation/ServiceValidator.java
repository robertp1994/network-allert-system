package com.allertSystem.validation;

import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Set;

/**
 * Utility class for validating service-related operations.
 */
@RequiredArgsConstructor
public class ServiceValidator {
    private final Map<String, Set<String>> adjacencyList;

    public void validateServiceName(String serviceName) {
        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new IllegalArgumentException("Service name cannot be null or empty");
        }
    }

    public void validateServiceExists(String serviceName) {
        if (!adjacencyList.containsKey(serviceName)) {
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