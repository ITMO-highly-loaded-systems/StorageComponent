package com.storage.MM;

import com.storage.Entities.KVPair;

import java.util.ArrayList;

public interface IWal {
    <K, V> void write(KVPair<K, V> pair);

    void clear();

    <K, V> ArrayList<KVPair<K, V>> getAll();

}
