package com.assignment.dataset.strategy;

import com.assignment.dataset.util.JsonNodeComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonNodeComparatorTest {

    @Test
    @DisplayName("Should sort numeric fields correctly in ascending order")
    void shouldSortNumericAscending() {
        Map<String, Object> r1 = Map.of("id", 1, "age", 30);
        Map<String, Object> r2 = Map.of("id", 2, "age", 25);
        Map<String, Object> r3 = Map.of("id", 3, "age", 100);

        List<Map<String, Object>> list = new ArrayList<>(List.of(r1, r2, r3));
        list.sort(new JsonNodeComparator("age", true));

        assertEquals(25, list.get(0).get("age"));
        assertEquals(30, list.get(1).get("age"));
        assertEquals(100, list.get(2).get("age"));
    }

    @Test
    @DisplayName("Should sort string fields lexicographically in descending order")
    void shouldSortStringDescending() {
        Map<String, Object> r1 = Map.of("name", "Alice");
        Map<String, Object> r2 = Map.of("name", "John");
        Map<String, Object> r3 = Map.of("name", "Bob");

        List<Map<String, Object>> list = new ArrayList<>(List.of(r1, r2, r3));
        list.sort(new JsonNodeComparator("name", false));

        assertEquals("John", list.get(0).get("name"));
        assertEquals("Bob", list.get(1).get("name"));
        assertEquals("Alice", list.get(2).get("name"));
    }

    @Test
    @DisplayName("Should handle nested keys via dot notation")
    void shouldHandleNestedKeySort() {
        Map<String, Object> r1 = Map.of("user", Map.of("score", 85));
        Map<String, Object> r2 = Map.of("user", Map.of("score", 95));

        List<Map<String, Object>> list = new ArrayList<>(List.of(r2, r1));
        list.sort(new JsonNodeComparator("user.score", true));

        assertEquals(85, ((Map<?, ?>) list.get(0).get("user")).get("score"));
    }
}