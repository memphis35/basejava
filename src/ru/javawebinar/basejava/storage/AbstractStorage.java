package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {

    public void save(Resume resume) throws StorageException {
        int index = getIndex(resume.getUuid());
        if (index < 0) {
            saveToArray(index, resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    protected abstract void saveToArray(int index, Resume resume);

    public void update(Resume resume) throws NotExistException {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateResumeToStorage(index, resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    abstract void updateResumeToStorage(int index, Resume resume);

    public Resume get(String uuid) throws NotExistException {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getResume(index);
        } else {
            throw new NotExistException(uuid);
        }
    }

    abstract Resume getResume(int index);

    public void delete(String uuid) throws NotExistException {
        int index = getIndex(uuid);
        if (index >= 0) {
            deleteFromArray(index);
        } else {
            throw new NotExistException(uuid);
        }
    }

    protected abstract void deleteFromArray(int index);

    public abstract int getIndex(String uuid);
}