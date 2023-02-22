package ru.nsu.fit.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.Objects;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StaxStreamProcessor(InputStream inputStream) throws XMLStreamException {
        reader = FACTORY.createXMLStreamReader(inputStream);
    }

    @Override
    public void close() throws XMLStreamException {
        if (Objects.nonNull(reader)) {
            reader.close();
        }
    }

    public boolean walk(String element, String parent) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();

            if (Objects.nonNull(parent) && event == XMLStreamConstants.END_ELEMENT && parent.equals(reader.getLocalName())) {
                return false;
            }

            if (event == XMLStreamConstants.START_ELEMENT && element.equals(reader.getLocalName())) {
                return true;
            }
        }

        return false;
    }

    public boolean walk(String element) throws XMLStreamException {
        return walk(element, null);
    }

    public String getAttribute(String name) {
        return reader.getAttributeValue(null, name);
    }
}
