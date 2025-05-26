package com.allertSystem.cache;

import com.allertSystem.Edge;
import lombok.RequiredArgsConstructor;

import java.util.*;

/**
 * Cache manager for network operations.
 */
@RequiredArgsConstructor
public class NetworkCache {
    private final Map<String, Set<String>> affectedServicesCache = new HashMap<>();
    private final Map<String, List<String>> propagationPathCache = new HashMap<>();
    private final Map<String, Set<Edge>> containmentEdgesCache = new HashMap<>();

    public Optional<Set<String>> getAffectedServices(String serviceName) {
        return Optional.ofNullable(affectedServicesCache.get(serviceName))
                .map(HashSet::new);
    }

    public void putAffectedServices(String serviceName, Set<String> affected) {
        affectedServicesCache.put(serviceName, new HashSet<>(affected));
    }

    public Optional<List<String>> getPropagationPath(String sourceService, String targetService) {
        String cacheKey = sourceService + "->" + targetService;
        return Optional.ofNullable(propagationPathCache.get(cacheKey))
                .map(ArrayList::new);
    }

    public void putPropagationPath(String sourceService, String targetService, List<String> path) {
        String cacheKey = sourceService + "->" + targetService;
        propagationPathCache.put(cacheKey, new ArrayList<>(path));
    }

    private String keyForServices(Set<String> services) {
        List<String> sorted = new ArrayList<>(services);
        Collections.sort(sorted);
        return String.join(",", sorted);
    }

    public Optional<Set<Edge>> getContainmentEdges(Set<String> services) {
        return Optional.ofNullable(containmentEdgesCache.get(keyForServices(services)))
                .map(HashSet::new);
    }

    public void putContainmentEdges(Set<String> services, Set<Edge> edges) {
        containmentEdgesCache.put(keyForServices(services), new HashSet<>(edges));
    }

    public void clear() {
        affectedServicesCache.clear();
        propagationPathCache.clear();
        containmentEdgesCache.clear();
    }
} 