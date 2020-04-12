package ru.javawebinar.basejava.storage;

import org.junit.*;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.Resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    final Storage storage;
    private Resume r1 = new Resume("uuid1","Aaron Paul");
    private Resume r2 = new Resume("uuid2","Nikki Six");
    private Resume r3 = new Resume("uuid3", "Mick Mars");
    private Resume r4 = new Resume("uuid4", "Aaron Paul");

    AbstractStorageTest(Storage test) {
        storage = test;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(r1);
        storage.save(r2);
        storage.save(r3);
    }

    @Test
    public void clear() {
        storage.clear();
        List<Resume> result = new ArrayList<>();
        assertEquals(result, storage.getAllSorted());
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
        storage.save(r2);
    }

    @Test
    public void updateSuccess() {
        Resume r5 = new Resume(r2.getUuid(), "fullNameUpdated");
        storage.update(r5);
        assertEquals(r5, storage.get(r2.getUuid()));
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
    public void getAllSorted() {
        storage.save(r4);
        List<Resume> result = Arrays.asList(r1, r4, r3, r2);
        List<Resume> test1 = storage.getAllSorted();
        assertEquals(result, test1);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}