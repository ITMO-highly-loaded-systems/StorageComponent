package com.storage.service;

import com.storage.Entities.KVPair;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;

@Getter
@AllArgsConstructor
public class FileSystem{
    public File file;
    public int blockSize;
    public Compressor compressor;

    private void write(String str) throws IOException {
        byte[] compressedData = compressor.compress(str);
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(Integer.toString(compressedData.length).getBytes());
        fos.write(compressedData);
    }

    private String read(int off) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        fis.skip(off);
        byte[] b = new byte[2];
        fis.read(b, 0, 2);
        int len = Integer.parseInt(new String(b));
        byte[] compressedData = new byte[len];
        fis.read(compressedData, 0, len);
        String str = compressor.decompress(compressedData);
        return str;
    }

    public void set(ArrayList<KVPair> pairs) throws IOException {
        int count = 0;
        String str = "";
        for (KVPair pair:pairs) {
            if(count == blockSize){
                write(str);
                str = "";
                count = 0;
            }
            count++;
            str = str.concat(pair.getKey() + "," + pair.getValue() + ";");
        }
        write(str);
    }
    public KVPair get(String key, int off) {
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            String str = read(off);
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
        String filePath = file.getPath();
        ArrayList<KVPair> list = new ArrayList<>();

        try {
            String str = read(off);
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

    public void clearFile(){
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
