package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import com.storage.service.Interfaces.IWal;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL implements IWal {
    public FileSystem fs;
    private String pairSep;
    private String pairEnd;
    private String fileName;

    @Override
    public <K extends Comparable<K>, V> void set(KVPair<K, V> pair) throws IOException {
        String str = pair.getKey() + pairSep + pair.getValue() + pairEnd;
        fs.write(str, fileName);
    }

    @Override
    public <K extends Comparable<K>, V> ArrayList<KVPair<K, V>> getAll() throws IOException {
        ArrayList<KVPair<K, V>> list = new ArrayList<>();
        String line = fs.read(fileName);
        String[] pairs = line.split(pairEnd);
        for (String pair : pairs) {
            String[] values = pair.split(pairSep);

            if (values.length >= 2) {
                K key = (K) values[0]; // так плохо делать, надо будет что-нибудь придумать
                V value = (V) values[1]; // так плохо делать, надо будет что-нибудь придумать
                list.add(new KVPair<K, V>(key, value));
            }
        }
        return list;
    }

    @Override
    public void clear() {
        fs.clearFile(fileName);
    }
}
