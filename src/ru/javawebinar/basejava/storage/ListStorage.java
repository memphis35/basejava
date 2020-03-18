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
    protected void saveToStorage(Object index, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateToStorage(Object index, Resume resume) {
        storage.set((int) index, resume);
    }

    @Override
    protected void deleteFromStorage(Object index) {
        storage.remove((int) index);
    }

    @Override
    protected Resume getResume(Object index) {
        return storage.get((int) index);
    }

    @Override
    protected boolean isExistKey(Object index) {
        return (int) index >= 0;
    }

    @Override
    public List<Resume> getAll() {
        return storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
