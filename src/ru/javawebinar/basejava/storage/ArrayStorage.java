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
    public void save(Resume resume) {
        if (currentSize >= MAX_SIZE) {
            System.out.println("Storage overloaded.");
        } else if (getIndex(resume.getUuid()) == -1) {
            storage[currentSize] = resume;
            System.out.println("Resume successfully saved.");
            currentSize++;
        } else {
            System.out.println("Resume already exist");
        }
    }

    public void delete(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            storage[i] = storage[currentSize - 1];
            storage[currentSize - 1] = null;
            currentSize--;
            System.out.println("Resume #" + uuid + " successfully deleted.");
        } else {
            System.out.println("Resume #" + uuid + " not found.");
        }
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