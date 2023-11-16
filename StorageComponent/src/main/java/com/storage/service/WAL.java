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
                K key = (K)values[0]; // так плохо делать, надо будет что-нибудь придумать
                V value = (V)values[1]; // так плохо делать, надо будет что-нибудь придумать
                list.add(new KVPair<K,V>(key, value));
            }
        }
        return list;
    }

    public void clear(String FileName){
        fs.clearFile(FileName);
    }
}
