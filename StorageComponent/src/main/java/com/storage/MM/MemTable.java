package com.storage.MM;

import com.storage.Entities.KVPair;
import com.storage.MM.Interfaces.IMemTable;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class MemTable<K extends Comparable<K>, V> implements IMemTable<K, V> {

    private final TreeMap<K, V> tree;
    private final int maxSize;
    public MemTable(int maxSize) {
        this.maxSize = maxSize;
        tree = new TreeMap<>();
    }

    @Override
    public V get(K key) {
        return tree.get(key);
    }

    @Override
    public boolean set(KVPair<K, V> pair) {
        tree.remove(pair.getKey());
        if (size() == maxSize) return false;
        tree.put(pair.getKey(), pair.getValue());
        return true;
    }

    @Override
    public void clear() {
        tree.clear();
    }

    @Override
    public int size() {
        return tree.size();
    }

    @Override
    public ArrayList<KVPair<K, V>> getAll() {
        var pairs = new ArrayList<KVPair<K, V>>(size());
        for (Map.Entry<K, V> entry : tree.entrySet()) {
            pairs.add(new KVPair<>(entry.getKey(), entry.getValue()));
        }
        return pairs;
    }

}
