package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToArray(Object key, Resume resume) {
        storage.put((String) key, resume);
    }

    @Override
    protected void updateResumeToStorage(Object key, Resume resume) {
        storage.replace((String) key, resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return storage.get(key);
    }

    @Override
    protected boolean isExistKey(String uuid) {
        Iterator it = storage.keySet().iterator();
        while (it.hasNext()) {
            if (it.next().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void deleteFromArray(Object key) {
        storage.remove(key);
    }

    @Override
    public Object getKey(String uuid) {
        return uuid;
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }
}

