package com.storage.utils;


import com.storage.Entities.KVPair;

public interface DBOperations<K,V> {
    public void set(KVPair<K,V> pair);
    public V get(K key);
}
