package ru.javawebinar.basejava.storage;

public class ObjectStreamPathStorageTest extends AbstractStorageTest {
    public ObjectStreamPathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath()));
        PathStorage test = (PathStorage) storage;
        test.setStrategy(new ObjectStreamSerialization());
    }
}
