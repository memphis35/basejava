package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    private Comparator<Resume> comparator = Comparator.comparing(Resume::getFullName);

    public void save(Resume resume) throws StorageException {
        Object searchKey = getSearchKey(resume.getUuid());
        if (!isExistKey(searchKey)) {
            saveToStorage(searchKey, resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    public void update(Resume resume) throws NotExistException {
        if (checkKey(resume.getUuid())) {
            updateToStorage(getSearchKey(resume.getUuid()), resume);
        }
    }

    public Resume get(String uuid) {
        if (checkKey(uuid)) return getResume(getSearchKey(uuid));
        return null;
    }

    public void delete(String uuid) {
        if (checkKey(uuid)) {
            deleteFromStorage(getSearchKey(uuid));
        }
    }

    public List<Resume> getAllSorted() {
        List<Resume> result = getAll();
        result.sort(comparator);
        return result;
    }

    private boolean checkKey(String uuid) throws NotExistException {
        Object key = getSearchKey(uuid);
        if (isExistKey(key)) {
            return true;
        } else {
            throw new NotExistException(uuid);
        }
    }

    public abstract List<Resume> getAll();

    protected abstract void saveToStorage(Object searchKey, Resume resume);

    protected abstract void updateToStorage(Object searchKey, Resume resume);

    protected abstract Resume getResume(Object searchKey);

    protected abstract boolean isExistKey(Object searchKey);

    protected abstract void deleteFromStorage(Object searchKey);

    public abstract Object getSearchKey(String uuid);
}