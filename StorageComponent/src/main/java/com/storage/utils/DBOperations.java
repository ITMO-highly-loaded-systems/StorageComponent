package com.storage.utils;


import com.storage.Entities.KVPair;

import java.io.IOException;

public interface DBOperations<K extends Comparable<K>,V> {
    public void set(KVPair<K,V> pair) throws IOException;
    public V get(K key);
}
