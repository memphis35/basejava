package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    @Override
    public void saveToMainArray(int index, Resume resume) {
        storage[currentSize] = resume;
    }

    @Override
    public void deleteFromMainArray(int index) {
        storage[index] = storage[currentSize - 1];
    }

    @Override
    public Object getKey(String uuid) {
        for (int i = 0; i < currentSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}