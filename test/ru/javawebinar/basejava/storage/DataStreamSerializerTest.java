package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializer.DataStreamSerializer;

import java.io.IOException;

public class DataStreamSerializerTest extends AbstractStorageTest {

    public DataStreamSerializerTest() throws IOException {
        super(new FileStorage(STORAGE_DIR, new DataStreamSerializer()));
    }
}
