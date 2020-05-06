package ru.javawebinar.basejava.storage;

import java.io.File;
import java.io.IOException;

public class Context {

    Strategy strategy;

    public Context(SerializationType type) throws IOException {
        switch (type) {
            case JAVA_IO:
                strategy = new ObjectStreamFileStorage(new File("..\\storage"));
                break;
            case JAVA_NIO:
                strategy = new ObjectStreamPathStorage("..\\storage");
                break;
            default: break;
        }
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public enum SerializationType {
        JAVA_IO,
        JAVA_NIO,
        JSON,
        XML
    }
}
