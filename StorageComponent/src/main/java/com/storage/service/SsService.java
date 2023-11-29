package com.storage.service;

import com.storage.Entities.KVPair;
import com.storage.FileSystem.FileSystem;
import com.storage.SS.SSMap;
import com.storage.SS.SSSegmentInfo;
import com.storage.service.Interfaces.ISSService;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;

@Getter
public class SsService implements ISSService {
    private FileSystem fs;
    private int blockSize;
    private String pairSep;
    private String pairEnd;
    private int bitCount;

    public SsService(FileSystem fs, int blockSize, String pairSep, String pairEnd) {
        this.fs = fs;
        this.blockSize = blockSize;
        this.pairSep = pairSep;
        this.pairEnd = pairEnd;
        this.bitCount = Integer.toString(blockSize).length() + 1;
    }

    @Override
    public <K extends Comparable<K>, V> ArrayList<SSMap<K>> set(ArrayList<KVPair<K, V>> pairs, String fileName) throws IOException {
        var map = new ArrayList<SSMap<K>>();
        String str = "";
        ArrayList<KVPair<K, V>> block = new ArrayList<>();
        for (KVPair<K, V> pair : pairs) {
            if (block.size() == blockSize) {
                var segmentInfo = fs.writeWithCompression(str, bitCount, fileName);
                map.add(new SSMap<K>(block.get(0).getKey(), segmentInfo));
                str = "";
                block.clear();
            }
            block.add(pair);
            str = str.concat(pair.getKey() + pairSep + pair.getValue() + pairEnd);
        }
        if (!str.isEmpty()) {
            SSSegmentInfo segmentInfo = fs.writeWithCompression(str, bitCount, fileName);
            map.add(new SSMap<K>(block.get(0).getKey(), segmentInfo));
        }
        return map;
    }

    @Override
    public <K extends Comparable<K>, V> KVPair<K, V> get(K key, int off, String fileName) {
        try {
            String str = fs.readCompressedBlock(off, bitCount, fileName);
            String[] pairs = str.split(pairEnd);
            for (String pair : pairs) {
                String[] values = pair.split(pairSep);
                if (values[0].equals(key)) {
                    return new KVPair<K, V>((K) values[0], (V) values[1]); // так плохо делать, надо будет что-нибудь придумать
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K extends Comparable<K>, V> ArrayList<KVPair<K, V>> getAll(String fileName) {
        int off = 0;
        var list = new ArrayList<KVPair<K, V>>();
        try {
            while (true) {
                int size = fs.readSegmentSize(off, bitCount, fileName);
                if (size == 0) {
                    break;
                }
                String str = fs.readCompressedBlock(off, bitCount, fileName);
                String[] pairs = str.split(pairEnd);
                for (String pair : pairs) {
                    String[] values = pair.split(pairSep);
                    list.add(new KVPair<K, V>((K) values[0], (V) values[1]));
                }
                off += size + bitCount;
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <K extends Comparable<K>, V> ArrayList<SSMap<K>> restoreSSMap(String fileName) {
        int off = 0;
        var map = new ArrayList<SSMap<K>>();
        try {
            while (true) {
                int size = fs.readSegmentSize(off, bitCount, fileName);
                if (size == 0) {
                    break;
                }
                String str = fs.readCompressedBlock(off, bitCount, fileName);
                String[] pairs = str.split(pairEnd);
                String[] firstPair = pairs[0].split(pairSep);
                K key = (K) firstPair[0];
                map.add(new SSMap<K>(key, new SSSegmentInfo(off, size)));
                off += size + bitCount;
            }
            return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void clear(String fileName) {
        fs.clearFile(fileName);
        fs.deleteFile(fileName);
    }

    @Override
    public <K extends Comparable<K>, V> ArrayList<String> getFilesNameWithPrefix(String prefix) {
        var names = new ArrayList<String>();
        var files = fs.GetFilesForPathByPrefix("SSTable");
        for (var file : files) {
            var name = file.getName();
            names.add(name);
        }
        return names;
    }
}
