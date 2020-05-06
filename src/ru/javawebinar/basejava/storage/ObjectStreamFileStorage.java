package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamFileStorage extends AbstractFileStorage {
    ObjectStreamFileStorage(File dir) throws IOException {
        super(dir);
    }

    @Override
    protected void write(BufferedOutputStream newFile, Resume resume) {
        try (ObjectOutputStream out = new ObjectOutputStream(newFile)) {
            out.writeObject(resume);
        } catch (IOException e) {
            throw new StorageException("Resume write error", e, null);
        }
    }

    @Override
    protected Resume read(BufferedInputStream searchKey) {
        try (ObjectInputStream in = new ObjectInputStream(searchKey)) {
            return (Resume) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Resume read error", e, null);
        }
    }
}
