package com.storage.SS;

import com.storage.Entities.KVPair;

public class DataForPriorityQueue<K extends Comparable<K>, V> implements Comparable<DataForPriorityQueue<K, V>> {
    public DataForPriorityQueue(KVPair<K, V> kvPair, int arrayNumber, int iterator) {
        pair = kvPair;
        this.arrayNumber = arrayNumber;
        this.index = iterator;
    }

    public KVPair<K, V> getPair() {
        return pair;
    }

    public void setPair(KVPair<K, V> pair) {
        this.pair = pair;
    }

    private KVPair<K, V> pair;
    private int arrayNumber;

    public int getArrayNumber() {
        return arrayNumber;
    }

    public void setArrayNumber(int arrayNumber) {
        this.arrayNumber = arrayNumber;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    int index;

    @Override
    public int compareTo(DataForPriorityQueue<K, V> o) {
        return this.pair.getKey().compareTo(o.pair.getKey());
    }
}
