package com.assignment.dataset.util;

import java.util.Comparator;
import java.util.Map;

public class JsonNodeComparator implements Comparator<Map<String, Object>> {

    private final String sortKey;
    private final boolean ascending;

    public JsonNodeComparator(String sortKey, boolean ascending) {
        this.sortKey = sortKey;
        this.ascending = ascending;
    }

    @Override
    public int compare(Map<String, Object> o1, Map<String, Object> o2) {
        Object v1 = resolveValue(o1, sortKey);
        Object v2 = resolveValue(o2, sortKey);

        if (v1 == null && v2 == null) return 0;
        if (v1 == null) return 1;
        if (v2 == null) return -1;

        int result;
        if (v1 instanceof Number num1 && v2 instanceof Number num2) {
            result = Double.compare(num1.doubleValue(), num2.doubleValue());
        } else if (v1 instanceof Boolean b1 && v2 instanceof Boolean b2) {
            result = Boolean.compare(b1, b2);
        } else {
            result = v1.toString().compareToIgnoreCase(v2.toString());
        }

        return ascending ? result : -result;
    }

    public static Object resolveValue(Map<String, Object> map, String keyPath) {
        if (map == null || keyPath == null) return null;

        if (!keyPath.contains(".")) {
            return map.get(keyPath);
        }

        String[] parts = keyPath.split("\\.");
        Object current = map;
        for (String part : parts) {
            if (current instanceof Map<?, ?> nestedMap) {
                current = nestedMap.get(part);
            } else {
                return null;
            }
        }
        return current;
    }
}