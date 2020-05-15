package ru.javawebinar.basejava.writers;

import java.io.IOException;

@FunctionalInterface
public interface WriterInterface<T> {

    void writeData(T element) throws IOException;
}
