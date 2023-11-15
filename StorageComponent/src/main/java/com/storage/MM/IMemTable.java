package com.storage.MM;

import com.storage.Entities.KVPair;

import java.io.IOException;
import java.util.ArrayList;

public interface IMemTable<K extends Comparable<K>, V> {
    V get(K key);

    boolean set(KVPair<K, V> pair) throws IOException;

    void clear();

    int size();

    ArrayList<KVPair<K, V>> getAll();
}
