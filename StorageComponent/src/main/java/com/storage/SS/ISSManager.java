package com.storage.SS;

import com.storage.Entities.KVPair;

import java.util.ArrayList;

public interface ISSManager<K, V> {

    void createTable(ArrayList<KVPair<K, V>> pairs);

    V get(K key);

    void Merge();
}
