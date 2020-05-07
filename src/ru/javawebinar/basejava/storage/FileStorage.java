package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AbstractFileStorage extends AbstractStorage<File> {

    private File dir;

    AbstractFileStorage(File dir) throws IOException {
        Objects.requireNonNull(dir, "Destination path must not be null");
        if (!dir.canRead() || !dir.canWrite())
            throw new IllegalArgumentException(dir.getCanonicalPath() + "isn't readable/writeable");
        this.dir = dir;
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
            write(new BufferedOutputStream(new FileOutputStream(searchKey)), resume);
        } catch (FileNotFoundException e) {
            throw new StorageException("File not found.", e, searchKey.getName());
        }
    }

    @Override
    protected Resume getResume(File searchKey) {
        try {
            return read(new BufferedInputStream(new FileInputStream(searchKey)));
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
    public List<Resume> getAll() {
        List<Resume> list = new ArrayList<>();
        for (File f : checkNullValue()) {
            list.add(getResume(f));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File f : checkNullValue()) {
            deleteFromStorage(f);
        }
    }

    @Override
    public int size() {
        return checkNullValue().length;
    }

    private File[] checkNullValue() {
        if (dir.listFiles() != null) return dir.listFiles();
        throw new StorageException("Object have null value.", null);
    }

    @Override
    public File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    protected abstract void write(BufferedOutputStream out, Resume resume);

    protected abstract Resume read(BufferedInputStream in);

}
