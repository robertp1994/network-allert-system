# Plan implementacji Network Alert System

## 1. Przygotowanie struktury projektu

- [x] Utworzenie podstawowej struktury projektu Maven
- [x] Konfiguracja zależności i narzędzi testowych
- [x] Utworzenie interfejsu AlertNetwork
- [x] Utworzenie klasy implementującej interfejs

## 2. Implementacja podstawowych struktur danych

- [x] Implementacja reprezentacji grafu (Map<String, Set<String>> dla zależności)
- [x] Implementacja metod pomocniczych (addService, addDependency, getDependencies)
- [x] Dodanie walidacji danych wejściowych

## 3. Implementacja algorytmów propagacji alertów

- [x] Implementacja findAlertPropagationPath (BFS dla najkrótszej ścieżki)
- [x] Implementacja getAffectedServices (DFS dla pełnej analizy zasięgu)
- [x] Implementacja suggestContainmentEdges (analiza krytycznych krawędzi)

## 4. Testy i optymalizacja

- [x] Implementacja testów jednostkowych
- [x] Implementacja testów integracyjnych
- [x] Optymalizacja wydajności
- [x] Refaktoryzacja kodu

## 5. Dokumentacja

- [x] Dokumentacja kodu (JavaDoc)
- [x] Dokumentacja algorytmów
- [ ] Przykłady użycia
- [ ] Instrukcja instalacji i uruchomienia

## 6. Bonus - Reconstruct Ordered List

- [x] Implementacja metody reconstructOrder
- [x] Testy dla reconstructOrder
- [x] Optymalizacja rozwiązania 
