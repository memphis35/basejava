package ru.javawebinar.basejava.model;

import java.util.Objects;

public class StringSection extends Section {

    private String content;

    public StringSection() {
    }

    public StringSection(String content) {
        Objects.requireNonNull(content, "StringSection data must not be null.");
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
