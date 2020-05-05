package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    protected File dir;

    public AbstractFileStorage(File dir) throws IOException {
        Objects.requireNonNull(dir, "Destination path must not be null");
        if (!dir.canRead() || !dir.canWrite())
            throw new RuntimeException(dir.getCanonicalPath() + "isn't readable/writeable");
        this.dir = dir;
    }

    @Override
    public List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            try {
                list.add(read(new FileInputStream(f)));
            } catch (IOException e) {
                throw new StorageException("File read error", e, f.getName());
            }
        }
        return list;
    }

    @Override
    protected void saveToStorage(File searchKey, Resume resume) {
        try {
            searchKey.createNewFile();
            updateToStorage(searchKey, resume);
        } catch (IOException e) {
            throw new StorageException("File write error.", e, searchKey.getName());
        }
    }

    @Override
    protected void updateToStorage(File searchKey, Resume resume) {
        try {
            write(new FileOutputStream(searchKey), resume);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found.", e, searchKey.getName());
        }
    }

    @Override
    protected Resume getResume(File searchKey) {
        try {
            return read(new FileInputStream(searchKey));
        } catch (IOException e) {
            throw new StorageException("File read error", e, searchKey.getName());
        }
    }

    @Override
    protected boolean isExistKey(File searchKey) {
        return searchKey.exists();
    }

    @Override
    protected void deleteFromStorage(File searchKey) {
        if (!searchKey.delete()) {
            throw new StorageException("Can't find file to delete.", searchKey.getName());
        }
    }

    @Override
    public void clear() {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            deleteFromStorage(f);
        }
    }

    @Override
    public int size() {
        return Objects.requireNonNull(dir.listFiles()).length;
    }

    @Override
    public File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    protected abstract void write(FileOutputStream newFile, Resume resume);

    protected abstract Resume read(FileInputStream searchKey);

}
