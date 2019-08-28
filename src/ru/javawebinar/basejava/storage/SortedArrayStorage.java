package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void saveToMainArray(int index, Resume resume) {
        int i = -index - 1;
        System.arraycopy(storage, i, storage, i + 1, currentSize - i);
        storage[i] = resume;
    }

    @Override
    public void deleteFromMainArray(int index) {
        System.arraycopy(storage, index + 1, storage, index, currentSize - index - 1);
    }


    @Override
    public int getIndex(String uuid) {
        return Arrays.binarySearch(storage, 0, currentSize, new Resume(uuid));
    }
}