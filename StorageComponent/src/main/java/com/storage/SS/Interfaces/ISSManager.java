package com.storage.SS.Interfaces;

import com.storage.Entities.KVPair;
import com.storage.SS.SSTable;

import java.io.IOException;
import java.util.ArrayList;

public interface ISSManager<K extends Comparable<K>, V> {

    SSTable<K> createTable(ArrayList<KVPair<K, V>> pairs) throws IOException;

    V get(K key);

    void merge() throws IOException;
}
