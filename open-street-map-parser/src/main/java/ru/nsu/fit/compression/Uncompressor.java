package ru.nsu.fit.compression;

import java.io.IOException;

public interface Uncompressor {
    void uncompress(String inputFilepath, String outputFilepath) throws IOException;
}
