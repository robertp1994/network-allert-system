package com.allert;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AlertNetworkImplTest {
    private AlertNetwork network;

    @BeforeEach
    void setUp() {
        network = new AlertNetworkImpl();
    }

    @Test
    @DisplayName("Should add service successfully")
    void addService_ValidName_ServiceAdded() {
        network.addService("service1");
        assertTrue(network.getDependencies("service1").isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when adding service with invalid name")
    void addService_InvalidName_ThrowsException(String serviceName) {
        assertThrows(IllegalArgumentException.class, () -> network.addService(serviceName));
    }

    @Test
    @DisplayName("Should add dependency between services")
    void addDependency_ValidServices_DependencyAdded() {
        network.addService("service1");
        network.addService("service2");
        network.addDependency("service1", "service2");
        
        Set<String> dependencies = network.getDependencies("service1");
        assertEquals(1, dependencies.size());
        assertTrue(dependencies.contains("service2"));
    }

    @Test
    @DisplayName("Should throw exception when adding dependency to non-existent service")
    void addDependency_NonExistentService_ThrowsException() {
        network.addService("service1");
        assertThrows(IllegalArgumentException.class, () -> network.addDependency("service1", "service2"));
    }

    @Test
    @DisplayName("Should find shortest propagation path")
    void findAlertPropagationPath_ValidPath_ReturnsPath() {
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addDependency("A", "B");
        network.addDependency("B", "C");

        List<String> path = network.findAlertPropagationPath("A", "C");
        assertEquals(Arrays.asList("A", "B", "C"), path);
    }

    @Test
    @DisplayName("Should return empty list when no path exists")
    void findAlertPropagationPath_NoPath_ReturnsEmptyList() {
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addDependency("A", "B");

        List<String> path = network.findAlertPropagationPath("A", "C");
        assertTrue(path.isEmpty());
    }

    @Test
    @DisplayName("Should get all affected services")
    void getAffectedServices_ValidService_ReturnsAllAffected() {
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addDependency("A", "B");
        network.addDependency("B", "C");

        Set<String> affected = network.getAffectedServices("A");
        assertEquals(new HashSet<>(Arrays.asList("A", "B", "C")), affected);
    }

    @Test
    @DisplayName("Should suggest containment edges")
    void suggestContainmentEdges_ValidServices_ReturnsEdges() {
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addDependency("A", "B");
        network.addDependency("B", "C");

        Set<String> services = new HashSet<>(Arrays.asList("A", "B"));
        Set<AlertPropagation> containmentAlertPropagations = network.suggestContainmentEdges(services);

        assertEquals(1, containmentAlertPropagations.size());
        AlertPropagation alertPropagation = containmentAlertPropagations.iterator().next();
        assertEquals("B", alertPropagation.getSource());
        assertEquals("C", alertPropagation.getTarget());
    }

    @Test
    @DisplayName("Should reconstruct order correctly")
    void reconstructOrder_ValidDependencies_ReturnsOrder() {
        network.addService("A");
        network.addService("B");
        network.addService("C");
        network.addDependency("A", "B");
        network.addDependency("B", "C");

        List<String> order = network.reconstructOrder();
        assertEquals(Arrays.asList("C", "B", "A"), order);
    }

    @Test
    @DisplayName("Should throw exception when circular dependency detected")
    void reconstructOrder_CircularDependency_ThrowsException() {
        network.addService("A");
        network.addService("B");
        network.addDependency("A", "B");
        network.addDependency("B", "A");

        assertThrows(IllegalStateException.class, () -> network.reconstructOrder());
    }
} 
