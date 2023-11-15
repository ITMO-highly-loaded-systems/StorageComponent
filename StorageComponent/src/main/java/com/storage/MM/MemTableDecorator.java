package com.storage.MM;

import com.storage.Entities.KVPair;

import java.util.ArrayList;

public class MemTableDecorator<K extends Comparable<K>, V> implements IMemTable<K, V> {

    private final MemTable<K, V> manager;
    private final IWal logger;

    public MemTableDecorator(MemTable<K, V> manager, IWal logger, String logFile) {
        this.logger = logger;
        this.manager = manager;
//        log = парсинг файла(logFile)
//        for (var p: log) {
//            manager.set(p)
//        }
    }



    public MemTableDecorator(MemTable<K, V> manager, IWal logger) {
        this.manager = manager;
        this.logger = logger;
    }

    @Override
    public V get(K key) {
        return manager.get(key);
    }

    @Override
    public boolean set(KVPair<K, V> pair) {
        var flag = manager.set(pair);
        logger.log(pair);
        return flag;
    }

    @Override
    public void clear() {
        logger.clear();
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
