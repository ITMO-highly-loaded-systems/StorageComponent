package com.storage.SS;

import com.storage.Entities.KVPair;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class DataForPriorityQueue<K extends Comparable<K>, V> implements Comparable<DataForPriorityQueue<K, V>> {
    public DataForPriorityQueue(KVPair<K, V> kvPair, int arrayNumber, int iterator) {
        pair = kvPair;
        this.arrayNumber = arrayNumber;
        this.index = iterator;
    }

    private KVPair<K, V> pair;
    private int arrayNumber;
    int index;

    @Override
    public int compareTo(DataForPriorityQueue<K, V> o) {
        return this.pair.getKey().compareTo(o.pair.getKey());
    }
}
