package ru.javawebinar.basejava.model;

import java.io.IOException;

public abstract class Information<T> {

    protected T data;
    public void setData(T data) {
        this.data = data;
    }
    public T getData() {
        return data;
    }
}
