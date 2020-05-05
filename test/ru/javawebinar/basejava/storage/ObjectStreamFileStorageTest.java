package ru.javawebinar.basejava.storage;

import java.io.IOException;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() throws IOException {
        super(new ObjectStreamFileStorage(STORAGE_DIR));
    }
}
