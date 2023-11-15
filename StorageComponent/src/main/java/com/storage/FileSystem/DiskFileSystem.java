package com.storage.FileSystem;

import com.storage.service.Compressor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;

@Getter
@AllArgsConstructor
public class DiskFileSystem implements FileSystem{
    public File file;
    public Compressor compressor;

    public void write(String str) throws IOException {
        FileWriter writer = null;
        writer = new FileWriter(file, true);
        writer.write(str);
        writer.flush();
    }

    public void writeWithCompression(String str) throws IOException {
        byte[] compressedData = compressor.compress(str);
        FileOutputStream fos = new FileOutputStream(file, true);
        fos.write(Integer.toString(compressedData.length).getBytes());
        fos.write(compressedData);
    }

    public String read() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.readLine();
    }

    public String readCompressedBlock(int off) throws IOException {
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
