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
    protected boolean isExistKey(String uuid) {
        return (int) getKey(uuid) >= 0;
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
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
