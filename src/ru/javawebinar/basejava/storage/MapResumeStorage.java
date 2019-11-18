package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage {
    private Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void saveToArray(Object key, Resume resume) {
        storage.put(resume.getUuid(), resume);
    }

    @Override
    protected void updateResumeToStorage(Object key, Resume resume) {
        storage.replace(resume.getUuid(), resume);
    }

    @Override
    protected Resume getResume(Object key) {
        return storage.get(key.toString());
    }

    @Override
    protected boolean isExistKey(Object key) {
        return storage.containsValue(key);
    }

    @Override
    protected void deleteFromArray(Object key) {
        storage.remove(key.toString());
    }

    @Override
    public Object getKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public List<Resume> getAllSorted() {
        Resume[] array = storage.values().toArray(new Resume[size()]);
        List<Resume> list = Arrays.asList(array);
        list.sort(super.comparator);
        return list;
    }

    @Override
    public int size() {
        return storage.size();
    }
}

