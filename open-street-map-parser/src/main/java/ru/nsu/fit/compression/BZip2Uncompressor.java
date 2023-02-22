package ru.nsu.fit.compression;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BZip2Uncompressor implements Uncompressor {
    private static final int BUFFER_SIZE = 4096;

    @Override
    public void uncompress(String inputFilepath, String outputFilepath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(inputFilepath));
             BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
             BZip2CompressorInputStream compressorInputStream = new BZip2CompressorInputStream(bufferedInputStream);
             OutputStream outputStream = Files.newOutputStream(Paths.get(outputFilepath))) {
            final byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = compressorInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, n);
            }
        }
    }
}
