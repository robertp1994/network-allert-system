package com.allertSystem;

import com.allertSystem.cache.NetworkCache;
import com.allertSystem.validation.ServiceValidator;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the AlertNetwork interface using adjacency list representation.
 * Optimized with caching and improved algorithms.
 */
@RequiredArgsConstructor
public class AlertNetworkImpl implements AlertNetwork {
    private final Map<String, Set<String>> adjacencyList = new HashMap<>();
    private final Map<String, Set<String>> reverseAdjacencyList = new HashMap<>();
    private final ServiceValidator validator = new ServiceValidator(adjacencyList);
    private final NetworkCache cache = new NetworkCache();

    @Override
    public void addService(String serviceName) {
        validator.validateServiceName(serviceName);
        adjacencyList.putIfAbsent(serviceName, new HashSet<>());
        reverseAdjacencyList.putIfAbsent(serviceName, new HashSet<>());
        cache.clear();
    }

    @Override
    public void addDependency(String serviceName, String dependencyName) {
        validator.validateServiceName(serviceName);
        validator.validateServiceName(dependencyName);
        validator.validateServiceExists(serviceName);
        validator.validateServiceExists(dependencyName);
        
        adjacencyList.get(serviceName).add(dependencyName);
        reverseAdjacencyList.get(dependencyName).add(serviceName);
        cache.clear();
    }

    @Override
    public Set<String> getDependencies(String serviceName) {
        validator.validateServiceName(serviceName);
        validator.validateServiceExists(serviceName);
        
        return new HashSet<>(adjacencyList.get(serviceName));
    }

    @Override
    public List<String> findAlertPropagationPath(String sourceService, String targetService) {
        validator.validateServiceName(sourceService);
        validator.validateServiceName(targetService);
        validator.validateServiceExists(sourceService);
        validator.validateServiceExists(targetService);

        return cache.getPropagationPath(sourceService, targetService)
                .orElseGet(() -> findAndCachePropagationPath(sourceService, targetService));
    }

    private List<String> findAndCachePropagationPath(String sourceService, String targetService) {
        Map<String, String> previous = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(sourceService);
        visited.add(sourceService);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(targetService)) {
                List<String> path = reconstructPath(previous, sourceService, targetService);
                cache.putPropagationPath(sourceService, targetService, path);
                return path;
            }
            for (String dependency : adjacencyList.get(current)) {
                if (!visited.contains(dependency)) {
                    visited.add(dependency);
                    previous.put(dependency, current);
                    queue.offer(dependency);
                }
            }
        }
        List<String> emptyPath = Collections.emptyList();
        cache.putPropagationPath(sourceService, targetService, emptyPath);
        return emptyPath;
    }

    @Override
    public Set<String> getAffectedServices(String serviceName) {
        validator.validateServiceName(serviceName);
        validator.validateServiceExists(serviceName);

        return cache.getAffectedServices(serviceName)
                .orElseGet(() -> findAndCacheAffectedServices(serviceName));
    }

    private Set<String> findAndCacheAffectedServices(String serviceName) {
        Set<String> affected = new HashSet<>();
        Set<String> visited = new HashSet<>();
        Stack<String> stack = new Stack<>();
        
        stack.push(serviceName);
        visited.add(serviceName);
        affected.add(serviceName);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            adjacencyList.get(current).stream()
                .filter(dependency -> !visited.contains(dependency))
                .forEach(dependency -> {
                    visited.add(dependency);
                    affected.add(dependency);
                    stack.push(dependency);
                });
        }

        cache.putAffectedServices(serviceName, affected);
        return affected;
    }

    @Override
    public Set<Edge> suggestContainmentEdges(Set<String> services) {
        validator.validateServicesSet(services);

        return cache.getContainmentEdges(services)
                .orElseGet(() -> findAndCacheContainmentEdges(services));
    }

    private Set<Edge> findAndCacheContainmentEdges(Set<String> services) {
        Set<Edge> containmentEdges = new HashSet<>();
        Set<String> servicesSet = new HashSet<>(services);
        
        servicesSet.forEach(start -> {
            Set<String> visited = new HashSet<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.push(start);
            
            while (!stack.isEmpty()) {
                String current = stack.pop();
                if (!visited.add(current)) continue;
                
                adjacencyList.get(current).stream()
                    .filter(dep -> !servicesSet.contains(dep))
                    .forEach(dep -> containmentEdges.add(new Edge(current, dep)));
                    
                adjacencyList.get(current).stream()
                    .filter(dep -> servicesSet.contains(dep))
                    .forEach(stack::push);
            }
        });
        
        cache.putContainmentEdges(services, containmentEdges);
        return containmentEdges;
    }

    @Override
    public List<String> reconstructOrder() {
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String service : adjacencyList.keySet()) {
            if (!visited.contains(service)) {
                if (!topologicalSort(service, visited, temp, order)) {
                    throw new IllegalStateException("Circular dependency detected");
                }
            }
        }

        return order;
    }

    private boolean topologicalSort(String service, Set<String> visited, Set<String> temp, List<String> order) {
        if (temp.contains(service)) {
            return false; // Circular dependency detected
        }
        if (visited.contains(service)) {
            return true;
        }

        temp.add(service);
        for (String dependency : adjacencyList.get(service)) {
            if (!topologicalSort(dependency, visited, temp, order)) {
                return false;
            }
        }
        temp.remove(service);
        visited.add(service);
        order.add(service);
        return true;
    }

    private List<String> reconstructPath(Map<String, String> previous, String source, String target) {
        return Stream.iterate(target, Objects::nonNull, previous::get)
            .collect(Collectors.toList());
    }
} 