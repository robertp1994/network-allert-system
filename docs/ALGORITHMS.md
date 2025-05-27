# Network Alert System - Technical Documentation

## Overview

The Network Alert System implements a directed graph-based solution for managing service dependencies and alert
propagation. The system uses adjacency list representation for efficient graph operations and implements several key
algorithms for alert management.

## Data Structures

### Graph Representation

- **Adjacency List**: `Map<String, Set<String>>`
    - Key: Service name
    - Value: Set of dependent services
- **Reverse Adjacency List**: `Map<String, Set<String>>`
    - Key: Service name
    - Value: Set of services that depend on this service
- **Cache Structures**:
    - Affected services cache: `Map<String, Set<String>>`
    - Propagation path cache: `Map<String, List<String>>`
    - Containment edges cache: `Map<Set<String>, Set<Edge>>`

## Core Algorithms

### 1. Alert Propagation Path (BFS)

```java
findAlertPropagationPath(sourceService, targetService)
```

- **Purpose**: Finds the shortest path for alert propagation between two services
- **Algorithm**: Breadth-First Search (BFS)
- **Time Complexity**: O(V + E), where V is the number of vertices and E is the number of edges
- **Space Complexity**: O(V)
- **Implementation Details**:
    - Uses a queue for BFS traversal
    - Maintains a map of previous nodes for path reconstruction
    - Caches results for repeated queries
    - Returns empty list if no path exists

### 2. Affected Services Analysis (DFS)

```java
getAffectedServices(serviceName)
```

- **Purpose**: Identifies all services that would be affected by an alert in a given service
- **Algorithm**: Depth-First Search (DFS)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Implementation Details**:
    - Uses a stack for DFS traversal
    - Maintains visited set to avoid cycles
    - Caches results for repeated queries
    - Includes the source service in the result set

### 3. Containment Edge Suggestion

```java
suggestContainmentEdges(services)
```

- **Purpose**: Identifies edges that could be added to contain an alert within a specific set of services
- **Algorithm**: Edge Analysis
- **Time Complexity**: O(V * E)
- **Space Complexity**: O(E)
- **Implementation Details**:
    - Analyzes outgoing edges from the specified service set
    - Identifies edges that cross the containment boundary
    - Caches results for repeated queries
    - Returns set of Edge objects representing suggested containment edges

### 4. Service Order Reconstruction

```java
reconstructOrder()
```

- **Purpose**: Determines the topological order of services based on their dependencies
- **Algorithm**: Topological Sort (DFS-based)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Implementation Details**:
    - Uses DFS with cycle detection
    - Maintains temporary and permanent visited sets
    - Throws exception if circular dependencies are detected
    - Returns services in dependency order (most dependent first)

## Optimization Techniques

### 1. Caching

- **Purpose**: Improve performance for repeated operations
- **Implementation**:
    - Separate cache for each operation type
    - Automatic cache invalidation on network modifications
    - Thread-safe cache operations
    - Defensive copying for cache entries

### 2. Validation

- **Purpose**: Ensure data integrity and prevent errors
- **Implementation**:
    - Centralized validation logic
    - Early validation of input parameters
    - Clear error messages for invalid operations
    - Consistent validation across all operations

### 3. Memory Management

- **Purpose**: Optimize memory usage
- **Implementation**:
    - Efficient data structure choices
    - Defensive copying only when necessary
    - Proper cleanup of temporary data structures
    - Minimal object creation in hot paths

## Performance Considerations

### Time Complexity

- Graph Operations: O(V + E)
- Cache Operations: O(1) average case
- Validation: O(1) average case
- Memory Operations: O(1) average case

### Space Complexity

- Graph Storage: O(V + E)
- Cache Storage: O(V + E) worst case
- Temporary Operations: O(V) worst case

### Optimization Trade-offs

- Cache memory vs. computation time
- Validation overhead vs. error prevention
- Defensive copying vs. memory efficiency
- Graph representation vs. operation efficiency 
