package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractArrayStorage extends AbstractStorage {
    private final static int MAX_SIZE = 10_000;
    protected int currentSize = 0;
    protected Resume[] storage = new Resume[MAX_SIZE];

    @Override
    public void clear() {
        Arrays.fill(storage, 0, currentSize, null);
        currentSize = 0;
        System.out.println("Storage successfully cleared.");
    }

    public void saveToArray(Object searchKey, Resume resume) throws StorageException {
        if (currentSize >= MAX_SIZE) {
            throw new StorageException("Storage overloaded.", resume.getUuid());
        } else {
            doSave((int) searchKey, resume);
            currentSize++;
        }
    }

    public void updateResumeToStorage(Object searchKey, Resume resume) {
        storage[(int) searchKey] = resume;
    }

    public Resume getResume(Object searchKey) {
        return storage[(int) searchKey];
    }

    public int size() {
        return currentSize;
    }

    public void deleteFromArray(Object searchKey) {
        doDelete((int) searchKey);
        storage[currentSize - 1] = null;
        currentSize--;
    }

    protected boolean isExistKey(Object searchKey) {
        return (int) searchKey >= 0;
    }

    public List<Resume> getBox() {
        return Arrays.asList(Arrays.copyOf(storage, size()));
    }

    protected abstract void doDelete(int index);

    protected abstract void doSave(int index, Resume resume);
}
