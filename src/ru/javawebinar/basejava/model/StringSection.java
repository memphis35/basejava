package ru.javawebinar.basejava.model;

import java.util.Objects;

public class StringSection extends Section<String> {

    public StringSection(String content) {
        super(content);
        Objects.requireNonNull(content, "StringSection data must not be null.");
    }

    public String getContent() {
        return content;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return content;
    }

}
