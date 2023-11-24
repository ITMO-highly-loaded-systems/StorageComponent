package com.storage.service.Interfaces;

import com.storage.Entities.KVPair;
import com.storage.SS.SSMap;
import com.storage.SS.SSSegmentInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public interface ISSService {
    <K extends Comparable<K>,V> ArrayList<SSMap<K>> set(ArrayList<KVPair<K, V>> pairs, String FileName) throws IOException;

    <K extends Comparable<K>,V> KVPair<K,V> get(K key, int off, String FileName);

    <K extends Comparable<K>,V> ArrayList<KVPair<K,V>> getAll(String FileName);

    void clear(String FileName);
}
