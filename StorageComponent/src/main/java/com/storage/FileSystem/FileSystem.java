package com.storage.FileSystem;
import java.io.*;
import java.util.ArrayList;

import com.storage.SS.SSSegmentInfo;

public interface FileSystem {
    void write(String str, String FileName) throws IOException;

    SSSegmentInfo writeWithCompression(String str, int bitCount, String fileName) throws IOException;

    String read(String FileName) throws IOException;

    String readCompressedBlock(int off, int bitCount, String fileName) throws IOException;

    int readSegmentSize(int off, int bitCount, String FileName) throws IOException;

    void clearFile(String FileName);

    public void deleteFile(String fileName);

    ArrayList<File> GetFilesForPathByPrefix(String prefix);
}
