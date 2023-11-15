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
    public Compressor compressor;

    public void write(String str) throws IOException {
        byte[] compressedData = compressor.compress(str);
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(Integer.toString(compressedData.length).getBytes());
        fos.write(compressedData);
    }

    public String read(int off) throws IOException {
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
