package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    public void doSave(int index, Resume resume) {
        int i = -index - 1;
        System.arraycopy(storage, i, storage, i + 1, currentSize - i);
        storage[i] = resume;
    }

    @Override
    public void doDelete(int index) {
        System.arraycopy(storage, index + 1, storage, index, currentSize - index - 1);
    }

    @Override
    public Object getSearchKey(String uuid) {
        Resume searchKey = new Resume(uuid, null);
        return Arrays.binarySearch(storage, 0, currentSize, searchKey);
    }
}