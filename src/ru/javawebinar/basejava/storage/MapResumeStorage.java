package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapResumeStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(Object searchKey, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateToStorage(Object searchKey, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object searchKey) {
        return (Resume) searchKey;
    }

    @Override
    protected boolean isExistKey(Object searchKey) {
        return searchKey != null;
    }

    @Override
    protected void deleteFromStorage(Object searchKey) {
        storage.remove(searchKey.toString());
    }

    @Override
    public Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public List<Resume> getAll() {
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

