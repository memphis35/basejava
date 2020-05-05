package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;

public class ObjectStreamFileStorage extends AbstractFileStorage {
    ObjectStreamFileStorage(File dir) throws IOException {
        super(dir);
    }

    @Override
    protected void write(FileOutputStream newFile, Resume resume) {
        try (ObjectOutputStream out = new ObjectOutputStream(newFile)) {
            out.writeObject(resume);
        } catch (IOException e) {
            System.out.println(e.getClass());
            throw new StorageException("Resume write error", e, null);
        }
    }

    @Override
    protected Resume read(FileInputStream searchKey) {
        try (ObjectInputStream in = new ObjectInputStream(searchKey)) {
            return (Resume) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new StorageException("Resume read error", e, null);
        }
    }
}
