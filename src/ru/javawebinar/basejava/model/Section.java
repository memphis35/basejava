package ru.javawebinar.basejava.model;

public abstract class Section<T> {

    final T content;

    protected Section(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }
}
