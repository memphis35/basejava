package ru.javawebinar.basejava.model;

public abstract class Section<T> {

    protected T data;

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
