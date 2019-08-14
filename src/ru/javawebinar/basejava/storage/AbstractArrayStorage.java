package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected final static int MAX_SIZE = 10_000;
    protected Resume[] storage = new Resume[MAX_SIZE];
    protected int currentSize = 0;

    /**
     * Clear the array storage
     */
    public void clear() {
        Arrays.fill(storage, 0, currentSize, null);
        currentSize = 0;
        System.out.println("Storage successfully cleared.");
    }

    /**
     * Save resume object in array storage
     *
     * @param resume current resume
     */
    public void save(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (currentSize >= MAX_SIZE) {
            throw new StorageException("Storage overloaded.", resume.getUuid());
        } else if (index < 0) {
            saveToArray(index, resume);
            System.out.println("Resume successfully saved.");
            currentSize++;
        } else {
            throw new ExistException(resume.getUuid());
        }
    }

    /**
     * Replace resume in storage by UUID number
     *
     * @param resume incoming resume
     */
    public void update(Resume resume) {
        int index = getIndex(resume.getUuid());
        if (index >= 0) {
            storage[index] = resume;
            System.out.println("Resume #" + resume.getUuid() + " successfully updated.");
        } else {
            throw new NotExistException(resume.getUuid());
        }
    }

    /**
     * Get UUID number from Resume object, if it exist and possible
     *
     * @param uuid incoming UUID number
     * @return UUID number of existing Resume
     */
    public Resume get(String uuid) {
        int index = getIndex(uuid);
        if (index >= 0) {
            return storage[index];
        }
        throw new NotExistException(uuid);
    }

    /**
     * Delete existing Resume from array storage by UUID number
     *
     * @param uuid incoming UUID number
     */
    public void delete(String uuid) throws StorageException {
        int index = getIndex(uuid);
        if (index >= 0) {
            deleteFromArray(index);
            storage[currentSize - 1] = null;
            currentSize--;
            System.out.println("Resume successfully deleted.");
        } else {
            throw new NotExistException(uuid);
        }
    }

    /**
     * Get all Resume's UUIN numbers, printed to stack
     *
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, currentSize);
    }

    /**
     * Call a current quantity of Resume of array storage
     *
     * @return size of array (quantity of objects)
     */
    public int size() {
        return currentSize;
    }

    public abstract void saveToArray(int index, Resume resume);

    public abstract void deleteFromArray(int index);

    /**
     * Searching for a existing resume by UUID number
     *
     * @param uuid UUID number
     * @return true if resume exist
     */
    public abstract int getIndex(String uuid);
}