package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void save(Resume resume){
        if (!storage.contains(resume)) {
            storage.add(resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    @Override
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage.set(index, resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    @Override
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage.get(index);
        } else {
            throw new NotExistException("uuid");
        }
    }

    @Override
    public void delete(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            storage.remove(index);
        } else {
            throw new NotExistException(uuid);
        }
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[0]);
    }

    @Override
    public int size() {
        return storage.size();
    }

    private int getIndex(String uuid) {
        Iterator<Resume> iterator = storage.iterator();
        int index = -1;
        while (iterator.hasNext()) {
            Resume search = iterator.next();
            if (search.getUuid().equals(uuid)) {
                index = storage.indexOf(search);
            }
        }
        return index;
    }
}
