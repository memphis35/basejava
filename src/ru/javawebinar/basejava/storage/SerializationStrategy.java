package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public interface SerializationStrategy {

    void write(BufferedOutputStream newFile, Resume resume);

    Resume read(BufferedInputStream fileIn);
}
