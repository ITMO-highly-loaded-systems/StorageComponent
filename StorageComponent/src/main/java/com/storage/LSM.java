package com.storage;

import com.storage.MM.Interfaces.IMemTable;
import com.storage.SS.SSManager;
import com.storage.utils.DBOperations;
import com.storage.Entities.KVPair;
import com.storage.utils.IMerger;

import java.io.IOException;

public class LSM<K extends Comparable<K>, V> implements DBOperations<K, V>, IMerger {
    private final IMemTable<K, V> mmManager;
    private final SSManager<K, V> ssManager;

    public LSM(IMemTable<K, V> mmManager, SSManager<K, V> ssManager) {
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
            mmManager.clear();
            mmManager.set(pair);
            ssManager.createTable(pairs);
        }
    }



    @Override
    public void merge() throws IOException {
        ssManager.merge();
    }
}
