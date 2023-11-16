package com.storage;

import com.storage.MM.MemTable;
import com.storage.SS.SSManager;
import com.storage.utils.DBOperations;
import com.storage.Entities.KVPair;

import java.io.IOException;

public class LSM<K extends Comparable<K>, V> implements DBOperations<K,V> {
    private final MemTable<K, V> mmManager;
    private final SSManager<K, V> ssManager;

    public LSM(MemTable<K, V> mmManager, SSManager<K, V> ssManager) {
        this.mmManager = mmManager;
        this.ssManager = ssManager;
    }

    public V get(K key) {
        var value = mmManager.get(key);
        if (value == null) {
            value = ssManager.get(key);
        }
        return value;
    }

    public void set(KVPair<K, V> pair) throws IOException {
        if (!mmManager.set(pair)) {
            var pairs = mmManager.getAll();
            ssManager.createTable(pairs);
            mmManager.clear();
            mmManager.set(pair);
        }
    }
}
