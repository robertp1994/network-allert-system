## Network Alert System - Java Tech Interview Assignment

### Assignment: Network Alert System

Imagine you're building a network alert system for a large-scale distributed service.
The system is modeled as a directed graph where each node represents a service or
subsystem,
and edges represent alert propagation dependencies (i.e., if node A fails, it can trigger an
alert in node B).
You are tasked with implementing core logic to determine how alerts spread and suggest
optimization steps for containment.

---

## 1. Propagation Path Detection

**Goal:**  
Implement a method to find the shortest path of alert propagation from a source system to a target system.

- **Return:** Ordered list of affected services along the path.
- **No path:** Return an empty list.

**Method Signature:**
```java
List<String> findAlertPropagationPath(String source, String target);
```

**Expected Behavior:**  
Detect how a failure at one service could impact others downstream.

---

## 2. Alert Impact Scope

**Goal:**  
Given a service that triggered an alert, return a list of all services that could be affected directly or indirectly (reachability analysis).

**Method Signature:**
```java
List<String> getAffectedServices(String source);
```

---

## 3. (Bonus) Containment Suggestions

**Goal:**  
Suggest edges (connections) that, if temporarily disabled, would minimize the alert spread from a given source.

- **Prioritize:** Cutting paths that isolate the largest number of downstream systems, while cutting the fewest edges.

**Method Signature:**
```java
List<Pair<String, String>> suggestContainmentEdges(String source);
```

---

## Provided Interface

```java
public interface AlertNetwork {
    void addService(String service);
    void addDependency(String fromService, String toService); // Directed edge
    List<String> getDependencies(String service);
    List<String> findAlertPropagationPath(String source, String target);
    List<String> getAffectedServices(String source);
    List<Pair<String, String>> suggestContainmentEdges(String source); // Bonus
}
```

---

## Example Scenario

Suppose you add the following dependencies:
```java
addDependency("A", "B");
addDependency("B", "C");
addDependency("A", "D");
addDependency("D", "C");
```

- `findAlertPropagationPath("A", "C")` might return `["A", "B", "C"]` (or `["A", "D", "C"]`)
- `getAffectedServices("A")` would return `["B", "C", "D"]`
- `suggestContainmentEdges("A")` could return `[("B", "C")]` or `[("D", "C")]` depending on implementation

---

## Evaluation Criteria

- Correctness and completeness
- Use of appropriate graph algorithms (BFS, DFS, etc.)
- Code readability and documentation
- Efficiency (bonus)

---

## Reconstruct Ordered List

### Problem Description

Given a `List<List<String>>` called `pairs`, where each inner list contains exactly two strings:

- First element: value
- Second element: name of the next value

**Goal:**  
Reconstruct the correct ordered list of values as a `List<String>`, starting from the first element in the chain and ending at the last (where the next value is `null` or missing).

**Assumptions:**

- No cycles
- Chain is continuous, no branching
- Exactly one valid ordering

---

### Example Input

```java
List<List<String>> pairs = List.of(
    List.of("A", "B"),
    List.of("C", "D"),
    List.of("B", "C"),
    List.of("D", null)
);
```

**Expected Output:**
```java
List<String> ordered = List.of("A", "B", "C", "D");
```

---

### Instructions

1. Implement the following method:
    ```java
    public static List<String> reconstructOrder(List<List<String>> pairs) {
        // your code here
    }
    ```
2. Add a few test cases in a `main` method to demonstrate your solution.

---

### Evaluation Criteria

- Correctness of the logic
- Use of appropriate data structures
- Code readability and structure
- Handling of edge cases
- Efficiency of your solution

Sources
[1] Java-homework-assignments.pdf https://ppl-ai-file-upload.s3.amazonaws.com/web/direct-files/attachments/55608005/938e5f44-76e7-449f-9c8d-00bca03f1086/Java-homework-assignments.pdf
