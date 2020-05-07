package ru.javawebinar.basejava.storage;

import java.io.IOException;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() throws IOException {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath()));
        storage.setStrategy(new ObjectStreamSerialization());
    }
}
