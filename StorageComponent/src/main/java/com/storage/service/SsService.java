package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
@AllArgsConstructor
public class SsService {
    private FileSystem fs;
    private int blockSize;
    private String pairSep;
    private String pairEnd;

    public HashMap<String, SSSegmentInfo> set(ArrayList<KVPair> pairs, String FileName) throws IOException {
        HashMap<String, SSSegmentInfo> map = new HashMap<>();
        String str = "";
        ArrayList<KVPair> block = new ArrayList<>();
        for (KVPair pair:pairs) {
            if(block.size() == blockSize){
                SSSegmentInfo segmentInfo = fs.writeWithCompression(str, FileName);
                map.put(block.get(0).getKey(), segmentInfo);
                str = "";
                block.clear();
            }
            block.add(pair);
            str = str.concat(pair.getKey() + pairSep + pair.getValue() + pairEnd);
        }
        if(!str.isEmpty()) {
            SSSegmentInfo segmentInfo = fs.writeWithCompression(str, FileName);
            map.put(block.get(0).getKey(), segmentInfo);
        }
        return map;
    }
    public void set(KVPair pair, String FileName) throws IOException {
        String str = pair.getKey() + pairSep + pair.getValue() + pairEnd;
        fs.writeWithCompression(str, FileName);
    }

    public KVPair get(String key, int off, String FileName) {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            String str = fs.readCompressedBlock(off, FileName);
            String[] pairs = str.split(pairEnd);
            for (String pair : pairs) {
                String[] values = pair.split(pairSep);
                if (values[0].equals(key)) {
                    return new KVPair(values[0], values[1]);
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<KVPair> getAll(int off, String FileName) {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            String str = fs.readCompressedBlock(off, FileName);
            String[] pairs = str.split(pairEnd);
            for (String pair : pairs) {
                String[] values = pair.split(pairSep);

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

    public void clear(String FileName){
        fs.clearFile(FileName);
    }
}
