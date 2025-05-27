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

## 9. Jak rozszerzyć system o wagi krawędzi?

- Implementacja algorytmu Dijkstry
    - Zmiana struktury grafu na graf ważony
    - Dodanie pól weight do krawędzi
    - Implementacja algorytmu znajdowania najkrótszej ścieżki
    - Złożoność czasowa: O((V + E)log V)
- Priorytetyzacja propagacji
    - Wagi reprezentujące czas propagacji
    - Wagi reprezentujące koszt propagacji
    - Wagi reprezentujące priorytet kanału
    - Wagi reprezentujące niezawodność połączenia
- Dynamiczne wagi
    - Modyfikacja wag w czasie rzeczywistym
    - Adaptacja do zmieniających się warunków sieci
    - Automatyczna aktualizacja na podstawie metryk
- Rozszerzenie interfejsu
    - Metody zarządzania wagami
    - Ustawianie wag dla krawędzi
    - Wizualizacja wag na grafie
- Optymalizacje
    - Cachowanie wyników dla często używanych ścieżek
    - Przechowywanie wstępnie obliczonych najkrótszych ścieżek
    - Efektywne struktury danych dla grafów ważonych


