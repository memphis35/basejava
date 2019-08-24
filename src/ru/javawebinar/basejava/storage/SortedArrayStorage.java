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
    public void saveToArray(int index, Resume resume) {
        int i = -index - 1;
        System.arraycopy(storage, i, storage, i + 1, currentSize - i);
        storage[i] = resume;
    }

    /**
     * Delete existing Resume from array storage by UUID number
     *
     * @param index index of resume
     */
    public void deleteFromArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, currentSize - index - 1);
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