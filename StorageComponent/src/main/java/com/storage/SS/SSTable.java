package com.storage.SS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SSTable<K extends Comparable<K>> {
    public SSTable(String paths) {
        this.fileName = paths;
    }

    public SSTable(String paths, ArrayList<SSMap<K>> mapper) {
        this.fileName = paths;
        this.mapper = mapper;
    }
    private final String fileName;
    //    private HashMap<K, SSSegmentInfo> mapper;
    private ArrayList<SSMap<K>> mapper;

    public String getFileName() {
        return fileName;
    }

    //    public void setMapper(HashMap<K, SSSegmentInfo> mapper) {
//        this.mapper = mapper;
//    }
    public void setMapper(ArrayList<SSMap<K>> mapper) {
        this.mapper = mapper;
    }

    public SSSegmentInfo FindSegments(K key) {
//        return mapper.get(key);
        var i = Collections.binarySearch(mapper, key);
        if (i < 0) {
            i *= -1;
            i -= 1;
        } else {
            return mapper.get(i).getSegment();
        }
        if (i == 0) return null;
        return mapper.get(i - 1).getSegment();
    }

}
