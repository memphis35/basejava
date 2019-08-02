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
    public void saveToArray(Resume resume) {
        int i = -getIndex(resume.getUuid());
        System.arraycopy(storage, i - 1, storage, i, currentSize - i + 1);
        storage[i - 1] = resume;
        System.out.println("Saved.");
        currentSize++;
    }

    /**
     * Delete existing Resume from array storage by UUID number
     *
     * @param i index of resume
     */
    public void deleteFromArray(int i) {
        System.arraycopy(storage, i + 1, storage, i, currentSize - 1 - i);
        currentSize--;
        System.out.println("Resume #" + storage[i].getUuid() + " successfully deleted.");
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
