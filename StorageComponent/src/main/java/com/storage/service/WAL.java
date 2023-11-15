package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.MM.IWal;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL implements IWal {
    public File file;

    public <K, V> void write(KVPair<K, V> pair) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            writer.write(pair.getKey() + "," + pair.getValue() + ";");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, V> KVPair<K, V> get(K key) {
        ArrayList<KVPair<K, V>> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] pairs = line.split(";");
            for (String pair : pairs) {
                String[] values = pair.split(",");
                if (values[0].equals(key)) {
                    return new KVPair<K, V>((K) values[0], (V) values[1]); // так делать плохо
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <K, V> ArrayList<KVPair<K, V>> getAll() {
        ArrayList<KVPair<K, V>> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
            String line = reader.readLine();
            String[] pairs = line.split(";");
            for (String pair : pairs) {
                String[] values = pair.split(",");

                if (values.length >= 2) {
                    K key = (K) values[0]; // так делать плохо
                    V value = (V) values[1]; // так делать плохо
                    list.add(new KVPair<>(key, value));
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, false);
            writer.write("");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
