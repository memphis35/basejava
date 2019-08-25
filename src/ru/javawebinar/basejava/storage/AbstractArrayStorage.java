package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {
    private final static int MAX_SIZE = 10_000;
    protected int currentSize = 0;
    protected Resume[] storage = new Resume[MAX_SIZE];

    /**
     * Clear the array storage
     */
    @Override //from storage interface
    public void clear() {
        Arrays.fill(storage, 0, currentSize, null);
        currentSize = 0;
        System.out.println("Storage successfully cleared.");
    }

    public void saveToArray(int index, Resume resume) throws StorageException {
        if (currentSize >= MAX_SIZE) {
            throw new StorageException("Storage overloaded.", resume.getUuid());
        } else if (index < 0) {
            saveToMainArray(index, resume);
            currentSize++;
        } else {
            throw new ExistException(resume.getUuid());
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

    protected abstract void saveToMainArray(int index, Resume resume);

    public abstract void deleteFromArray(int index);
}
