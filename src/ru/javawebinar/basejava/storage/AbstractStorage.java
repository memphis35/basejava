package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Comparator;
import java.util.List;

public abstract class AbstractStorage implements Storage {

    private Comparator<Resume> comparator = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);

    public void save(Resume resume) {
        Object searchKey = getSearchKey(resume.getUuid());
        if (!isExistKey(searchKey)) {
            saveToStorage(searchKey, resume);
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    public void update(Resume resume) {
        updateToStorage(checkKey(resume.getUuid()), resume);
    }

    public Resume get(String uuid) {
        return getResume(checkKey(uuid));
    }

    public void delete(String uuid) {
        deleteFromStorage(checkKey(uuid));
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        resumes.sort(comparator);
        return resumes;
    }

    private Object checkKey(String uuid) {
        Object searchKey = getSearchKey(uuid);
        if (isExistKey(searchKey)) {
            return searchKey;
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