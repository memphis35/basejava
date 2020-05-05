package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamPathStorage extends AbstractPathStorage {
    public ObjectStreamPathStorage(String dir) {
        super(dir);
    }

    @Override
    void write(FileOutputStream fileOut, Resume resume) {
        try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(resume);
        } catch (IOException e) {
            throw new StorageException("Storage write error", e, resume.getUuid());
        }
    }

    @Override
    Resume read(FileInputStream fileIn) {
        try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
            return (Resume) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Storage read error.", e, null);
        }
    }
}
