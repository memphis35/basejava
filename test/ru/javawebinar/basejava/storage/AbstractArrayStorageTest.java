package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {

    AbstractArrayStorageTest(Storage test) {
        super(test);
    }

    @Test(expected = StorageException.class)
    public void saveOverloaded() {
        storage.clear();
        int uuid = 1;
        for (int i = 0; i < 10_000; i++) {
            Resume r = new Resume("uuid" + i, i + "ass");
            try {
                storage.save(r);
                uuid++;
            } catch (StorageException s) {
                Assert.fail("Overflow");
            }
        }
        Resume a = new Resume("OVERLOADING UUID", "OVERLOADING RESUME");
        storage.save(a);
    }
}
