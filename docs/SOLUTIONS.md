### 1. Alert Propagation Path (BFS)

```java
findAlertPropagationPath(sourceService, targetService)
```

- Działanie:
    - Przeszukuje graf warstwa po warstwie, zaczynając od źródła alertu i rozszerzając się na sąsiadów.
    - Używa kolejki FIFO do przechowywania wierzchołków do odwiedzenia, co gwarantuje przetwarzanie w odpowiedniej
      kolejności.
- Zalety:
    - Znajduje najkrótszą ścieżkę - każdy wierzchołek jest odwiedzany po raz pierwszy przez najkrótszą możliwą ścieżkę.
    - Gwarantuje optymalne rozwiązanie - pierwsze znalezione rozwiązanie jest zawsze najlepsze, co eliminuje potrzebę
      dalszego przeszukiwania.
    - Kompletność - Jeśli ścieżka istnieje, BFS ją znajdzie Jeśli nie ma ścieżki, BFS to potwierdzi (zwracając pustą
      listę)

- Alternatywy byłyby gorsze:
    - DFS mógłby znaleźć dłuższą ścieżkę
    - Dijkstra byłby nadmiarowy, bo wszystkie krawędzie mają tę samą wagę
    - A* byłby niepotrzebnie skomplikowany, bo nie mamy heurystyki

- **Purpose**: Finds the shortest path for alert propagation between two services
- **Algorithm**: Breadth-First Search (BFS)
- **Time Complexity**: O(V + E), where V is the number of vertices and E is the number of edges
- **Space Complexity**: O(V)
- **Implementation Details**:
    - Uses a queue for BFS traversal
    - Maintains a map of previous nodes for path reconstruction
    - Returns empty list if no path exists

Tak, masz rację. Użycie add(0, current) w ArrayList jest operacją kosztowną, ponieważ wymaga przesunięcia wszystkich
istniejących elementów w prawo. Złożoność czasowa tej operacji to O(n), gdzie n to rozmiar listy.
W metodzie reconstructPath możemy to zoptymalizować na dwa sposoby:
Użyć LinkedList zamiast ArrayList - wtedy dodawanie na początku będzie O(1)
Albo najpierw zbudować listę w odwrotnej kolejności, a na końcu ją odwrócić
Proponuję drugie rozwiązanie, ponieważ:
Jest bardziej wydajne niż LinkedList dla małych list
Nie wymaga zmiany typu kolekcji
Odwrócenie listy jest operacją O(n), ale wykonujemy ją tylko raz

### 2. Affected Services Analysis (DFS)

```java
getAffectedServices(serviceName)
```

- Działanie:
    - Rekurencyjnie odwiedza wszystkie osiągalne wierzchołki, zagłębiając się maksymalnie w każdą gałąź przed powrotem.
    - Używa stosu do przechowywania stanu, co pozwala na efektywne zarządzanie pamięcią i łatwe cofanie się.
- Zalety:
    - Efektywne dla dużych grafów - zużywa mniej pamięci niż BFS, ponieważ przechowuje tylko aktualną ścieżkę.
    - Naturalne odwzorowanie propagacji alertów - odwiedza serwisy w kolejności zgodnej z ich zależnościami, co ułatwia
      analizę.

- **Purpose**: Identifies all services that would be affected by an alert in a given service
- **Algorithm**: Depth-First Search (DFS)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)
- **Implementation Details**:
    - Uses a stack for DFS traversal
    - Maintains visited set to avoid cycles
    - Caches results for repeated queries
    - Includes the source service in the result set

#### Recursive Depth-First Search

```
@Override
public List<String> getAffectedServices(String source) {
    Set<String> affected = new HashSet<>();
    Set<String> visited = new HashSet<>();
    
    findAffectedServices(source, affected, visited);
    
    return new ArrayList<>(affected);
}

private void findAffectedServices(String current, Set<String> affected, Set<String> visited) {
if (!visited.add(current)) {
return;
}

    affected.add(current);
    
    serviceDependencies.get(current).forEach(dependency -> 
        findAffectedServices(dependency, affected, visited)
    );
}
```

### 3. Containment Edge Suggestion

```java
suggestContainmentEdges(services)
```


    
