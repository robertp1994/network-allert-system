package com.allert;

import javafx.util.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.jupiter.api.Assertions.*;

class AlertNetworkImplTest {
    private AlertNetwork alertNetwork;

    @BeforeEach
    void setUp() {
        alertNetwork = new AlertNetworkImpl();
    }

    @Test
    @DisplayName("Should add service successfully")
    void shouldAddServiceSuccessfully() {
        // Given
        String serviceName = "service1";

        // When
        alertNetwork.addService(serviceName);

        // Then
        assertTrue(alertNetwork.getDependencies("service1").isEmpty());
    }

    @Test
    @DisplayName("Should add dependency between services")
    void shouldAddDependencyBetweenServices() {
        // Given
        alertNetwork.addService("service1");
        alertNetwork.addService("service2");

        // When
        alertNetwork.addDependency("service1", "service2");

        // Then
        List<String> dependencies = alertNetwork.getDependencies("service1");
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains("service2"));
    }

    @Test
    @DisplayName("Should throw exception when adding dependency to non-existent service")
    void shouldThrowExceptionWhenAddingDependencyToNonExistentService() {
        // Given
        alertNetwork.addService("service1");

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> alertNetwork.addDependency("service1", "service2"));
    }



    @Nested
    @DisplayName("Alert Propagation Path Tests")
    class AlertPropagationPathTests {

        private static Stream<Arguments> provideGraphConfigurations() {
            return Stream.of(
                    // Test case 1: Simple linear path
                    Arguments.of(
                            List.of("A", "B", "C"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("B", "C")
                            ),
                            "A", "C",
                            List.of("A", "B", "C")
                    ),
                    // Test case 2: Diamond shape graph
                    Arguments.of(
                            List.of("A", "B", "C", "D"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("A", "C"),
                                    new Pair<>("B", "D"),
                                    new Pair<>("C", "D")
                            ),
                            "A", "D",
                            List.of("A", "B", "D")
                    ),
                    // Test case 3: Complex graph with cycles
                    Arguments.of(
                            List.of("A", "B", "C", "D", "E"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("B", "C"),
                                    new Pair<>("C", "D"),
                                    new Pair<>("D", "E"),
                                    new Pair<>("E", "B")
                            ),
                            "A", "E",
                            List.of("A", "B", "C", "D", "E")
                    )
            );
        }

        @ParameterizedTest
        @DisplayName("Should find path in various graph configurations")
        @MethodSource("provideGraphConfigurations")
        void shouldFindPathInVariousGraphConfigurations(
                List<String> services,
                List<Pair<String, String>> dependencies,
                String start,
                String end,
                List<String> expectedPath) {
            // Given
            services.forEach(alertNetwork::addService);
            dependencies.forEach(pair ->
                    alertNetwork.addDependency(pair.getKey(), pair.getValue()));

            // When
            List<String> path = alertNetwork.findAlertPropagationPath(start, end);

            // Then
            assertEquals(expectedPath, path);
        }

        @Test
        @DisplayName("Should return empty list when no path exists")
        void shouldReturnEmptyListWhenNoPathExists() {
            // Given
            alertNetwork.addService("A");
            alertNetwork.addService("B");
            alertNetwork.addService("C");
            alertNetwork.addDependency("A", "B");

            // When
            List<String> path = alertNetwork.findAlertPropagationPath("A", "C");

            // Then
            assertTrue(path.isEmpty());
        }

        @Test
        @DisplayName("Should throw exception when service does not exist")
        void shouldThrowExceptionWhenServiceDoesNotExist() {
            // Given
            alertNetwork.addService("A");

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> alertNetwork.findAlertPropagationPath("A", "B"));
        }
    }

    @Nested
    @DisplayName("Get Affected Services Tests")
    class GetAffectedServicesTests {

        private static Stream<Arguments> provideGraphConfigurations() {
            return Stream.of(
                    // Test case 1: Simple linear path
                    Arguments.of(
                            List.of("A", "B", "C"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("B", "C")
                            ),
                            "A",
                            List.of("A", "B", "C")
                    ),
                    // Test case 2: Complex graph with multiple paths
                    Arguments.of(
                            List.of("A", "B", "C", "D", "E"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("A", "C"),
                                    new Pair<>("B", "D"),
                                    new Pair<>("C", "D"),
                                    new Pair<>("D", "E")
                            ),
                            "A",
                            List.of("A", "B", "C", "D", "E")
                    ),
                    // Test case 3: Graph with isolated components
                    Arguments.of(
                            List.of("A", "B", "C", "D"),
                            List.of(
                                    new Pair<>("A", "B"),
                                    new Pair<>("C", "D")
                            ),
                            "A",
                            List.of("A", "B")
                    )
            );
        }

        @Test
        @DisplayName("Should return only source service when no dependencies exist")
        void shouldReturnOnlySourceServiceWhenNoDependenciesExist() {
            // Given
            alertNetwork.addService("A");
            alertNetwork.addService("B");
            alertNetwork.addService("C");

            // When
            List<String> affected = alertNetwork.getAffectedServices("A");

            // Then
            assertEquals(List.of("A"), affected);
        }

        @Test
        @DisplayName("Should throw exception when service does not exist")
        void shouldThrowExceptionWhenServiceDoesNotExist() {
            // Given
            alertNetwork.addService("A");

            // When & Then
            assertThrows(IllegalArgumentException.class,
                    () -> alertNetwork.getAffectedServices("B"));
        }

        @ParameterizedTest
        @DisplayName("Should get affected services in various graph configurations")
        @MethodSource("provideGraphConfigurations")
        void shouldGetAffectedServicesInVariousGraphConfigurations(
                List<String> services,
                List<Pair<String, String>> dependencies,
                String source,
                List<String> expectedAffected) {
            // Given
            services.forEach(alertNetwork::addService);
            dependencies.forEach(pair ->
                    alertNetwork.addDependency(pair.getKey(), pair.getValue()));

            // When
            List<String> affected = alertNetwork.getAffectedServices(source);

            // Then
            assertEquals(expectedAffected, affected);
        }
    }

    @Test
    @DisplayName("Should suggest containment edges")
    void shouldSuggestContainmentEdges() {
        // Given
        alertNetwork.addService("A");
        alertNetwork.addService("B");
        alertNetwork.addService("C");
        alertNetwork.addService("D");
        alertNetwork.addDependency("A", "B");
        alertNetwork.addDependency("B", "C");
        alertNetwork.addDependency("A", "D");
        alertNetwork.addDependency("D", "C");

        // When
        List<Pair<String, String>> containmentEdges = alertNetwork.suggestContainmentEdges("A");

        // Then
        assertEquals(2, containmentEdges.size());
        assertTrue(containmentEdges.stream().anyMatch(e -> e.getKey().equals("D") && e.getValue().equals("C")));
    }

    @Test
    @DisplayName("Should suggest containment edges for graph with cycle")
    void shouldSuggestContainmentEdgesForGraphWithCycle() {
        // Given
        alertNetwork.addService("A");
        alertNetwork.addService("B");
        alertNetwork.addService("C");
        alertNetwork.addService("D");
        alertNetwork.addService("E");
        alertNetwork.addService("F");
        alertNetwork.addService("G");

        alertNetwork.addDependency("A", "B");
        alertNetwork.addDependency("A", "C");
        alertNetwork.addDependency("A", "G");

        alertNetwork.addDependency("B", "C");

        alertNetwork.addDependency("C", "D");
        alertNetwork.addDependency("C", "E");

        alertNetwork.addDependency("D", "F");
        alertNetwork.addDependency("D", "B");

        // When
        List<Pair<String, String>> containmentEdges = alertNetwork.suggestContainmentEdges("A");

        // Then
        assertEquals(3, containmentEdges.size());
        assertTrue(containmentEdges.stream().anyMatch(e -> e.getKey().equals("C") && e.getValue().equals("D")));
    }
} 
