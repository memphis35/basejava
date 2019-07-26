package com.urise.webapp.storage;

import com.urise.webapp.model.Resume;

import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final static int MAX_SIZE = 10_000;
    private Resume[] storage = new Resume[MAX_SIZE];
    private int currentSize = 0;

    /**
     * Clear the array storage
     */
    public void clear() {
        Arrays.fill(storage, 0, currentSize, null);
        currentSize = 0;
        System.out.println("Storage successfully cleared.");
    }

    /**
     * Save resume object in array storage
     *
     * @param resume current resume
     */
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

    /**
     * Replace resume in storage by UUID number
     *
     * @param resume incoming resume
     */
    public void update(Resume resume) {
        int i = getIndex(resume.getUuid());
        if (i != -1) {
            storage[i] = resume;
            System.out.println("Resume #" + resume.getUuid() + " successfully updated.");
        } else {
            System.out.println("Resume #" + resume.getUuid() + " not found.");
        }
    }

    /**
     * Get UUID number from Resume object, if it exist and possible
     *
     * @param uuid incoming UUID number
     * @return UUID number of existing Resume
     */
    public Resume get(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            return storage[i];
        } else {
            System.out.println("Resume #" + uuid + " not found.");
        }
        return null;
    }

    /**
     * Delete existing Resume from array storage by UUID number
     *
     * @param uuid incoming UUID number
     */
    public void delete(String uuid) {
        int i = getIndex(uuid);
        if (i != -1) {
            System.arraycopy(storage, i + 1, storage, i, currentSize - 1 - i);
            currentSize--;
            System.out.println("Resume #" + uuid + " successfully deleted.");
        } else {
            System.out.println("Resume #" + uuid + " not found.");
        }
    }

    /**
     * Get all Resume's UUIN numbers, printed to stack
     *
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, currentSize);
    }

    /**
     * Call a current quantity of Resume of array storage
     *
     * @return size of array (quantity of objects)
     */
    public int size() {
        return currentSize;
    }

    /**
     * Searching for a existing resume by UUID number
     *
     * @param uuid UUID number
     * @return true if resume exist
     */
    private int getIndex(String uuid) {
        for (int i = 0; i < currentSize; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}