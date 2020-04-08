package ru.javawebinar.basejava.model;

import java.io.IOException;

public abstract class Information<T> {

    T data;

    public T getData() {
        return data;
    }
}
