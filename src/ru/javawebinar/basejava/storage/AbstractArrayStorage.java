package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

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

    public void saveToArray(int index, Resume resume) throws StorageException {
        if (currentSize >= MAX_SIZE) {
            throw new StorageException("Storage overloaded.", resume.getUuid());
        } else {
            saveToMainArray(index, resume);
            currentSize++;
        }
    }

    public void updateResumeToStorage(int index, Resume resume) {
        storage[index] = resume;
    }

    public Resume getResume(int index) {
        return storage[index];
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, currentSize);
    }

    public int size() {
        return currentSize;
    }

    public void deleteFromArray(int index) {
        deleteFromMainArray(index);
        storage[currentSize - 1] = null;
        currentSize--;
    }

    protected abstract void deleteFromMainArray(int index);

    protected abstract void saveToMainArray(int index, Resume resume);
}
