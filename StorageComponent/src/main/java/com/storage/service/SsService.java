package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import com.storage.SS.SSMap;
import com.storage.SS.SSSegmentInfo;
import com.storage.service.Interfaces.ISSService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class SsService implements ISSService {
    private FileSystem fs;
    private int blockSize;
    private String pairSep;
    private String pairEnd;

    @Override
    public <K extends Comparable<K>,V> ArrayList<SSMap<K>> set(ArrayList<KVPair<K, V>> pairs, String FileName) throws IOException {
        var map = new ArrayList<SSMap<K>>();
        String str = "";
        ArrayList<KVPair<K,V>> block = new ArrayList<>();
        for (KVPair<K,V> pair:pairs) {
            if(block.size() == blockSize){
                var segmentInfo = fs.writeWithCompression(str, FileName);
                map.add(new SSMap<K>(block.get(0).getKey(), segmentInfo));
                str = "";
                block.clear();
            }
            block.add(pair);
            str = str.concat(pair.getKey() + pairSep + pair.getValue() + pairEnd);
        }
        if(!str.isEmpty()) {
            SSSegmentInfo segmentInfo = fs.writeWithCompression(str, FileName);
            map.add(new SSMap<K>(block.get(0).getKey(), segmentInfo));
        }
        return map;
    }
    public <K extends Comparable<K>,V> void set(KVPair<K,V> pair, String FileName) throws IOException {
        String str = pair.getKey() + pairSep + pair.getValue() + pairEnd;
        fs.writeWithCompression(str, FileName);
    }

    @Override
    public <K extends Comparable<K>,V> KVPair<K,V> get(K key, int off, String FileName) {
        try {
            String str = fs.readCompressedBlock(off, FileName);
            String[] pairs = str.split(pairEnd);
            for (String pair : pairs) {
                String[] values = pair.split(pairSep);
                if (values[0].equals(key)) {
                    return new KVPair<K,V>((K) values[0], (V) values[1]); // так плохо делать, надо будет что-нибудь придумать
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K extends Comparable<K>,V> ArrayList<KVPair<K,V>> getAll(String FileName) {
        ArrayList<KVPair<K,V>> list = new ArrayList<>();
        var off = 0;

        try {
            String str = fs.readCompressedBlock(off, FileName);
            String[] pairs = str.split(pairEnd);
            for (String pair : pairs) { // этот кусок кода есть и в wal, надо вынести
                String[] values = pair.split(pairSep);

                if (values.length >= 2) {
                    K key = (K)values[0]; // так плохо делать, надо будет что-нибудь придумать
                    V value = (V)values[1]; // так плохо делать, надо будет что-нибудь придумать
                    list.add(new KVPair<K,V>(key, value));
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K extends Comparable<K>,V> ArrayList<SSMap<K>> restoreSSMap(String FileName) {
        int off = 0;
        var map = new ArrayList<SSMap<K>>();
        try {
            while(true){
                int size = fs.readSegmentSize(off, FileName);
                if (size == 0){
                    break;
                }
                String str = fs.readCompressedBlock(off, FileName);
                String[] pairs = str.split(pairEnd);
                String[] firstPair = pairs[0].split(pairSep);
                K key = (K)firstPair[0];
                map.add(new SSMap<K>(key, new SSSegmentInfo(off, size)));
                off += size + (int)Math.log10(size) + 1;
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clear(String FileName){
        fs.clearFile(FileName);
    }
}
