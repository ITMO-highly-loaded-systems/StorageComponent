package com.storage.SS;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SSMap<K extends Comparable<K>> implements Comparable<K> {
    private K key;
    private SSSegmentInfo segment;


    public SSSegmentInfo getSegment() {
        return segment;
    }
    public K getKey() {
        return key;
    }

    @Override
    public int compareTo(K o) {
        return key.compareTo(o);
    }
}