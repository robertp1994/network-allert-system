package com.allert;

import com.allert.cache.*;
import com.allert.validation.*;
import lombok.*;

import java.util.*;
import java.util.stream.*;

/**
 * Implementation of the AlertNetwork interface using adjacency list representation.
 * Optimized with caching and improved algorithms.
 */
@RequiredArgsConstructor
public class AlertNetworkImpl implements AlertNetwork {
    private final Map<String, Set<String>> serviceDependencies = new HashMap<>();
    private final Map<String, Set<String>> reverseServiceDependencies = new HashMap<>();
    private final ServiceValidator validator = new ServiceValidator(serviceDependencies);
    private final NetworkCache cache = new NetworkCache();

    @Override
    public void addService(String serviceName) {
        validator.validateServiceName(serviceName);
        serviceDependencies.putIfAbsent(serviceName, new HashSet<>());
        reverseServiceDependencies.putIfAbsent(serviceName, new HashSet<>());
        cache.clear();
    }

    @Override
    public void addDependency(String serviceName, String dependencyName) {
        validator.validateServiceName(serviceName);
        validator.validateServiceName(dependencyName);
        validator.validateServiceExists(serviceName);
        validator.validateServiceExists(dependencyName);

        serviceDependencies.get(serviceName).add(dependencyName);
        reverseServiceDependencies.get(dependencyName).add(serviceName);
        cache.clear();
    }

    @Override
    public Set<String> getDependencies(String serviceName) {
        validator.validateServiceName(serviceName);
        validator.validateServiceExists(serviceName);

        return Set.copyOf(serviceDependencies.get(serviceName));
    }

    @Override
    public List<String> findAlertPropagationPath(String sourceService, String targetService) {
        validator.validateServiceName(sourceService);
        validator.validateServiceName(targetService);
        validator.validateServiceExists(sourceService);
        validator.validateServiceExists(targetService);

        return cache.getPropagationPath(sourceService, targetService).orElseGet(() ->
                findPropagationPath(sourceService, targetService));
    }

    private List<String> findPropagationPath(String sourceService, String targetService) {
        Map<String, String> previous = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(sourceService);
        visited.add(sourceService);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(targetService)) {
                List<String> path = reconstructPath(previous, targetService);
                cache.putPropagationPath(sourceService, targetService, path);
                return path;
            }
            for (String dependency : serviceDependencies.get(current)) {
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

        return cache.getAffectedServices(serviceName).orElseGet(() -> findAndCacheAffectedServices(serviceName));
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
            serviceDependencies.get(current).stream().filter(dependency -> !visited.contains(dependency)).forEach(dependency -> {
                visited.add(dependency);
                affected.add(dependency);
                stack.push(dependency);
            });
        }

        cache.putAffectedServices(serviceName, affected);
        return affected;
    }

    @Override
    public Set<AlertPropagation> suggestContainmentEdges(Set<String> services) {
        validator.validateServicesSet(services);

        return cache.getContainmentEdges(services).orElseGet(() -> findAndCacheContainmentEdges(services));
    }

    private Set<AlertPropagation> findAndCacheContainmentEdges(Set<String> services) {
        Set<AlertPropagation> containmentAlertPropagations = new HashSet<>();
        Set<String> servicesSet = new HashSet<>(services);

        servicesSet.forEach(start -> {
            Set<String> visited = new HashSet<>();
            Deque<String> stack = new ArrayDeque<>();
            stack.push(start);

            while (!stack.isEmpty()) {
                String current = stack.pop();
                if (!visited.add(current)) continue;

                serviceDependencies.get(current).stream().filter(dep -> !servicesSet.contains(dep)).forEach(dep -> containmentAlertPropagations.add(new AlertPropagation(current, dep)));

                serviceDependencies.get(current).stream().filter(servicesSet::contains).forEach(stack::push);
            }
        });

        cache.putContainmentEdges(services, containmentAlertPropagations);
        return containmentAlertPropagations;
    }

    @Override
    public List<String> reconstructOrder() {
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();
        List<String> order = new ArrayList<>();

        for (String service : serviceDependencies.keySet()) {
            if (!visited.contains(service) && topologicalSort(service, visited, temp, order)) {
                    throw new IllegalStateException("Circular dependency detected");
                }

        }

        return order;
    }

    private boolean topologicalSort(String service, Set<String> visited, Set<String> temp, List<String> order) {
        if (temp.contains(service)) {
            return true; // Circular dependency detected
        }
        if (visited.contains(service)) {
            return false;
        }

        temp.add(service);
        for (String dependency : serviceDependencies.get(service)) {
            if (topologicalSort(dependency, visited, temp, order)) {
                return true;
            }
        }
        temp.remove(service);
        visited.add(service);
        order.add(service);
        return false;
    }

    private List<String> reconstructPath(Map<String, String> previous, String target) {
        return Stream.iterate(target, Objects::nonNull, previous::get).toList();
    }
} 
