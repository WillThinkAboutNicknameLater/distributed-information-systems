package ru.nsu.fit;

import ru.nsu.fit.stax.StaxStreamProcessor;
import ru.nsu.fit.util.MapSorter;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class OpenStreetMapParser {
    private final String osmFilepath;

    public OpenStreetMapParser(String osmFilepath) {
        this.osmFilepath = osmFilepath;
    }

    private InputStream openInputStream() throws IOException {
        InputStream inputStream = Files.newInputStream(Paths.get(osmFilepath));
        return new BufferedInputStream(inputStream);
    }

    public Map<String, Long> getNumberOfEditsPerUser() throws XMLStreamException, IOException {
        try (StaxStreamProcessor streamProcessor = new StaxStreamProcessor(openInputStream())) {
            Map<String, Long> statistics = new HashMap<>();

            while (streamProcessor.walk("node")) {
                String username = streamProcessor.getAttribute("user");
                Long currentNumberOfEdits = statistics.getOrDefault(username, 0L);
                statistics.put(username, currentNumberOfEdits + 1);
            }

            return MapSorter.sortByValue(statistics, Comparator.reverseOrder());
        }
    }

    public Map<String, Long> getNumberOfNodesPerKey() throws XMLStreamException, IOException {
        try (StaxStreamProcessor streamProcessor = new StaxStreamProcessor(openInputStream())) {
            Map<String, Long> statistics = new HashMap<>();

            while (streamProcessor.walk("node")) {
                while (streamProcessor.walk("tag", "node")) {
                    String key = streamProcessor.getAttribute("k");
                    Long currentNumberOfEdits = statistics.getOrDefault(key, 0L);
                    statistics.put(key, currentNumberOfEdits + 1);
                }
            }

            return MapSorter.sortByValue(statistics, Comparator.reverseOrder());
        }
    }
}
