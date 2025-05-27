package com.allert;

import java.util.*;

/**
 * Interface representing a network of services with dependencies.
 * Each service can depend on other services, and alerts can propagate through these dependencies.
 */
public interface AlertNetwork {
    /**
     * Adds a service to the network.
     * @param serviceName name of the service to add
     * @throws IllegalArgumentException if service name is null or empty
     */
    void addService(String serviceName);

    /**
     * Adds a dependency between two services.
     * @param serviceName name of the service that depends on another service
     * @param dependencyName name of the service that is being depended upon
     * @throws IllegalArgumentException if either service name is null or empty,
     *         or if either service doesn't exist in the network
     */
    void addDependency(String serviceName, String dependencyName);

    /**
     * Gets all dependencies of a service.
     * @param serviceName name of the service
     * @return set of service names that the given service depends on
     * @throws IllegalArgumentException if service name is null or empty,
     *         or if service doesn't exist in the network
     */
    Set<String> getDependencies(String serviceName);

    /**
     * Finds the shortest path of alert propagation from source to target service.
     * @param sourceService name of the service where the alert originates
     * @param targetService name of the service to which the alert should propagate
     * @return list of service names representing the path of propagation,
     *         or empty list if no path exists
     * @throws IllegalArgumentException if either service name is null or empty,
     *         or if either service doesn't exist in the network
     */
    List<String> findAlertPropagationPath(String sourceService, String targetService);

    /**
     * Gets all services that would be affected by an alert in the given service.
     * @param serviceName name of the service where the alert originates
     * @return set of service names that would be affected by the alert
     * @throws IllegalArgumentException if service name is null or empty,
     *         or if service doesn't exist in the network
     */
    Set<String> getAffectedServices(String serviceName);

    /**
     * Suggests edges that could be added to contain an alert within a specific set of services.
     * @param services set of service names that should contain the alert
     * @return set of pairs of service names representing suggested containment edges
     * @throws IllegalArgumentException if services set is null or empty,
     *         or if any service doesn't exist in the network
     */
    Set<AlertPropagation> suggestContainmentEdges(Set<String> services);

    /**
     * Reconstructs the ordered list of services based on their dependencies.
     * @return list of service names in order of their dependencies
     * @throws IllegalStateException if there are circular dependencies in the network
     */
    List<String> reconstructOrder();
} 
