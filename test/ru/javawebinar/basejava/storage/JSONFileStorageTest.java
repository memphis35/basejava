package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializer.JSONSerializer;

import java.io.IOException;

public class JSONFileStorageTest extends AbstractStorageTest {

    public JSONFileStorageTest() throws IOException {
        super(new FileStorage(STORAGE_DIR, new JSONSerializer()));
    }
}
