package com.storage.SS;

import com.storage.SS.SSMap;

import java.util.ArrayList;

public class SSTable<K extends Comparable<K>> {
    private ArrayList<String> paths;
    private ArrayList<SSMap<K>> mapper;

    public ArrayList<String> getPaths() {
        return paths;
    }

    public SSSegmentsInfo FindSegments(K key) {
        var i = LeftBinarySearch.search(mapper, key);
        var end = 0;
        if (i + 1 != mapper.size()) {
            end = mapper.get(i + 1).getOffset();
        }

        return new SSSegmentsInfo(mapper.get(i).getOffset(), end);
    }

//    public int FindSegmentsIndex(K key) {
//        var index = LeftBinarySearch.search(mapper, key);
//        return mapper.get(index).getOffset();
//    }
}
