package ru.javawebinar.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;

public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Test(expected = StorageException.class)
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
