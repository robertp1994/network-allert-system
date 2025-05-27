## Algorytmy

### BFS (Breadth-First Search)

- Algorytm przeszukiwania grafu wszerz
- Zasada działania:
  - Przeszukuje wszystkie wierzchołki na tym samym poziomie przed przejściem głębiej
  - Używa kolejki FIFO do przechowywania wierzchołków do odwiedzenia
  - Zapewnia znalezienie najkrótszej ścieżki w grafie nieważonym
- Złożoność:
  - Czasowa: O(V + E), gdzie V to liczba wierzchołków, E to liczba krawędzi
  - Pamięciowa: O(V)
- Zastosowania:
  - Znajdowanie najkrótszej ścieżki w grafie nieważonym
  - Sprawdzanie spójności grafu
  - Znajdowanie wszystkich wierzchołków w danej odległości

Pseudokod:

```
function BFS(graph, start):
    queue = new Queue()
    visited = new Set()
    queue.enqueue(start)
    visited.add(start)
    
    while queue is not empty:
        current = queue.dequeue()
        
        for neighbor in graph.getNeighbors(current):
            if neighbor not in visited:
                visited.add(neighbor)
                queue.enqueue(neighbor)
```

### DFS (Depth-First Search)

- Algorytm przeszukiwania grafu w głąb
- Zasada działania:
  - Idzie jak najgłębiej wzdłuż każdej ścieżki przed powrotem
  - Używa stosu (rekurencji) do przechowywania wierzchołków
  - Może nie znaleźć najkrótszej ścieżki
- Złożoność:
  - Czasowa: O(V + E)
  - Pamięciowa: O(V)
- Zastosowania:
  - Sprawdzanie cykli w grafie
  - Sortowanie topologiczne
  - Znajdowanie silnie spójnych składowych

Pseudokod:

```
function DFS(graph, start):
    visited = new Set()
    
    function dfsRecursive(vertex):
        visited.add(vertex)
        
        for neighbor in graph.getNeighbors(vertex):
            if neighbor not in visited:
                dfsRecursive(neighbor)
    
    dfsRecursive(start)
```

### Algorytm Dijkstry

- Algorytm znajdowania najkrótszej ścieżki w grafie ważonym
- Zasada działania:
  - Używa kolejki priorytetowej do wyboru wierzchołka o najmniejszym koszcie
  - Aktualizuje koszty dojścia do sąsiadów
  - Działa tylko dla grafów z nieujemnymi wagami
- Złożoność:
  - Czasowa: O((V + E)logV) z użyciem kopca binarnego
  - Pamięciowa: O(V)
- Zastosowania:
  - Routing w sieciach
  - Planowanie tras
  - Optymalizacja ścieżek

Pseudokod:

```
function Dijkstra(graph, start):
    distances = new Map() // odległości od startu
    distances[start] = 0
    pq = new PriorityQueue()
    pq.insert(start, 0)
    
    while pq is not empty:
        current = pq.extractMin()
        
        for neighbor in graph.getNeighbors(current):
            newDistance = distances[current] + graph.getWeight(current, neighbor)
            
            if newDistance < distances[neighbor]:
                distances[neighbor] = newDistance
                pq.insert(neighbor, newDistance)
```

### Algorytm A*

- Algorytm znajdowania najkrótszej ścieżki z heurystyką
- Zasada działania:
  - Kombinuje koszt dotychczasowej ścieżki (g(n)) z szacowanym kosztem do celu (h(n))
  - Używa funkcji f(n) = g(n) + h(n) do wyboru następnego wierzchołka
  - Wymaga dopuszczalnej heurystyki (nie przeszacowuje kosztu)
- Złożoność:
  - Czasowa: O(E), gdzie E to liczba krawędzi
  - Pamięciowa: O(V)
- Zastosowania:
  - Gry komputerowe (pathfinding)
  - Robotyka
  - Systemy nawigacji

Pseudokod:

