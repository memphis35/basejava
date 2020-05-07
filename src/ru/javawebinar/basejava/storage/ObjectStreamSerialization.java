package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamSerialization implements SerializationStrategy {

    @Override
    public void write(BufferedOutputStream fileOut, Resume resume) {
        try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(resume);
        } catch (IOException e) {
            throw new StorageException("Resume write error", e, null);
        }
    }

    @Override
    public Resume read(BufferedInputStream fileIn) {
        try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Resume) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Resume read error", e, null);
        }
    }
}
