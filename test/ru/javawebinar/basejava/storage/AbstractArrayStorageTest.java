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
        for (int i = 0; i < 10_000; i++) {
            try {
                storage.save(new Resume("name" + i));
            } catch (StorageException s) {
                Assert.fail("Overflow");
            }
        }
        storage.save(new Resume("OVERLOADING UUID", "OVERLOADING RESUME"));
    }
}
