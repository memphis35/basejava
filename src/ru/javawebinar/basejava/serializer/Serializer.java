package ru.javawebinar.basejava.serializer;

import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

    void write(OutputStream out, Resume resume) throws IOException;

    Resume read(InputStream in) throws IOException;
}
