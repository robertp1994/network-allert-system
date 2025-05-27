# Potencjalne pytania i odpowiedzi

## 1. Dlaczego BFS zamiast DFS dla findAlertPropagationPath?

- BFS gwarantuje znalezienie najkrótszej ścieżki
    - Przeszukuje graf warstwa po warstwie (level by level)
    - Złożoność czasowa: O(V + E)
    - Złożoność pamięciowa: O(V) dla kolejki
    - Idealne dla grafów nieważonych
- DFS mógłby znaleźć dłuższą ścieżkę
    - Idzie w głąb grafu przed sprawdzeniem sąsiadów
    - Może przeszukać całą gałąź przed znalezieniem optymalnej ścieżki
    - Złożoność czasowa: O(V + E)
    - Złożoność pamięciowa: O(V) dla stosu rekurencji
- W kontekście alertów, krótsza ścieżka oznacza szybszą reakcję
    - Mniej węzłów pośrednich
    - Szybsza propagacja alertów
    - Mniejsze opóźnienia w systemie

## 2. Jak rozwiązujesz problem cykli w grafie?

- Implementacja mechanizmu wykrywania cykli
    - Użycie zbioru `visited` do śledzenia odwiedzonych wierzchołków
    - Implementacja flagi `inStack` dla wykrywania cykli w czasie rzeczywistym
    - Algorytm: O(V + E) złożoność czasowa
- Użycie zbioru odwiedzonych wierzchołków
    - Struktura danych: HashSet
    - Złożoność operacji: O(1)
    - Automatyczne czyszczenie po zakończeniu przeszukiwania
- Ochrona przed nieskończonymi pętlami
    - Limity głębokości rekurencji
    - Timeouty dla operacji przeszukiwania
    - Mechanizm wykrywania zbyt długich ścieżek

## 3. Jak radzisz sobie z dużą skalą systemu?

- Efektywne struktury danych (Map, Set)
    - Map: O(1) dostęp do krawędzi
    - Set: O(1) operacje na unikalnych elementach
    - PriorityQueue: O(log n) dla operacji na priorytetach
- Optymalne algorytmy (O(V + E))
    - BFS/DFS: O(V + E)
    - Dijkstra: O((V + E)log V) dla grafów ważonych
    - Floyd-Warshall: O(V³) dla wszystkich par najkrótszych ścieżek
- Możliwość równoległego przetwarzania
    - Podział grafu na podgrafy
    - Wykorzystanie wątków dla niezależnych operacji
    - Batch processing dla dużych zbiorów danych

## 4. Jak testujesz poprawność implementacji?

- Testy jednostkowe dla każdej metody
    - Pokrycie kodu > 90%
    - Testy granicznych przypadków
    - Mockowanie zależności
    - Asercje dla każdej metody
- Testy integracyjne dla całego systemu
    - Scenariusze end-to-end
    - Testy wydajnościowe
    - Testy obciążeniowe
- Testy wydajnościowe dla dużych grafów
    - Benchmarki dla różnych rozmiarów grafów
    - Pomiar zużycia pamięci
    - Analiza złożoności czasowej

## 5. Jakie są ograniczenia implementacji?

- Pamięć: O(V + E)
    - Reprezentacja grafu: O(V + E)
    - Struktury pomocnicze: O(V)
- Czas: O(V + E) dla podstawowych operacji
    - BFS/DFS: O(V + E)
    - Sugerowanie krawędzi: O(V²)
- Skalowalność: do kilku tysięcy wierzchołków
    - Optymalne do 10k wierzchołków
    - Możliwość shardowania
    - Cachowanie wyników

## 6. Jak można zoptymalizować suggestContainmentEdges?

- Implementacja algorytmu heurystycznego
    - Greedy approach
    - Score-based ranking
    - Lokalna optymalizacja
- Użycie struktury danych do śledzenia częstotliwości krawędzi
    - PriorityQueue dla krawędzi
    - Cache dla wyników
    - Indeksowanie wierzchołków
