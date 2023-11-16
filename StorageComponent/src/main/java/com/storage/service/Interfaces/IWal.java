package com.storage.service.Interfaces;

import com.storage.Entities.KVPair;

import java.io.IOException;
import java.util.ArrayList;

public interface IWal {
    <K extends Comparable<K>, V> void set(KVPair<K, V> pair) throws IOException;

    <K extends Comparable<K>, V> ArrayList<KVPair<K, V>> getAll() throws IOException;

    void clear();
}
