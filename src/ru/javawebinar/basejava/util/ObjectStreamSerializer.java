package ru.javawebinar.basejava.util;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSerializer implements Serializer {

    @Override
    public void write(OutputStream fileOut, Resume resume) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(resume);
        }
    }

        @Override
        public Resume read (InputStream fileIn) throws IOException {
            try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
                return (Resume) in.readObject();
            } catch (ClassNotFoundException e) {
                throw new StorageException("Class not found", e, null);
            }
        }
    }
