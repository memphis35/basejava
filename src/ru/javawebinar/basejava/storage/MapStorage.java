package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapStorage extends AbstractStorage<String> {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToStorage(String searchKey, Resume resume) {
        storage.put(searchKey, resume);
    }

    @Override
    protected void updateToStorage(String searchKey, Resume resume) {
        storage.replace(searchKey, resume);
    }

    @Override
    protected Resume getResume(String searchKey) {
        return storage.get(searchKey);
    }

    @Override
    protected boolean isExistKey(String searchKey) {
        return storage.containsKey(searchKey);
    }

    @Override
    protected void deleteFromStorage(String searchKey) {
        storage.remove(searchKey);
    }

    @Override
    public String getSearchKey(String uuid) {
        return uuid;
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

