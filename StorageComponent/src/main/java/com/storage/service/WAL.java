package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.MM.IWal;
import com.storage.FileSystem.FileSystem;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL implements IWal {
    public FileSystem fs;

    public <K,V>void set(KVPair<K,V> pair) throws IOException {
        String str = pair.getKey() + "," + pair.getValue() + ";";
        fs.write(str);
    }

    public <K,V> KVPair<K,V> get(String key) throws IOException {
        ArrayList<KVPair> list = new ArrayList<>();
        String line = fs.read();
        String[] pairs = line.split(";");
        for (String pair : pairs) {
            String[] values = pair.split(",");
            if (values[0].equals(key)) {
                return new KVPair(values[0], values[1]);
            }
        }

        return null;
    }

    public <K,V> ArrayList<KVPair<K,V>> getAll() throws IOException {
        ArrayList<KVPair<K,V>> list = new ArrayList<>();
        String line = fs.read();
        String[] pairs = line.split(";");
        for (String pair : pairs) {
            String[] values = pair.split(",");

            if (values.length >= 2) {
                K key = (K)values[0]; // так плохо делать, надо будет что-нибудь придумать
                V value = (V)values[1]; // так плохо делать, надо будет что-нибудь придумать
                list.add(new KVPair<K,V>(key, value));
            }
        }
        return list;
    }
}
