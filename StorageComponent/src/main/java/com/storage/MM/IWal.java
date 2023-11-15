package com.storage.MM;

import com.storage.Entities.KVPair;

import java.io.IOException;
import java.util.ArrayList;

public interface IWal {
    <K, V> void set(KVPair<K, V> pair) throws IOException;

    void clear();

    <K, V> ArrayList<KVPair<K, V>> getAll() throws IOException;

}
