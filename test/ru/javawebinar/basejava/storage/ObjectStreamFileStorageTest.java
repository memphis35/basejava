package ru.javawebinar.basejava.storage;

import java.io.IOException;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() throws IOException {
        super(new FileStorage(STORAGE_DIR));
        FileStorage test = (FileStorage) storage;
        test.setStrategy(new ObjectStreamSerialization());
    }
}
