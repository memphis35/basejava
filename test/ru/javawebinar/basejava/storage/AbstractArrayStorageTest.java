package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private Resume[] result = new Resume[3];

    public AbstractArrayStorageTest(Storage test) {
        storage = test;
    }

    @Before
    public void setUp() {
        Resume r1 = new Resume("1111");
        Resume r2 = new Resume("2222");
        Resume r3 = new Resume("3333");
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
        this.result = new Resume[]{r1, r2, r3};
    }

    @Test
    public void clear() {
        storage.clear();
        Resume[] result = new Resume[0];
        assertEquals(storage.getAll(), result);
    }

    @Test
    public void saveSuccess() {
        Resume r4 = new Resume("4444");
        storage.save(r4);
        assertEquals(storage.get("4444"), r4);
    }

    @Test(expected = ExistException.class)
    public void saveFailed() {
        Resume r = new Resume("1111");
        storage.save(r);
    }

    @Test
    public void updateSuccess() {
        Resume r4 = new Resume("2222");
        storage.update(r4);
        assertEquals(storage.get("2222"), r4);
    }

    @Test(expected = NotExistException.class)
    public void updateFailed() {
        Resume r4 = new Resume("4444");
        storage.update(r4);
    }

    @Test
    public void getSuccess() {
        Resume r4 = new Resume("4444");
        storage.save(r4);
        assertEquals(storage.get("4444"), r4);
    }

    @Test(expected = NotExistException.class)
    public void getFailed() {
        storage.get("4444");
    }

    @Test(expected = NotExistException.class)
    public void deleteSuccess() {
        storage.delete("2222");
        storage.get("2222");
    }

    @Test(expected = NotExistException.class)
    public void deleteFailed() {
        storage.delete("4444");
    }

    @Test
    public void getAll() {
        Resume[] test1 = storage.getAll();
        assertEquals(result, test1);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test(expected = StorageException.class)
    public void saveOverloaded() {
        storage.clear();
        int uuid = 1;
        for (int i = 0; i < 10_000; i++) {
            Resume r = new Resume(Integer.toString(uuid));
            storage.save(r);
            uuid++;
        }
        Resume a = new Resume("OVERLOADING RESUME");
        storage.save(a);
    }
}
