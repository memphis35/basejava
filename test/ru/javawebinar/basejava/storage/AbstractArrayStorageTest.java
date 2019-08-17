package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

import static org.junit.Assert.*;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;
    private Resume r1 = new Resume("1111");
    private Resume r2 = new Resume("2222");
    private Resume r3 = new Resume("3333");
    private Resume r4 = new Resume("4444");
    private Resume r5 = r2;

    public AbstractArrayStorageTest(Storage test) {
        storage = test;
    }

    @Before
    public void setUp() {
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    public void clear() {
        storage.clear();
        Resume[] result = new Resume[0];
        assertArrayEquals(result, storage.getAll());
        assertEquals(0, storage.size());
    }

    @Test
    public void saveSuccess() {
        storage.save(r4);
        assertEquals(r4, storage.get(r4.getUuid()));
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistException.class)
    public void saveFailed() {
        storage.save(r5);
    }

    @Test
    public void updateSuccess() {
        storage.update(r5);
        assertEquals(r5, storage.get(r5.getUuid()));
    }

    @Test(expected = NotExistException.class)
    public void updateFailed() {
        storage.update(r4);
    }

    @Test
    public void getSuccess() {
        assertEquals(r3, storage.get(r3.getUuid()));
    }

    @Test(expected = NotExistException.class)
    public void getFailed() {
        storage.get(r4.getUuid());
    }

    @Test(expected = NotExistException.class)
    public void deleteSuccess() {
        storage.delete(r2.getUuid());
        assertEquals(2, storage.size());
        storage.get(r2.getUuid());
    }

    @Test(expected = NotExistException.class)
    public void deleteFailed() {
        storage.delete(r4.getUuid());
    }

    @Test
    public void getAll() {
        Resume[] result = new Resume[]{r1, r2, r3};
        Resume[] test1 = storage.getAll();
        assertArrayEquals(result, test1);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }

    @Test (expected = StorageException.class)
    public void saveOverloaded() {
        storage.clear();
        int uuid = 1;
        for (int i = 0; i < 10_000; i++) {
            Resume r = new Resume(Integer.toString(uuid));
            try {
                storage.save(r);
                uuid++;
            } catch (StorageException s) {
                Assert.fail("Overflow");
            }
        }
        Resume a = new Resume("OVERLOADING RESUME");
        storage.save(a);
    }
}
