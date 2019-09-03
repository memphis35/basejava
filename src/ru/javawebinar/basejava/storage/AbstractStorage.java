package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) throws StorageException {
        Object key = getKey(resume.getUuid());
        if (!isExistKey(key)) {
            saveToArray(key, resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    protected abstract void saveToArray(Object key, Resume resume);

    public void update(Resume resume) throws NotExistException {
        Object key = getKey(resume.getUuid());
        if (isExistKey(key)) {
            updateResumeToStorage(key, resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    public Resume get(String uuid) throws NotExistException {
        Object key = getKey(uuid);
        if (isExistKey(key)) {
            return getResume(key);
        } else {
            throw new NotExistException(uuid);
        }
    }

    public void delete(String uuid) throws NotExistException {
        Object key = getKey(uuid);
        if (isExistKey(key)) {
            deleteFromArray(key);
        } else {
            throw new NotExistException(uuid);
        }
    }

    protected abstract void updateResumeToStorage(Object key, Resume resume);

    protected abstract Resume getResume(Object key);

    protected abstract boolean isExistKey(Object key);

    protected abstract void deleteFromArray(Object key);

    public abstract Object getKey(String uuid);
}