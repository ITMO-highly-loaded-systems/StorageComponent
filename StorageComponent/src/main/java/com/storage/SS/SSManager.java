package com.storage.SS;

import com.storage.Entities.KVPair;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class SSManager<K extends Comparable<K>, V> implements ISSManager<K, V> {
    private ArrayList<SSTable<K>> tables;
//    private fs;

    @Override
    public void createTable(ArrayList<KVPair<K, V>> pairs) {
        // fileNames.add(сгенерировать имя файла)
//        for (var p:pairs) {
//            var name = fs.write(fileNames.Last, p);// надо обработать внути запись в несуществующий файл(создать и писать) и переполнение файла (создать еще один, а потом вернуть лист из путей)
//            if (fileNames.Last!=name){
//              filenames.add(name)
//            }
//        }
        // разделить на сегменты, заполнить mapper
        // заархивировать сегменты, добавить инфу о размере сегментов в файл
    }

    @Override
    public V get(K key) {
        KVPair<K, V> pair = null;
        for (int i = tables.size() - 1; i >= 0; --i) {
            var segmentsInfo = tables.get(i).FindSegments(key);
            var size = segmentsInfo.end() - segmentsInfo.start();
            if (size < 0) {
                // разархивируем от старта до конца файла
            } else {
                // разархивируем от старта до энда
            }
            //// какой-то костыль, но я не смогла придумать что-то лучше, только если сразу хранить не начало, а начало + размер
            // бин поиском ищем элемент. если нашли, return pair
        }
        return pair.getValue();
    }

    @Override
    public void Merge() {
        ArrayList<ArrayList<KVPair<K, V>>> tables = new ArrayList<>();
        ArrayList<KVPair<K, V>> oldestTable = new ArrayList<>();
        // считать сначала первую таблицу из листа в олдест
        // считать остальные таблицы в лист tables
        // реверснуть этот лист (?)

        var res1 = mergeArrays(tables);
        var res = mergeArrays(res1, oldestTable);
        // создать таблицу по res
        // добавить в лист новую таблицу, остальное удалить
        // удалить файлы прошлых таблиц
    }

    private ArrayList<KVPair<K, V>> mergeArrays(ArrayList<ArrayList<KVPair<K, V>>> tables) {
        var tablesCount = tables.size();
        var resultList = new ArrayList<KVPair<K, V>>();
        var iterators = new int[tablesCount];
        for (var i : iterators) {
            i = 0;
        }
        KVPair<K, V> pairWithMinKey;
        int index = -1;
        while (true) {
            pairWithMinKey = null;
            index = -1;
            for (int i = 0; i < tablesCount; ++i) {
                var pair = tables.get(i).get(iterators[i]);
                if (index < tables.get(i).size()) {
                    while (resultList.size() != 0 && pair.getKey() == resultList.get(resultList.size() - 1).getKey()) {
                        ++iterators[i];
                    }
                    if (pairWithMinKey == null || pair.getKey().compareTo(pairWithMinKey.getKey()) < 0) {
                        pairWithMinKey = pair;
                        index = i;
                    }
                }
            }
            if (index == -1) break;
            resultList.add(pairWithMinKey);
            ++iterators[index];
        }
        return resultList;
    }

    private ArrayList<KVPair<K, V>> mergeArrays(ArrayList<KVPair<K, V>> l1, ArrayList<KVPair<K, V>> l2) {
        var list = new ArrayList<ArrayList<KVPair<K, V>>>();
        list.add(l1);
        list.add(l2);
        return mergeArrays(list);
    }

    private ArrayList<KVPair<K, V>> newMerge(ArrayList<ArrayList<KVPair<K, V>>> tables) {
        var tablesCount = tables.size();
        var resultList = new ArrayList<KVPair<K, V>>();
        var queue = new PriorityQueue<DataForPriorityQueue<K, V>>();
        for (int i = 0; i < tablesCount; ++i) {
            queue.add(new DataForPriorityQueue<>(tables.get(i).get(0), i, 0));
        }
        var curMinArrNum = tablesCount + 1;
        while (!queue.isEmpty()) {
            var min = queue.remove();
            if (resultList.get(resultList.size() - 1).equals(min.getPair())) {
                if (curMinArrNum > min.getArrayNumber())// тут потом проверить, какой знак нужен, взависимости от того, в каком порядке таблицы записаны. Пока считаю, что меньше номер-новее запись
                {
                    resultList.set(resultList.size() - 1, min.getPair());
                    curMinArrNum = min.getArrayNumber();
                }
            } else {
                resultList.add(min.getPair());
                curMinArrNum = min.getArrayNumber();
            }
            min.setIndex(min.getIndex() + 1);
            if (min.getIndex() < tables.get(min.getArrayNumber()).size()) {
                min.setPair(tables.get(min.getArrayNumber()).get(min.getIndex()));
                queue.add(min);
            }
        }
        return resultList;
    }
}
