package com.storage.FileSystem;

import com.storage.service.Compressor;
import com.storage.service.SSSegmentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;

@Getter
@AllArgsConstructor
public class DiskFileSystem implements FileSystem{
    private File directory;
    private Compressor compressor;

    public void write(String str, String FileName) throws IOException {
        File file = new File(directory, FileName);
        FileWriter writer = null;
        writer = new FileWriter(file, true);
        writer.write(str);
        writer.flush();
    }

    public SSSegmentInfo writeWithCompression(String str, String FileName) throws IOException {
        File file = new File(directory, FileName);
        int off = (int) file.length();
        byte[] compressedData = compressor.compress(str);
        FileOutputStream fos = new FileOutputStream(file, true);
        int size = compressedData.length;
        fos.write(Integer.toString(size).getBytes());
        fos.write(compressedData);
        return new SSSegmentInfo(off, size);
    }

    public String read(String FileName) throws IOException {
        File file = new File(directory, FileName);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.readLine();
    }

    public String readCompressedBlock(int off, String FileName) throws IOException {
        File file = new File(directory, FileName);
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

    public void clearFile(String FileName){
        File file = new File(directory, FileName);
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
