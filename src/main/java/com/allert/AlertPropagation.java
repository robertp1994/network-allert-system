package com.allert;

/**
 * Represents an edge in the service dependency graph.
 */
public record AlertPropagation(String source, String target) {
} 
