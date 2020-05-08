package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface SerializationStrategy {

    void write(OutputStream out, Resume resume) throws IOException;

    Resume read(InputStream in) throws IOException;
}
