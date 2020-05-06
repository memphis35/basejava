package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

public interface Strategy {

    void write(BufferedOutputStream newFile, Resume resume);

    Resume read(BufferedInputStream fileIn);
}
