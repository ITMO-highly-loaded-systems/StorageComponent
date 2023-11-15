package com.storage.SS;

import java.util.ArrayList;
import java.util.HashMap;

public class SSTable<K extends Comparable<K>> {
    public SSTable(String paths) {
        this.path = paths;
    }

    private String path;
    private HashMap<K, SSSegmentInfo> mapper;

    public String getPath() {
        return path;
    }

    public void setMapper(HashMap<K, SSSegmentInfo> mapper) {
        this.mapper = mapper;
    }

    public SSSegmentInfo FindSegments(K key) {
        return mapper.get(key);
    }

}
