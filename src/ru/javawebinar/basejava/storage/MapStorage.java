package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;

import java.util.HashMap;

public abstract class MapStorage extends AbstractStorage {
    private HashMap<String, Resume> storage = new HashMap<>();

    public void clear() {
        storage.clear();
    }

    public void save(Resume resume) {
        if (!storage.containsValue(resume)) {
            storage.put(resume.getUuid(), resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }
    public void update(Resume resume) {
        if (storage.containsValue(resume)) {
            storage.replace(resume.getUuid(), resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    public Resume get(String uuid) {
        if (storage.containsKey(uuid)) {
            return storage.get(uuid);
        } else {
            throw new NotExistException(uuid);
        }
    }

    public void delete(String uuid) {
        if (storage.containsKey(uuid)) {
            storage.remove(uuid);
        } else {
            throw new NotExistException(uuid);
        }
    }

    public Resume[] getAll() {
        return storage.values().toArray(new Resume[0]);
    }

    public int size() {
        return storage.size();
    }
}
