package com.storage.Entities;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KVPair<K, V> {

    private Integer id;
    public K key;
    private V value;

    public KVPair() {
    }

    public KVPair(K key, V value) throws NullPointerException {
        if (key == null) throw new NullPointerException();
        if (value == null) throw new NullPointerException();

        this.key = key;
        this.value = value;
    }


}
