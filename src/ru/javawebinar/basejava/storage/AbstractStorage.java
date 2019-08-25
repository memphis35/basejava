package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    /**
     * Saving current resume to array.
     *
     * @param resume current resume
     * @throws StorageException if resume not found.
     */
    public void save(Resume resume) throws StorageException {
        int index = getIndex(resume.getUuid());
        try {
            saveToArray(index, resume);
        } catch (StorageException s) {
            System.out.println(s.getMessage());
        }
    }

    protected abstract void saveToArray(int index, Resume resume);

    /**
     * Replace old resume to current resume inside the array.
     *
     * @param resume current resume
     * @throws NotExistException if resume not found.
     */
    public void update(Resume resume) throws NotExistException {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            updateResumeToStorage(index, resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    abstract void updateResumeToStorage(int index, Resume resume);

    /**
     * Return current resume from array.
     *
     * @param uuid UUID of current resume.
     * @return current resume
     * @throws NotExistException if resume not found.
     */
    public Resume get(String uuid) throws NotExistException {
        int index = getIndex(uuid);
        if (index >= 0) {
            return getResume(index);
        } else {
            throw new NotExistException(uuid);
        }
    }

    abstract Resume getResume(int index);

    /**
     * Remove current resume from array.
     *
     * @param uuid UUID of current resume.
     * @throws NotExistException if resume not found.
     */
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