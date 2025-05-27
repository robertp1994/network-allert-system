package com.reconstruct;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderedListReconstructorTest {

    @Test
    void testBasicExample() {
        List<List<String>> pairs = Arrays.asList(
                Arrays.asList("A", "B"),
                Arrays.asList("C", "D"),
                Arrays.asList("B", "C"),
                Arrays.asList("D", null)
        );
        List<String> expected = Arrays.asList("A", "B", "C", "D");
        assertEquals(expected, OrderedListReconstructor.reconstructOrder(pairs));
    }

    @Test
    void testEmptyList() {
        List<List<String>> pairs = Collections.emptyList();
        assertTrue(OrderedListReconstructor.reconstructOrder(pairs).isEmpty());
    }

    @Test
    void testSingleElement() {
        List<List<String>> pairs = java.util.List.of(
                java.util.Arrays.asList("A", null)
        );
        List<String> expected = java.util.List.of("A");
        assertEquals(expected, OrderedListReconstructor.reconstructOrder(pairs));
    }
}
