package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void saveToArray(Object key, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateResumeToStorage(Object key, Resume resume) {
        storage.set((int) key, resume);
    }

    @Override
    protected void deleteFromArray(Object key) {
        storage.remove((int) key);
    }

    @Override
    protected Resume getResume(Object key) {
        return storage.get((int) key);
    }

    @Override
    protected boolean isExistKey(Object key) {
        return (int) key >= 0;
    }

    @Override
    public List<Resume> getAllSorted() {
        this.storage.sort(super.comparator);
        return this.storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public Object getKey(String uuid) {
        for (int index = 0; index < storage.size(); index++) {
            if (storage.get(index).getUuid().equals(uuid)) {
                return index;
            }
        }
        return -1;
    }
}
