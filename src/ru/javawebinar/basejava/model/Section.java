package ru.javawebinar.basejava.model;

import java.io.Serializable;

public abstract class Section<T> implements Serializable {

    final T content;

    protected Section(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}
