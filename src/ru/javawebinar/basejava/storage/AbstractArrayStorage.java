package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    private final static int MAX_SIZE = 10_000;
    int currentSize = 0;
    protected Resume[] storage = new Resume[MAX_SIZE];

    @Override
    public void clear() {
        Arrays.fill(storage, 0, currentSize, null);
        currentSize = 0;
        System.out.println("Storage successfully cleared.");
    }

    public void saveToStorage(Object index, Resume resume) throws StorageException {
        if (currentSize >= MAX_SIZE) {
            throw new StorageException("Storage overloaded.", resume.getUuid());
        } else {
            doSave((int) index, resume);
            currentSize++;
        }
    }

    public void updateToStorage(Object index, Resume resume) {
        storage[(int) index] = resume;
    }

    public Resume getResume(Object index) {
        return storage[(int) index];
    }

    public int size() {
        return currentSize;
    }

    public void deleteFromStorage(Object index) {
        doDelete((int) index);
        storage[currentSize - 1] = null;
        currentSize--;
    }

    protected boolean isExistKey(Object index) {
        return (int) index >= 0;
    }

    public List<Resume> getAll() {
        return Arrays.asList(Arrays.copyOf(storage, size()));
    }

    protected abstract void doDelete(int index);

    protected abstract void doSave(int index, Resume resume);
}
