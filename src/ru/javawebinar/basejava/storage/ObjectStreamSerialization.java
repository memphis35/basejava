package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSerialization implements SerializationStrategy {

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