- Możliwość równoległego przetwarzania
    - MapReduce
    - Batch processing
    - Asynchroniczne operacje

## 7. Jak radzisz sobie z błędami wejściowymi?

- Walidacja danych wejściowych
    - Sprawdzanie formatu
    - Weryfikacja integralności
    - Sanityzacja inputu
- Obsługa wyjątków
    - Hierarchia wyjątków
    - Graceful degradation
    - Fallback strategies
- Logowanie błędów
    - Strukturyzowane logi
    - Monitoring
    - Alerting

## 8. Jak można rozszerzyć system?

- Dodanie wag do krawędzi
    - Implementacja Dijkstry
    - Priorytetyzacja propagacji
    - Dynamiczne wagi
- Implementacja różnych strategii propagacji
    - Time-based
    - Priority-based
    - Threshold-based
- Dodanie mechanizmu priorytetyzacji alertów
    - System scoringu
    - Dynamiczne priorytety
    - Reguły biznesowe

## BFS

How BFS Works
BFS follows these key steps:
1. Start at the root node (or any designated starting point) and mark it as visited.
2. Use a queue to track nodes to explore. The first-in, first-out (FIFO) structure ensures nodes are processed in the
order they’re discovered.
3. Explore neighbors level by level:
• Dequeue the front node
• Visit its unvisited adjacent nodes
• Mark them as visited and enqueue them.
4. Repeat until the queue is empty.
Example: In a graph starting at node A, BFS would visit A → B → C → D → E (assuming connections exist), exploring all
immediate neighbors before deeper layers.
Key Characteristics
• Time complexity:  for graphs with vertices and edges.
• Space complexity:  in worst-case scenarios.
• Optimal for unweighted graphs: Guarantees the shortest path between two nodes.
Applications
1. Shortest path finding in networks (e.g., GPS navigation).
2. Web crawlers that index pages level by level.

## DFS

Depth-First Search (DFS) is a fundamental algorithm for traversing or searching tree and graph data structures. The core
idea is to explore as far as possible along each branch before backtracking to explore alternative paths.
How DFS Works
• Start at a node (often called the “root” in trees, or any arbitrary node in a graph).
• Mark the node as visited to avoid revisiting and potential infinite loops, especially in graphs with cycles.
• Explore deeply: For each unvisited neighbor of the current node, recursively visit that neighbor before returning to
the current node.
• Backtrack: When a node has no unvisited neighbors, the algorithm backtracks to the previous node to continue the
search.
• Repeat until all reachable nodes have been visited.
DFS can be implemented recursively (using function call stacks) or iteratively (using an explicit stack data structure).
Key Features
• Stack-based: DFS uses a stack, either the call stack (recursion) or an explicit stack, to remember the path and enable
backtracking.
• Backtracking: The algorithm explores a path until it cannot go further, then backtracks to explore other branches.
• Complete traversal: DFS ensures every node is visited once, making it suitable for exhaustive searches.
• Handling cycles: By marking nodes as visited, DFS avoids revisiting nodes and prevents infinite loops in cyclic
graphs.
Example Use Cases
• Solving mazes and puzzles (e.g., Sudoku, navigating decision trees).
• Topological sorting in directed acyclic graphs.
• Detecting cycles in graphs.
• Finding connected components in undirected graphs.
Pseudocode
Recursive DFS:
DFS(node):
mark node as visited
for each neighbor of node:
if neighbor is not visited:
DFS(neighbor)
Itewrative:
stack.push(start_node)
while stack is not empty:
node = stack.pop()
if node is not visited:
mark node as visited
for each neighbor of node:
if neighbor is not visited:
stack.push(neighbor)

Complexity
• Time complexity: , where is the number of vertices and the number of edges.
• Space complexity:  due to the stack and visited set.
DFS is a powerful tool for exploring all possible paths in a structure, especially when the solution may be far from the
root or when you need to visit every node.
