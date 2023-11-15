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

    public void set(ArrayList<KVPair> pairs) throws IOException {
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
    public void set(KVPair pair) throws IOException {
        String str = pair.getKey() + "," + pair.getValue() + ";";
        fs.write(str);
    }

    public KVPair get(String key, int off) {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            String str = fs.readCompressedBlock(off);
            String[] pairs = str.split(";");
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

    public ArrayList<KVPair> getAll(int off) {
        ArrayList<KVPair> list = new ArrayList<>();

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
