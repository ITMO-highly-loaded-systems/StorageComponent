package com.storage.service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressor implements Compressor{
    public byte[] compress(String str) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(outputStream);

        gzipOutputStream.write(str.getBytes(StandardCharsets.UTF_8));
        gzipOutputStream.close();

        byte[] compressedData = outputStream.toByteArray();
        outputStream.close();

        return compressedData;
    }

    public String decompress(byte[] compressedData) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(compressedData);
        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = gzipInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        gzipInputStream.close();
        inputStream.close();
        outputStream.close();

        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
