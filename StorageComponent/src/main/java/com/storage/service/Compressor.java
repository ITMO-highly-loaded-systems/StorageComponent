package com.storage.service;

import java.io.*;

public interface Compressor {
    byte[] compress(String str) throws IOException;
    String decompress(byte[] compressedData) throws IOException;
}
