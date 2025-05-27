# Wybór algorytmów i struktur danych

## Struktury danych

### Reprezentacja grafu

- `Map<String, Set<String>>` dla przechowywania zależności
    - Klucz: nazwa serwisu
    - Wartość: zbiór serwisów zależnych
    - Wewnętrzna struktura HashMap:
        - Tablica bucketów (domyślnie 16 elementów) przechowująca listy węzłów
        - Każdy węzeł zawiera: klucz, wartość, hash i referencję do następnego węzła
        - Przy przepełnieniu (load factor > 0.75) tablica jest powiększana 2x
    - Wewnętrzna struktura Set:
        - Implementacja HashSet oparta na HashMap
        - Wartości są przechowywane jako klucze w HashMap (wartość = null)
        - Gwarantuje unikalność poprzez mechanizm haszowania

  ### Kluczowe koncepcje HashMap i Set
    - Funkcja haszująca:
        - Przekształca klucz na wartość numeryczną (hash)
        - Powinna zapewniać równomierne rozłożenie wartości
        - W Javie: hashCode() i equals() muszą być spójne
        - Kolizje są rozwiązywane przez łańcuchowanie (Java 8) lub drzewa (Java 8+)

    - Load Factor i Rehashing:
        - Load factor = liczba elementów / rozmiar tablicy
        - Domyślny load factor = 0.75 (75% zapełnienia)
        - Przy przekroczeniu load factor następuje rehashing:
            - Nowa tablica 2x większa
            - Przeliczanie hashów dla wszystkich elementów
            - Redystrybucja elementów do nowych bucketów

    - Thread Safety:
        - HashMap nie jest thread-safe
        - ConcurrentHashMap zapewnia thread safety
        - Collections.synchronizedMap() tworzy thread-safe wrapper
        - Hashtable jest thread-safe, ale przestarzały

    - Wydajność operacji:
        - Get/Put/Remove: O(1) średnio, O(n) w najgorszym przypadku
        - Iteracja: O(n + m) gdzie n to liczba elementów, m to rozmiar tablicy
        - Contains: O(1) dla HashMap, O(1) dla HashSet
        - Add/Remove: O(1) dla HashSet

    - Typowe problemy i rozwiązania:
        - Kolizje hashów:
            - Łańcuchowanie (linked list)
            - Drzewa (Java 8+ dla długich łańcuchów)
            - Open addressing (alternatywne podejście)
        - Memory leaks:
            - Niezmienne klucze
            - Prawidłowa implementacja equals() i hashCode()
        - Wydajność:
            - Odpowiedni initial capacity
            - Unikanie częstego rehashingu
            - Prawidłowy load factor

    - Best Practices:
        - Używaj niezmiennych typów jako kluczy
        - Implementuj equals() i hashCode() zgodnie z kontraktem
        - Unikaj modyfikacji kluczy po dodaniu do mapy
        - Wybierz odpowiedni initial capacity
        - Używaj ConcurrentHashMap dla współbieżności
        - Unikaj synchronizacji na poziomie całej mapy

    - Zalety:
        - Szybki dostęp do zależności O(1) - HashMap zapewnia stały czas dostępu dzięki funkcji haszującej i
          łańcuchowaniu.
        - Łatwe dodawanie/usuwanie zależności - Set gwarantuje unikalność i O(1) operacje dzięki wewnętrznej strukturze
          haszującej.
        - Efektywne wykorzystanie pamięci - struktura przechowuje tylko aktywne zależności, bez narzutu na puste
          miejsca.
        - Łatwa serializacja/deserializacja - format JSON jest natywnie obsługiwany przez większość bibliotek i
          frameworków.

## Algorytmy

### reverseServiceDependencies

Korzyści z używania reverseServiceDependencies:
Szybkie wyszukiwanie - bez tej struktury musielibyśmy przeszukiwać całą mapę serviceDependencies aby znaleźć wszystkie
serwisy zależne od danego serwisu
Efektywność - operacja wyszukiwania ma złożoność O(1) zamiast O(n)
Ułatwia implementację funkcji takich jak getAffectedServices - możemy szybko znaleźć wszystkie serwisy, które będą
dotknięte awarią danego serwisu
Jest to szczególnie ważne w systemie alertowania, gdzie musimy szybko określić wpływ awarii jednego serwisu na inne
serwisy w systemie.

### findAlertPropagationPath

- Algorytm: BFS (Breadth-First Search)
- Złożoność: O(V + E), gdzie V to liczba wierzchołków, E to liczba krawędzi
- Działanie:
    - Przeszukuje graf warstwa po warstwie, zaczynając od źródła alertu i rozszerzając się na sąsiadów.
    - Używa kolejki FIFO do przechowywania wierzchołków do odwiedzenia, co gwarantuje przetwarzanie w odpowiedniej
      kolejności.
- Zalety:
    - Znajduje najkrótszą ścieżkę - każdy wierzchołek jest odwiedzany po raz pierwszy przez najkrótszą możliwą ścieżkę.
    - Gwarantuje optymalne rozwiązanie - pierwsze znalezione rozwiązanie jest zawsze najlepsze, co eliminuje potrzebę
      dalszego przeszukiwania.

### getAffectedServices

- Algorytm: DFS (Depth-First Search)
- Złożoność: O(V + E)
- Działanie:
    - Rekurencyjnie odwiedza wszystkie osiągalne wierzchołki, zagłębiając się maksymalnie w każdą gałąź przed powrotem.
    - Używa stosu do przechowywania stanu, co pozwala na efektywne zarządzanie pamięcią i łatwe cofanie się.
- Zalety:
    - Efektywne dla dużych grafów - zużywa mniej pamięci niż BFS, ponieważ przechowuje tylko aktualną ścieżkę.
    - Naturalne odwzorowanie propagacji alertów - odwiedza serwisy w kolejności zgodnej z ich zależnościami, co ułatwia
      analizę.

### suggestContainmentEdges

- Algorytm: Analiza krytycznych krawędzi
- Złożoność: O(V * (V + E))
- Działanie:
    - Analizuje wszystkie możliwe ścieżki propagacji, używając BFS dla każdego wierzchołka jako źródła.
    - Identyfikuje krawędzie, które pojawiają się najczęściej w różnych ścieżkach propagacji.
- Zalety:
    - Znajduje optymalne miejsca do izolacji - minimalizuje liczbę potrzebnych zmian poprzez wybór krawędzi o
      największym wpływie.
    - Uwzględnia wszystkie możliwe scenariusze - analiza wszystkich ścieżek zapewnia kompleksowe podejście do problemu.

### reconstructOrder

- Algorytm: Topological Sort
- Złożoność: O(V + E)
- Działanie:
    - Buduje listę wierzchołków w kolejności topologicznej, zaczynając od wierzchołków bez zależności.
    - Iteracyjnie usuwa wierzchołki o zerowym stopniu wejściowym, aktualizując stopnie pozostałych wierzchołków.
- Zalety:
    - Gwarantuje poprawną kolejność - wszystkie zależności są zachowane, co jest kluczowe dla prawidłowego działania
      systemu.
    - Efektywna implementacja - liniowa złożoność czasowa i możliwość wykrycia cykli w czasie rzeczywistym.

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
