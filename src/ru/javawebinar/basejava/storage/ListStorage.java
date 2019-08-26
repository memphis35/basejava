package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.model.Resume;

import java.util.*;

public class ListStorage extends AbstractStorage {
    private List<Resume> storage = new ArrayList<>();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void saveToArray(int index, Resume resume) {
        if (index < 0) {
            storage.add(resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    @Override
    void updateResumeToStorage(int index, Resume resume) {
        storage.set(index, resume);
    }

    @Override
    protected void deleteFromArray(int index) {
        storage.remove(index);
    }

    @Override
    Resume getResume(int index) {
        return storage.get(index);
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
    public int getIndex(String uuid) {
        int i = 0;
        int index = -1;
        while (i < storage.size()) {
            if (storage.get(i).getUuid().equals(uuid)) {
                index = i;
            }
            i++;
        }
        return index;
    }
}
