package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) throws StorageException {
        if (!isExistKey(resume.getUuid())) {
            saveToArray(getKey(resume.getUuid()), resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    protected abstract void saveToArray(Object key, Resume resume);

    public void update(Resume resume) throws NotExistException {
        if (isExistKey(resume.getUuid())) {
            updateResumeToStorage(getKey(resume.getUuid()), resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    public Resume get(String uuid) throws NotExistException {
        if (isExistKey(uuid)) {
            return getResume(getKey(uuid));
        } else {
            throw new NotExistException(uuid);
        }
    }

    public void delete(String uuid) throws NotExistException {
        if (isExistKey(uuid)) {
            deleteFromArray(getKey(uuid));
        } else {
            throw new NotExistException(uuid);
        }
    }

    protected abstract void updateResumeToStorage(Object key, Resume resume);

    protected abstract Resume getResume(Object key);

    protected abstract boolean isExistKey(String uuid);

    protected abstract void deleteFromArray(Object key);

    public abstract Object getKey(String uuid);
}