package com.storage.SS;

import com.storage.Entities.KVPair;
import com.storage.SS.Interfaces.ISSManager;
import com.storage.SS.Interfaces.ISSTableNameGenerator;
import com.storage.service.SsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class SSManager<K extends Comparable<K>, V> implements ISSManager<K, V> {
    private final ArrayList<SSTable<K>> tables;
    private final ISSTableNameGenerator nameGenerator;
    private final SsService service;

    public SSManager(SSTableNameGenerator nameGenerator, SsService service) {
        this.nameGenerator = nameGenerator;
        this.service = service;
        tables = new ArrayList<>();
        var fileNames = service.getFilesNameWithPrefix("SSTable");
        for(var file: fileNames)
        {
            var mapper = service.<K, V>restoreSSMap(file);
            var table = new SSTable<K>(file, mapper);
            this.tables.add(table);
        }

    }

    @Override
    public SSTable<K> createTable(ArrayList<KVPair<K, V>> pairs) throws IOException {
        var fileName = nameGenerator.getName();
        return createTable(pairs, fileName);
    }

    private SSTable<K> createTable(ArrayList<KVPair<K, V>> pairs, String fileName) throws IOException {
        var newTable = new SSTable<K>(fileName);
        newTable.setMapper(service.set(pairs, fileName));
        tables.add(newTable);
        return newTable;
    }

    @Override
    public V get(K key) {
        var i = tables.size();
        SSSegmentInfo segmentInfo = null;
        while (i > 0 && segmentInfo == null) {
            --i;
            segmentInfo = tables.get(i).FindSegments(key);
        }
        if (segmentInfo == null) return null;
        return service.<K, V>get(key, segmentInfo.offset(), tables.get(i).getFileName()).getValue();
    }

    @Override
    public void merge() throws IOException {
        var arrays = new ArrayList<ArrayList<KVPair<K, V>>>();
        for (var table : tables) {
            arrays.add(service.getAll(table.getFileName()));
        }
        if (arrays.size() != 0) {
            var newArray = mergeArrays(arrays);
            clear();
            var newTable = createTable(newArray);
            tables.add(newTable);
        }
    }

    private ArrayList<KVPair<K, V>> mergeArrays(ArrayList<ArrayList<KVPair<K, V>>> arrays) {
        var tablesCount = arrays.size();
        var resultList = new ArrayList<KVPair<K, V>>();
        var queue = new PriorityQueue<DataForPriorityQueue<K, V>>();
        for (int i = 0; i < tablesCount; ++i) {
            queue.add(new DataForPriorityQueue<>(arrays.get(i).get(0), i, 0));
        }
        var currentMaxArrayNumber = -1;
        while (!queue.isEmpty()) {
            var min = queue.remove();

            if (!resultList.isEmpty() && resultList.get(resultList.size() - 1).equals(min.getPair())) {
                if (currentMaxArrayNumber < min.getArrayNumber()) {
                    resultList.set(resultList.size() - 1, min.getPair());
                    currentMaxArrayNumber = min.getArrayNumber();
                }
            } else {
                resultList.add(min.getPair());
                currentMaxArrayNumber = min.getArrayNumber();
            }
            min.setIndex(min.getIndex() + 1);
            if (min.getIndex() < arrays.get(min.getArrayNumber()).size()) {
                min.setPair(arrays.get(min.getArrayNumber()).get(min.getIndex()));
                queue.add(min);
            }
        }
        return resultList;
    }

    private void clear() {
        for (var table : tables) {
            service.clear(table.getFileName());
        }
        tables.clear();
    }
}
