package com.reconstruct;

import lombok.*;

import java.util.*;
import java.util.stream.*;

@AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class OrderedListReconstructor {

    public static List<String> reconstructOrder(List<List<String>> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            return Collections.emptyList();
        }

        // Create a map of value -> next value
        Map<String, String> nextMap = new HashMap<>();
        for (List<String> pair : pairs) {
            nextMap.put(pair.get(0), pair.get(1));
        }

        Set<String> nextValues = nextMap.values().stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        // Find the first element (the one that's not a next value)
        String current = nextMap.keySet().stream()
                .filter(value -> !nextValues.contains(value))
                .findFirst()
                .orElse(null);

        if (current == null) {
            return Collections.emptyList();
        }

        // Reconstruct the list
        List<String> result = new ArrayList<>();
        while (current != null) {
            result.add(current);
            current = nextMap.get(current);
        }

        return result;
    }
}
