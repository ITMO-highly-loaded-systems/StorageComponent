package com.storage.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public interface Compressor {
    byte[] compress(String str) throws IOException;
    String decompress(byte[] compressedData) throws IOException;
}
