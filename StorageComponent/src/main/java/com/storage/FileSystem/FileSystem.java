package com.storage.FileSystem;

import com.storage.service.SSSegmentInfo;

import java.io.*;
import java.util.HashMap;

public interface FileSystem {
    void write(String str, String FileName) throws IOException;

    SSSegmentInfo writeWithCompression(String str, String FileName) throws IOException;

    String read(String FileName) throws IOException;

    String readCompressedBlock(int off, String FileName) throws IOException;

    void clearFile(String FileName);
}
