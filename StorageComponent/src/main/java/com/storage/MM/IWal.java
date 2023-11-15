package com.storage.MM;

import com.storage.Entities.KVPair;

public interface IWal {
    <K,V> void log(KVPair<K,V> pair);
    void clear();

}
