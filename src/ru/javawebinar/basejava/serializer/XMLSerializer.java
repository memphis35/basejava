package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.XMLParser;

import java.io.*;

public class XMLSerializer implements Serializer {
    private final XMLParser parser;

    public XMLSerializer() {
        parser = new XMLParser(
                Resume.class, StringSection.class, ListSection.class, OrganizationSection.class,
                Link.class, Organization.class, Organization.Position.class);
    }

    @Override
    public void write(OutputStream out, Resume resume) {
        try (BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(out))) {
            parser.marshall(resume, buffer);
        } catch (IOException e) {
            throw new StorageException("Storage write error", e, null);
        }

    }

    @Override
    public Resume read(InputStream in) {
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(in))) {
            return parser.unmarshall(buffer);
        } catch (IOException e) {
            throw new StorageException("Storage read error", e, null);
        }
    }
}
