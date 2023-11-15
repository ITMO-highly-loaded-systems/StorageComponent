package com.storage.SS;

import java.util.List;

public class LeftBinarySearch {
    public static <K extends Comparable<K>> int search(List<SSMap<K>> list, K x) {
        var l = 0;
        var m = 0;
        var r = list.size();
        while (l < r) {
            m = (l + r) / 2;
            if (list.get(m).getKey().compareTo(x) < 0) {
                l = m;
            } else {
                r = m;
            }
        }
        return r;
    }
}
