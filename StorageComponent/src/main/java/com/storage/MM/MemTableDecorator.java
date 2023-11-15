package com.storage.MM;

import com.storage.Entities.KVPair;

import java.io.IOException;
import java.util.ArrayList;

public class MemTableDecorator<K extends Comparable<K>, V> implements IMemTable<K, V> {

    private final MemTable<K, V> manager; // переделать дерево так, чтобы хранились наши пары + переписать гет
    private final IWal wal;

    public MemTableDecorator(MemTable<K, V> manager, IWal wal) throws IOException {
        this.wal = wal;
        this.manager = manager;
        for (KVPair<K, V> pair : wal.<K, V>getAll()) {
            manager.set(pair);
        }
    }

    @Override
    public V get(K key) {
        return manager.get(key);
    }

    @Override
    public boolean set(KVPair<K, V> pair) throws IOException {
        var flag = manager.set(pair);
        wal.set(pair);
        return flag;
    }

    @Override
    public void clear() {
        wal.clear();
        manager.clear();
    }

    @Override
    public int size() {
        return manager.size();
    }

    @Override
    public ArrayList<KVPair<K, V>> getAll() {
        return manager.getAll();
    }
}