```
function AStar(graph, start, goal):
    openSet = new PriorityQueue()
    openSet.insert(start, 0)
    cameFrom = new Map()
    
    gScore = new Map() // koszt ścieżki od startu
    gScore[start] = 0
    
    fScore = new Map() // gScore + heurystyka
    fScore[start] = heuristic(start, goal)
    
    while openSet is not empty:
        current = openSet.extractMin()
        
        if current == goal:
            return reconstructPath(cameFrom, current)
            
        for neighbor in graph.getNeighbors(current):
            tentativeGScore = gScore[current] + graph.getWeight(current, neighbor)
            
            if tentativeGScore < gScore[neighbor]:
                cameFrom[neighbor] = current
                gScore[neighbor] = tentativeGScore
                fScore[neighbor] = gScore[neighbor] + heuristic(neighbor, goal)
                openSet.insert(neighbor, fScore[neighbor])
```

## Notacja O i analiza złożoności

### Podstawy notacji O

- Notacja O (Big O) opisuje górną granicę złożoności czasowej
- Ignoruje stałe i mniejsze potęgi
- Przykłady:
  - O(1) - stały czas (np. dostęp do elementu w tablicy)
  - O(log n) - logarytmiczny (np. wyszukiwanie binarne)
  - O(n) - liniowy (np. przeszukanie tablicy)
  - O(n log n) - liniowo-logarytmiczny (np. sortowanie)
  - O(n²) - kwadratowy (np. sortowanie bąbelkowe)
  - O(2ⁿ) - wykładniczy (np. problem plecakowy)

O(1) - stała
Najlepsza możliwa złożoność
Czas wykonania nie zależy od rozmiaru danych
Przykład: dostęp do elementu tablicy po indeksie
O(log n) - logarytmiczna
Bardzo dobra złożoność
Przykład: wyszukiwanie binarne
O(n) - liniowa
Dobra złożoność
Przykład: nasz BFS, przeglądanie tablicy
O(n log n) - liniowo-logarytmiczna
Średnia złożoność
Przykład: sortowanie przez scalanie
O(n²) - kwadratowa
Słaba złożoność
Przykład: sortowanie bąbelkowe
O(2ⁿ) - wykładnicza
Najgorsza złożoność
Przykład: problem plecakowy (brute force)
Warto pamiętać, że:
O(1) < O(log n) < O(n) < O(n log n) < O(n²) < O(2ⁿ)
Im mniejsza złożoność, tym lepszy algorytm (przy dużych danych)

### Jak obliczać złożoność

1. Zidentyfikuj podstawowe operacje:

- Przypisania
- Porównania
- Operacje matematyczne
- Dostęp do elementów

2. Zasady obliczania:

- Sekwencja operacji: O(max(operacja1, operacja2))
- Pętle: O(n * złożoność_operacji_wewnątrz)
- Pętle zagnieżdżone: O(n * m)
- Rekurencja: O(branches^depth)

3. Przykłady obliczania:
   ```java
   // O(1) - stała liczba operacji
   int a = 1;
   int b = 2;
   return a + b;

   // O(n) - pętla wykonuje się n razy
   for(int i = 0; i < n; i++) {
       sum += array[i];
   }

   // O(n²) - zagnieżdżone pętle
   for(int i = 0; i < n; i++) {
       for(int j = 0; j < n; j++) {
           sum += array[i][j];
       }
   }
   ```

### Złożoność dla struktur danych

- Tablica:
  - Dostęp: O(1)
  - Wyszukiwanie: O(n)
  - Wstawianie: O(n)
  - Usuwanie: O(n)

- Lista połączona:
  - Dostęp: O(n)
  - Wyszukiwanie: O(n)
  - Wstawianie: O(1) - jeśli mamy referencję
  - Usuwanie: O(1) - jeśli mamy referencję

- HashMap/HashSet:
  - Dostęp: O(1) średnio, O(n) najgorszy przypadek
  - Wstawianie: O(1) średnio, O(n) najgorszy przypadek
  - Usuwanie: O(1) średnio, O(n) najgorszy przypadek

- Drzewo binarne:
  - Wyszukiwanie: O(log n) - zbalansowane
  - Wstawianie: O(log n) - zbalansowane
  - Usuwanie: O(log n) - zbalansowane

### Optymalizacja złożoności

1. Zmniejszanie złożoności:

- Używaj odpowiednich struktur danych
- Unikaj niepotrzebnych operacji
- Wykorzystuj cache i memoizację
- Rozważ algorytmy równoległe

2. Kompromisy:

- Czas vs pamięć
- Złożoność vs czytelność
- Optymalizacja vs utrzymanie kodu 
