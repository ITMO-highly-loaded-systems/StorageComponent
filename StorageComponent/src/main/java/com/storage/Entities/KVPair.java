package com.storage.Entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;



public class KVPair<K, V> {

    public K key;
    private V value;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

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
