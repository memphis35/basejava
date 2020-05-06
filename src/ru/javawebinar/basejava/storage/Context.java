package ru.javawebinar.basejava.storage;

import java.io.File;
import java.io.IOException;

public class Context {

    public Context(SerializationType type) throws IOException {
        switch (type) {
            case JAVA_IO:
                storage = new ObjectStreamFileStorage(new File("..\\storage"));
                break;
            case JAVA_NIO:
                storage = new ObjectStreamPathStorage("..\\storage");
                break;
            default: break;
        }
    }

    private Storage storage;

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    public Storage getStorage() {
        return storage;
    }

    public enum SerializationType {
        JAVA_IO,
        JAVA_NIO,
        JSON,
        XML
    }
}
