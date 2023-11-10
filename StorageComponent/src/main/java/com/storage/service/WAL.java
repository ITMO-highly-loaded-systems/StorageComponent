package com.storage.service;

import com.storage.Entities.KVPair;
import lombok.AllArgsConstructor;

import java.io.*;
import java.util.ArrayList;

@AllArgsConstructor
public class WAL {
    public File file;

    public void write(KVPair pair) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, true);
            writer.write(pair.getKey() + "," + pair.getValue() + ";");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KVPair get(String key) {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            String[] pairs = line.split(";");
            for (String pair : pairs) {
                String[] values = pair.split(",");
                if (values[0].equals(key)) {
                    return new KVPair(values[0], values[1]);
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<KVPair> getAll() {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));
            String line = reader.readLine();
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void clear(){
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
