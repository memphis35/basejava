package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    /**
     * Save resume object in array storage
     *
     * @param resume current resume
     */
    @Override
    public void save(Resume resume) {
        int i = -getIndex(resume.getUuid());
        if (currentSize >= MAX_SIZE) {
            System.out.println("Storage overloaded.");
        }
        if (i > 0) {
            System.arraycopy(storage, i - 1, storage, i, currentSize - i + 1);
            storage[i - 1] = resume;
            System.out.println("Saved.");
            currentSize++;
        } else {
            System.out.println("Resume already exist");
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
        return Arrays.binarySearch(storage, 0, currentSize, new Resume(uuid));
    }
}
