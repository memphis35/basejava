package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public abstract class AbstractStorage<T> implements Storage {

    private Comparator<Resume> comparator = Comparator.comparing(Resume::getFullName).thenComparing(Resume::getUuid);
    private static final Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    public void save(Resume resume) throws IOException {
        T searchKey = getSearchKey(resume.getUuid());
        if (!isExistKey(searchKey)) {
            saveToStorage(searchKey, resume);
            LOG.info("Save " + resume);
        } else {
            LOG.warning(resume + " already exist.");
            throw new ExistException(resume.getUuid());
        }
    }

    public void update(Resume resume) {
        updateToStorage(checkKey(resume.getUuid()), resume);
        LOG.info("Update " + resume);
    }

    public Resume get(String uuid) {
        LOG.info("Get " + uuid);
        return getResume(checkKey(uuid));
    }

    public void delete(String uuid) {
        deleteFromStorage(checkKey(uuid));
        LOG.info("Delete " + uuid);
    }

    public List<Resume> getAllSorted() {
        List<Resume> resumes = getAll();
        resumes.sort(comparator);
        return resumes;
    }

    private T checkKey(String uuid) {
        T searchKey = getSearchKey(uuid);
        if (isExistKey(searchKey)) {
            return searchKey;
        } else {
            LOG.warning(uuid + "not exist.");
            throw new NotExistException(uuid);
        }
    }

    public abstract List<Resume> getAll();

    protected abstract void saveToStorage(T searchKey, Resume resume) throws IOException;

    protected abstract void updateToStorage(T searchKey, Resume resume);

    protected abstract Resume getResume(T searchKey);

    protected abstract boolean isExistKey(T searchKey);

    protected abstract void deleteFromStorage(T searchKey);

    public abstract T getSearchKey(String uuid);
}