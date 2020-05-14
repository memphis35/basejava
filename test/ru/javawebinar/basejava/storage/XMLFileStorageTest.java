package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.serializer.XMLSerializer;

import java.io.IOException;

public class XMLFileStorageTest extends AbstractStorageTest {

    public XMLFileStorageTest() throws IOException {
        super(new FileStorage(STORAGE_DIR, new XMLSerializer()));
    }
}
