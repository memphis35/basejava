package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage extends AbstractArrayStorage {

    /**
     * Save resume object in array storage
     *
     * @param resume current resume
     */
    @Override
    public void saveToArray(Resume resume) {
        storage[currentSize] = resume;
        System.out.println("Resume successfully saved.");
        currentSize++;
    }

    @Override
    public void deleteFromArray(int i) {
        storage[i] = storage[currentSize - 1];
        storage[currentSize - 1] = null;
        currentSize--;
    }

    /**
     * Searching for a existing resume by UUID number
     *
     * @param uuid UUID number
     * @return true if resume exist
     */

    @Override
    public int getIndex(String uuid) {
        for (int i = 0; i < currentSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}