package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private File destination;

    public AbstractFileStorage(File destination) throws IOException {
        Objects.requireNonNull(destination, "Destination path must not be null");
        if (!destination.canRead() || !destination.canWrite())
            throw new RuntimeException(destination.getCanonicalPath() + "isn't readable/writeable");
        this.destination = destination;
    }

    @Override
    public List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File f : Objects.requireNonNull(destination.listFiles())) {
            list.add(read(f));
        }
        return list;
    }

    @Override
    protected void saveToStorage(File searchKey, Resume resume){
        write(searchKey, resume);
    }

    @Override
    protected void updateToStorage(File searchKey, Resume resume) {
        write(searchKey, resume);
    }

    @Override
    protected Resume getResume(File searchKey) {
        return read(searchKey);
    }

    @Override
    protected boolean isExistKey(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected void deleteFromStorage(File searchKey) {
        searchKey.delete();
    }

    @Override
    public void clear() {
        for (File f : Objects.requireNonNull(destination.listFiles())) {
            f.delete();
        }
    }

    @Override
    public int size() {
        return destination.listFiles().length;
    }

    protected abstract void write(File newFile, Resume resume);

    protected abstract Resume read(File searchKey);

}
