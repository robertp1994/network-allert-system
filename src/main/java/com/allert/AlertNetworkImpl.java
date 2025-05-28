package com.allert;

import com.allert.validation.*;
import javafx.util.*;
import lombok.*;

import java.util.*;

@RequiredArgsConstructor
public class AlertNetworkImpl implements AlertNetwork {
    private final Map<String, Set<String>> serviceDependencies = new HashMap<>();
    private final ServiceValidator validator = new ServiceValidator(serviceDependencies);

    @Override
    public void addService(String service) {
        validator.validateServiceName(service);
        serviceDependencies.putIfAbsent(service, new HashSet<>());
    }

    @Override
    public void addDependency(String fromService, String toService) {
        validator.validateServiceName(fromService);
        validator.validateServiceName(toService);
        validator.validateServiceExists(fromService);
        validator.validateServiceExists(toService);

        serviceDependencies.get(fromService).add(toService);
    }

    @Override
    public List<String> getDependencies(String service) {
        validator.validateServiceName(service);
        validator.validateServiceExists(service);

        return serviceDependencies.get(service).stream().toList();
    }

    @Override
    public List<String> findAlertPropagationPath(String source, String target) {
        validator.validateServiceName(source);
        validator.validateServiceName(target);
        validator.validateServiceExists(source);
        validator.validateServiceExists(target);

        Map<String, String> previous = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(target)) {
                return reconstructPath(previous, target);
            }
            serviceDependencies.get(current).stream().filter(dependency -> !visited.contains(dependency)).forEach(dependency -> {
                visited.add(dependency);
                previous.put(dependency, current);
                queue.offer(dependency);
            });
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAffectedServices(String source) {
        validator.validateServiceName(source);
        validator.validateServiceExists(source);

        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();

        stack.push(source);
        visited.add(source);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            serviceDependencies.get(current).stream()
                    .filter(dependency -> !visited.contains(dependency)).forEach(dependency -> {
                        visited.add(dependency);
                        stack.push(dependency);
                    });
        }

        return visited.stream().toList();
    }

    @Override
    public List<Pair<String, String>> suggestContainmentEdges(String source) {
        validator.validateServiceName(source);
        validator.validateServiceExists(source);

        Map<Pair<String, String>, Integer> edgeDownstreamCount = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Deque<String> stack = new ArrayDeque<>();
        stack.push(source);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (!visited.add(current)) continue;

            serviceDependencies.get(current).forEach(dep -> {
                // Skip edges directly connected to source
                if (!current.equals(source)) {
                    // Count downstream systems
                    Set<String> downstream = new HashSet<>();
                    Deque<String> downstreamStack = new ArrayDeque<>();
                    downstreamStack.push(dep);

                    while (!downstreamStack.isEmpty()) {
                        String downstreamService = downstreamStack.pop();
                        if (downstream.add(downstreamService)) {
                            serviceDependencies.get(downstreamService).forEach(downstreamStack::push);
                        }
                    }

                    edgeDownstreamCount.put(new Pair<>(current, dep), downstream.size());
                }
                stack.push(dep);
            });
        }

        return edgeDownstreamCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(maxEntry -> edgeDownstreamCount.entrySet().stream()
                        .filter(entry -> entry.getValue().equals(maxEntry.getValue()))
                        .map(Map.Entry::getKey)
                        .toList())
                .orElse(List.of());
    }

    private List<String> reconstructPath(Map<String, String> previous, String target) {
        List<String> path = new ArrayList<>();
        String current = target;
        while (current != null) {
            path.add(current);
            current = previous.get(current);
        }
        Collections.reverse(path);
        return path;
    }
} 
