package com.allert;

import javafx.util.*;

import java.util.*;

/**
 * Interface representing a network of services with dependencies.
 * Each service can depend on other services, and alerts can propagate through these dependencies.
 */
public interface AlertNetwork {
    /**
     * Adds a service to the network.
     * @param service name of the service to add
     */
    void addService(String service);

    /**
     * Adds a directed dependency between two services.
     * @param fromService name of the service that depends on another service
     * @param toService name of the service that is being depended upon
     */
    void addDependency(String fromService, String toService);

    /**
     * Gets all dependencies of a service.
     * @param service name of the service
     * @return list of service names that the given service depends on
     */
    List<String> getDependencies(String service);

    /**
     * Finds the shortest path of alert propagation from source to target service.
     * @param source name of the service where the alert originates
     * @param target name of the service to which the alert should propagate
     * @return list of service names representing the path of propagation,
     *         or empty list if no path exists
     */
    List<String> findAlertPropagationPath(String source, String target);

    /**
     * Gets all services that would be affected by an alert in the given service.
     * @param source name of the service where the alert originates
     * @return list of service names that would be affected by the alert
     */
    List<String> getAffectedServices(String source);

    /**
     * Suggests edges that could be added to contain an alert within a specific service.
     * @param source name of the service where the alert originates
     * @return list of pairs of service names representing suggested containment edges
     */
    List<Pair<String, String>> suggestContainmentEdges(String source);
} 
