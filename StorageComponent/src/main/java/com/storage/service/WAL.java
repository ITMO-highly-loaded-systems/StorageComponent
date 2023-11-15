package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL {
    public FileSystem fs;
    private String pairSep;
    private String pairEnd;

    public void set(KVPair pair, String FileName) throws IOException {
        String str = pair.getKey() + pairSep + pair.getValue() + pairEnd;
        fs.write(str, FileName);
    }

    public KVPair get(String key, String FileName) throws IOException {
        ArrayList<KVPair> list = new ArrayList<>();
        String line = fs.read(FileName);
        String[] pairs = line.split(pairEnd);
        for (String pair : pairs) {
            String[] values = pair.split(pairSep);
            if (values[0].equals(key)) {
                return new KVPair(values[0], values[1]);
            }
        }

        return null;
    }

    public ArrayList<KVPair> getAll(String FileName) throws IOException {
        ArrayList<KVPair> list = new ArrayList<>();
        String line = fs.read(FileName);
        String[] pairs = line.split(pairEnd);
        for (String pair : pairs) {
            String[] values = pair.split(pairSep);

            if (values.length >= 2) {
                String key = values[0];
                String value = values[1];
                list.add(new KVPair(key, value));
            }
        }
        return list;
    }

    public void clear(String FileName){
        fs.clearFile(FileName);
    }
}
