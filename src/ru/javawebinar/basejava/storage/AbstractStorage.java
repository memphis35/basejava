package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {
    Comparator<? super Resume> comparator = (Comparator<Resume>) (o1, o2) -> o1.compareTo(o2);

    public void save(Resume resume) throws StorageException {
        Object searchKey = getSearchKey(resume.getUuid());
        if (!isExistKey(searchKey)) {
            saveToArray(searchKey, resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    public void update(Resume resume) throws NotExistException {
        Object key = getSearchKey(resume.getUuid());
        if (isExistKey(key)) {
            updateResumeToStorage(key, resume);
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    public Resume get(String uuid) throws NotExistException {
        Object key = getSearchKey(uuid);
        if (isExistKey(key)) {
            return getResume(key);
        } else {
            throw new NotExistException(uuid);
        }
    }

    public void delete(String uuid) throws NotExistException {
        Object key = getSearchKey(uuid);
        if (isExistKey(key)) {
            deleteFromArray(key);
        } else {
            throw new NotExistException(uuid);
        }
    }

    public List<Resume> getAllSorted() {
        List<Resume> result = getBox();
        result.sort(comparator);
        return result;
    }

    public abstract List<Resume> getBox();

    protected abstract void saveToArray(Object searchKey, Resume resume);

    protected abstract void updateResumeToStorage(Object searchKey, Resume resume);

    protected abstract Resume getResume(Object searchKey);

    protected abstract boolean isExistKey(Object searchKey);

    protected abstract void deleteFromArray(Object searchKey);

    public abstract Object getSearchKey(String uuid);
}