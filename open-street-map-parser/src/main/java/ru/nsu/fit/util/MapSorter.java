package ru.nsu.fit.util;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MapSorter {
    private MapSorter() {}

    private static<K, V> Map<K, V> sort(Map<K, V> map, Comparator<? super Map.Entry<K, V>> comparator) {
        return map
                .entrySet()
                .stream()
                .sorted(comparator)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }

    public static<K, V> Map<K, V> sortByKey(Map<K, V> map, Comparator<K> comparator) {
        return sort(map, Map.Entry.comparingByKey(comparator));
    }

    public static<K extends Comparable<K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        return sort(map, Map.Entry.comparingByKey());
    }

    public static<K, V> Map<K, V> sortByValue(Map<K, V> map, Comparator<V> comparator) {
        return sort(map, Map.Entry.comparingByValue(comparator));
    }

    public static<K, V extends Comparable<V>> Map<K, V> sortByValue(Map<K, V> map) {
        return sort(map, Map.Entry.comparingByValue());
    }
}
