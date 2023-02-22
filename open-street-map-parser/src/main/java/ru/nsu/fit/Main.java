package ru.nsu.fit;

import lombok.extern.slf4j.Slf4j;
import ru.nsu.fit.compression.BZip2Uncompressor;
import ru.nsu.fit.compression.Uncompressor;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            log.error("Invalid number of parameters. Parameters: [bzip2Filepath]");
            return;
        }

        String bzip2Filepath = args[0];

        try {
            Instant startTime = Instant.now();

            File osmFile = File.createTempFile(bzip2Filepath.split("\\.")[0], ".osm");
            osmFile.deleteOnExit();

            String osmFilepath = osmFile.getAbsolutePath();
            log.debug("OSM filepath: {}", osmFilepath);

            Uncompressor bzip2Uncompressor = new BZip2Uncompressor();
            bzip2Uncompressor.uncompress(bzip2Filepath, osmFilepath);

            OpenStreetMapParser parser = new OpenStreetMapParser(osmFilepath);

            Map<String, Long> numberOfEditsPerUser = parser.getNumberOfEditsPerUser();
            log.debug(numberOfEditsPerUser.toString());

            Map<String, Long> numberOfNodesPerKey = parser.getNumberOfNodesPerKey();
            log.debug(numberOfNodesPerKey.toString());

            Instant finishTime = Instant.now();
            log.debug("Time: {} ms", Duration.between(startTime, finishTime).toMillis());
        } catch (IOException | XMLStreamException e) {
            log.error(e.getMessage());
        }
    }
}
