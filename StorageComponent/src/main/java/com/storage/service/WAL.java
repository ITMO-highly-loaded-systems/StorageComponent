package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL {
    public FileSystem fs;

    public void set(KVPair pair) throws IOException {
        String str = pair.getKey() + "," + pair.getValue() + ";";
        fs.write(str);
    }

    public KVPair get(String key) throws IOException {
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

    public ArrayList<KVPair> getAll() throws IOException {
        ArrayList<KVPair> list = new ArrayList<>();
        String line = fs.read();
        String[] pairs = line.split(";");
        for (String pair : pairs) {
            String[] values = pair.split(",");

            if (values.length >= 2) {
                String key = values[0];
                String value = values[1];
                list.add(new KVPair(key, value));
            }
        }
        return list;
    }
}
