package com.storage.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KVPair<?, ?> kvPair = (KVPair<?, ?>) o;
        return Objects.equals(key, kvPair.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
