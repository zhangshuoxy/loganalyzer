package com.officedepot.loganalyzer.utils;

        import java.util.Collections;
        import java.util.Comparator;
        import java.util.LinkedHashMap;
        import java.util.LinkedList;
        import java.util.List;
        import java.util.Map;

public class MapUtils {

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <K extends Comparable,V extends Comparable> Map<K, V> sortByValue(Map<K, V> map, int maxRecord) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        if (maxRecord <= 0) {
            maxRecord = list.size();
        } else {
            maxRecord = Math.min(list.size(), maxRecord);
        }

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (int i = 0; i < maxRecord; i++) {
            Map.Entry<K, V> entry = list.get(i);
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    public static <K extends Comparable,V extends Comparable> Map<K, V> sortByValue(Map<K, V> map) {
        return sortByValue(map, 0);
    }
}
