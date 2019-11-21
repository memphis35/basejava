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
    protected void saveToArray(Object searchKey, Resume resume) {
        storage.add(resume);
    }

    @Override
    protected void updateResumeToStorage(Object searchKey, Resume resume) {
        storage.set((int) searchKey, resume);
    }

    @Override
    protected void deleteFromArray(Object searchKey) {
        storage.remove((int) searchKey);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get((int) searchKey);
    }

    @Override
    protected boolean isExistKey(Object searchKey) {
        return (int) searchKey >= 0;
    }

    @Override
    public List<Resume> getBox() {
        return this.storage;
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public Object getSearchKey(String uuid) {
        for (int index = 0; index < storage.size(); index++) {
            if (storage.get(index).getUuid().equals(uuid)) {
                return index;
            }
        }
        return -1;
    }
}
