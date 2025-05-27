# Data Structures in Java

## Core Data Structures

### HashMap

- **Purpose**: Key-value storage with fast lookups
- **Implementation Details**:
    - Uses array of buckets with linked lists/red-black trees
    - Default initial capacity: 16
    - Load factor: 0.75
    - Buckets convert to trees when list length > 8
- **Performance Comparison**:
    - vs TreeMap: Faster for lookups (O(1) vs O(log n))
    - vs LinkedHashMap: More memory efficient
    - vs ConcurrentHashMap: Faster but not thread-safe

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

## Collections

### Queue

- **Purpose**: FIFO (First-In-First-Out) data structure
- **Implementation**: LinkedList
- **Java Implementation Details**:
    - LinkedList implements both List and Deque interfaces
    - Uses doubly-linked list internally
    - Each node contains value and references to next/previous nodes
- **Performance Comparison**:
    - vs ArrayDeque: ArrayDeque is generally faster for queue operations
    - vs ArrayList: Better for frequent insertions/deletions
    - vs PriorityQueue: Maintains insertion order, PriorityQueue doesn't

### Deque

- **Purpose**: Double-ended queue supporting operations at both ends
- **Implementation**: ArrayDeque
- **Java Implementation Details**:
    - Uses resizable circular array
    - Default initial capacity: 16
    - Grows by factor of 2 when full
    - No null elements allowed
- **Performance Comparison**:
    - vs LinkedList: Faster for most operations
    - vs Stack: More efficient than legacy Stack
    - vs ArrayList: Better for stack/queue operations

### Set

- **Purpose**: Collection of unique elements
- **Implementation**: HashSet
- **Java Implementation Details**:
    - Internally uses HashMap
    - Elements are stored as keys in HashMap with dummy value
    - No duplicate elements allowed
    - No order guaranteed
- **Performance Comparison**:
    - vs TreeSet: Faster for lookups (O(1) vs O(log n))
    - vs LinkedHashSet: More memory efficient
    - vs EnumSet: Faster for enum types

### List

- **Purpose**: Ordered collection of elements
- **Implementation**: ArrayList
- **Java Implementation Details**:
    - Uses resizable array
    - Default initial capacity: 10
    - Grows by 50% when full
    - Random access is O(1)
- **Performance Comparison**:
    - vs LinkedList: Better for random access
    - vs Vector: Not synchronized, thus faster
    - vs CopyOnWriteArrayList: Faster but not thread-safe

## Performance Characteristics

### Time Complexity

- **HashMap/Set**:
    - Insertion: O(1) average, O(n) worst
    - Lookup: O(1) average, O(n) worst
    - Deletion: O(1) average, O(n) worst
- **ArrayList**:
    - Random access: O(1)
    - Insertion at end: O(1) amortized
    - Insertion at beginning: O(n)
    - Deletion: O(n)
- **LinkedList/ArrayDeque**:
    - Insertion at ends: O(1)
    - Random access: O(n)
    - Deletion at ends: O(1)

### Space Complexity

- **HashMap/Set**: O(n)
- **ArrayList**: O(n)
- **LinkedList/ArrayDeque**: O(n)
- **ArrayDeque**: More memory efficient than LinkedList

### Memory Overhead

- **HashMap**: ~20 bytes per entry
- **ArrayList**: ~8 bytes per element
- **LinkedList**: ~24 bytes per node
- **ArrayDeque**: ~8 bytes per element

## Best Practices

1. Use ArrayList for random access and when size is known
2. Use ArrayDeque for stack/queue operations
3. Use HashSet for unique elements and fast lookups
4. Use HashMap for key-value pairs with fast lookups
5. Consider capacity hints for large collections
6. Use appropriate initial capacity to avoid resizing
