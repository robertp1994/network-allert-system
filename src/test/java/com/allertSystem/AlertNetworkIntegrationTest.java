package com.allertSystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlertNetworkIntegrationTest {
    private AlertNetwork network;

    @BeforeEach
    void setUp() {
        network = new AlertNetworkImpl();
    }

    @Test
    @DisplayName("Should handle complex network with multiple paths")
    void complexNetwork_MultiplePaths_HandlesCorrectly() {
        // Setup a complex network
        // A -> B -> C
        // A -> D -> C
        // B -> E
        // D -> E
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addService("D");
        network.addService("E");

        network.addDependency("A", "B");
        network.addDependency("B", "C");
        network.addDependency("A", "D");
        network.addDependency("D", "C");
        network.addDependency("B", "E");
        network.addDependency("D", "E");

        // Test alert propagation path
        List<String> path = network.findAlertPropagationPath("A", "C");
        assertTrue(path.size() == 3);
        assertEquals("A", path.get(0));
        assertEquals("C", path.get(2));

        // Test affected services
        Set<String> affected = network.getAffectedServices("A");
        assertEquals(new HashSet<>(Arrays.asList("A", "B", "C", "D", "E")), affected);

        // Test containment edges
        Set<String> servicesToContain = new HashSet<>(Arrays.asList("A", "B", "D"));
        Set<Edge> containmentEdges = network.suggestContainmentEdges(servicesToContain);
        assertEquals(2, containmentEdges.size());
        assertTrue(containmentEdges.stream().anyMatch(e -> e.getSource().equals("B") && e.getTarget().equals("C")));
        assertTrue(containmentEdges.stream().anyMatch(e -> e.getSource().equals("D") && e.getTarget().equals("C")));
    }

    @Test
    @DisplayName("Should handle network with multiple levels of dependencies")
    void multiLevelDependencies_HandlesCorrectly() {
        // Setup a network with multiple levels
        // A -> B -> C -> D -> E
        // A -> F -> G -> H
        // B -> I -> J
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addService("D");
        network.addService("E");
        network.addService("F");
        network.addService("G");
        network.addService("H");
        network.addService("I");
        network.addService("J");

        network.addDependency("A", "B");
        network.addDependency("B", "C");
        network.addDependency("C", "D");
        network.addDependency("D", "E");
        network.addDependency("A", "F");
        network.addDependency("F", "G");
        network.addDependency("G", "H");
        network.addDependency("B", "I");
        network.addDependency("I", "J");

        // Test reconstruct order
        List<String> order = network.reconstructOrder();
        assertTrue(order.indexOf("A") > order.indexOf("B"));
        assertTrue(order.indexOf("B") > order.indexOf("C"));
        assertTrue(order.indexOf("C") > order.indexOf("D"));
        assertTrue(order.indexOf("D") > order.indexOf("E"));
        assertTrue(order.indexOf("A") > order.indexOf("F"));
        assertTrue(order.indexOf("F") > order.indexOf("G"));
        assertTrue(order.indexOf("G") > order.indexOf("H"));
        assertTrue(order.indexOf("B") > order.indexOf("I"));
        assertTrue(order.indexOf("I") > order.indexOf("J"));
    }

    @Test
    @DisplayName("Should handle network with multiple containment scenarios")
    void multipleContainmentScenarios_HandlesCorrectly() {
        // Setup a network with multiple containment scenarios
        // A -> B -> C -> D
        // A -> E -> F -> D
        // B -> G -> H
        // E -> I -> J
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addService("D");
        network.addService("E");
        network.addService("F");
        network.addService("G");
        network.addService("H");
        network.addService("I");
        network.addService("J");

        network.addDependency("A", "B");
        network.addDependency("B", "C");
        network.addDependency("C", "D");
        network.addDependency("A", "E");
        network.addDependency("E", "F");
        network.addDependency("F", "D");
        network.addDependency("B", "G");
        network.addDependency("G", "H");
        network.addDependency("E", "I");
        network.addDependency("I", "J");

        // Test containment for different service sets
        Set<String> services1 = new HashSet<>(Arrays.asList("A", "B", "C"));
        Set<Edge> containment1 = network.suggestContainmentEdges(services1);
        assertEquals(1, containment1.size());
        assertTrue(containment1.stream().anyMatch(e -> e.getSource().equals("C") && e.getTarget().equals("D")));

        Set<String> services2 = new HashSet<>(Arrays.asList("A", "E", "F"));
        Set<Edge> containment2 = network.suggestContainmentEdges(services2);
        assertEquals(1, containment2.size());
        assertTrue(containment2.stream().anyMatch(e -> e.getSource().equals("F") && e.getTarget().equals("D")));

        Set<String> services3 = new HashSet<>(Arrays.asList("B", "G"));
        Set<Edge> containment3 = network.suggestContainmentEdges(services3);
        assertEquals(1, containment3.size());
        assertTrue(containment3.stream().anyMatch(e -> e.getSource().equals("G") && e.getTarget().equals("H")));
    }

    @Test
    @DisplayName("Should handle network with complex alert propagation scenarios")
    void complexAlertPropagation_HandlesCorrectly() {
        // Setup a network with complex propagation paths
        // A -> B -> C -> D
        // A -> E -> F -> D
        // B -> G -> H -> D
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addService("D");
        network.addService("E");
        network.addService("F");
        network.addService("G");
        network.addService("H");

        network.addDependency("A", "B");
        network.addDependency("B", "C");
        network.addDependency("C", "D");
        network.addDependency("A", "E");
        network.addDependency("E", "F");
        network.addDependency("F", "D");
        network.addDependency("B", "G");
        network.addDependency("G", "H");
        network.addDependency("H", "D");

        // Test shortest path
        List<String> path = network.findAlertPropagationPath("A", "D");
        assertTrue(path.size() == 4);
        assertEquals("A", path.get(0));
        assertEquals("D", path.get(3));

        // Test affected services
        Set<String> affected = network.getAffectedServices("A");
        assertEquals(new HashSet<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H")), affected);

        // Test containment
        Set<String> services = new HashSet<>(Arrays.asList("A", "B", "E"));
        Set<Edge> containment = network.suggestContainmentEdges(services);
        assertEquals(3, containment.size());
        assertTrue(containment.stream().anyMatch(e -> e.getSource().equals("C") && e.getTarget().equals("D")));
        assertTrue(containment.stream().anyMatch(e -> e.getSource().equals("F") && e.getTarget().equals("D")));
        assertTrue(containment.stream().anyMatch(e -> e.getSource().equals("H") && e.getTarget().equals("D")));
    }
} 