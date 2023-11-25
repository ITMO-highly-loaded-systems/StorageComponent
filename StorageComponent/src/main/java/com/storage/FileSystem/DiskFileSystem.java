package com.storage.FileSystem;

import com.storage.SS.SSSegmentInfo;
import com.storage.service.Interfaces.Compressor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.*;

@Getter
@AllArgsConstructor
public class DiskFileSystem implements FileSystem {
    private File directory;
    private Compressor compressor;

    @Override
    public void write(String str, String fileName) throws IOException {
        File file = new File(directory, fileName);
        FileWriter writer;
        writer = new FileWriter(file, true);
        writer.write(str);
        writer.flush();
        writer.close();
    }

    @Override

    public SSSegmentInfo writeWithCompression(String str, int bitCount, String fileName) throws IOException {
        File file = new File(directory, fileName);
        int off = (int) file.length();
        byte[] compressedData = compressor.compress(str);
        FileOutputStream fos = new FileOutputStream(file, true);
        int size = compressedData.length;
        str = "0".repeat(bitCount-Integer.toString(size).length()) + size;
        fos.write(str.getBytes());
        fos.write(compressedData);
        fos.close();
        return new SSSegmentInfo(off, size);
    }

    @Override
    public String read(String fileName) throws IOException {
        File file = new File(directory, fileName);
        if (!file.exists()) return "";
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return reader.readLine();
    }

    @Override
    public String readCompressedBlock(int off, int bitCount, String fileName) throws IOException {
        File file = new File(directory, fileName);
        FileInputStream fis = new FileInputStream(file);
        fis.skip(off);
        byte[] b = new byte[bitCount];
        fis.read(b, 0, bitCount);
        int len = Integer.parseInt(new String(b));
        byte[] compressedData = new byte[len];
        fis.read(compressedData, 0, len);
        String str = compressor.decompress(compressedData);
        fis.close();
        return str;
    }

    @Override
    public int readSegmentSize(int off, int bitCount, String fileName) throws IOException{
        File file = new File(directory, fileName);
        if(file.length() <= off) {
            return 0;
        }
        FileInputStream fis = new FileInputStream(file);
        fis.skip(off);
        byte[] b = new byte[bitCount];
        fis.read(b, 0, bitCount);
        int len = Integer.parseInt(new String(b));
        fis.close();
        return len;
    }

    @Override
    public void clearFile(String fileName){
        File file = new File(directory, fileName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file, false);
            writer.write("");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
