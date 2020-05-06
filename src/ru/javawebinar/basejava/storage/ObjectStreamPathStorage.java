package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.*;

class ObjectStreamPathStorage extends AbstractPathStorage {

    private ObjectStreamFileStorage storage;

    ObjectStreamPathStorage(String dir) throws IOException {
        super(dir);
        storage = new ObjectStreamFileStorage(new File(dir));
    }

    @Override
    void write(BufferedOutputStream out, Resume resume) {
        storage.write(out, resume);
    }

    @Override
    Resume read(BufferedInputStream in) {
        return storage.read(in);
    }
}
