package ru.javawebinar.basejava.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class XMLParser {
    private final Marshaller marshaller;
    private final Unmarshaller unmarshaller;

    public XMLParser(Class ... classes) {
        try {
            JAXBContext context = JAXBContext.newInstance(classes);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public void marshall(Object instance, Writer writer) {
        try {
            if (instance instanceof LocalDate) {
                String date = ((LocalDate) instance).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
                marshaller.marshal(date, writer);
            } else {
                marshaller.marshal(instance, writer);
            }
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public <T> T unmarshall(Reader reader) {
        try {
            return (T) unmarshaller.unmarshal(reader);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
