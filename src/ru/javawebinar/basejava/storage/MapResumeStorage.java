package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToArray(Object searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateResumeToStorage(Object searchKey, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return storage.get(searchKey.toString());
    }

    @Override
    protected boolean isExistKey(Object searchKey) {
        return storage.containsValue(searchKey);
    }

    @Override
    protected void deleteFromArray(Object searchKey) {
        storage.remove(searchKey.toString());
    }

    @Override
    public Object getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public List<Resume> getBox() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public int size() {
        return storage.size();
    }
}

