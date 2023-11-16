package com.storage.SS;

import java.util.HashMap;

public class SSTable<K extends Comparable<K>> {
    public SSTable(String paths) {
        this.fileName = paths;
    }

    private final String fileName;
    private HashMap<K, SSSegmentInfo> mapper;

    public String getFileName() {
        return fileName;
    }

    public void setMapper(HashMap<K, SSSegmentInfo> mapper) {
        this.mapper = mapper;
    }

    public SSSegmentInfo FindSegments(K key) {
        return mapper.get(key);
    }

}
