package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class SsService {
    public FileSystem fs;
    public int blockSize;

    public <K,V> void set(ArrayList<KVPair<K,V>> pairs) throws IOException {
        int count = 0;
        String str = "";
        for (KVPair pair:pairs) {
            if(count == blockSize){
                fs.writeWithCompression(str);
                str = "";
                count = 0;
            }
            count++;
            str = str.concat(pair.getKey() + "," + pair.getValue() + ";");
        }
        if(!str.equals("")) {
            fs.writeWithCompression(str);
        }
    }
    public <K,V> void set(KVPair<K,V> pair) throws IOException {
        String str = pair.getKey() + "," + pair.getValue() + ";";
        fs.write(str);
    }

    public <K,V>KVPair get(K key, int off) {
        ArrayList<KVPair<K,V>> list = new ArrayList<>();

        try {
            String str = fs.readCompressedBlock(off);
            String[] pairs = str.split(";");
            for (String pair : pairs) {
                String[] values = pair.split(",");
                if (values[0].equals(key)) {
                    return new KVPair<K,V>((K)values[0], (V)values[1]); // так плохо делать, надо будет что-нибудь придумать
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <K,V> ArrayList<KVPair<K,V>> getAll(int off) {
        ArrayList<KVPair<K,V>> list = new ArrayList<>();

        try {
            String str = fs.readCompressedBlock(off);
            String[] pairs = str.split(";");
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
}
