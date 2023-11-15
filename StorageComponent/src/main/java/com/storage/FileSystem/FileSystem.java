package com.storage.FileSystem;

import java.io.*;

public interface FileSystem {
    void write(String str) throws IOException;

    void writeWithCompression(String str) throws IOException;

    String read() throws IOException;

    String readCompressedBlock(int off) throws IOException;

    void clearFile();
}
