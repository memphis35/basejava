package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializer.ObjectStreamSerializer;

import java.io.IOException;

public class ObjectStreamFileStorageTest extends AbstractStorageTest {

    public ObjectStreamFileStorageTest() throws IOException {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerializer()));
    }
}
