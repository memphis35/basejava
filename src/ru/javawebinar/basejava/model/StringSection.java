package ru.javawebinar.basejava.model;

import java.util.Objects;

public class StringSection extends Section<String> {

    public StringSection(String content) {
        super(content);
        Objects.requireNonNull(content, "StringSection data must not be null.");
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StringSection)) return false;
        if (this == obj) return true;
        StringSection o = (StringSection) obj;
        return content.equals(o.content);
    }

    @Override
    public String toString() {
        return content;
    }
}
