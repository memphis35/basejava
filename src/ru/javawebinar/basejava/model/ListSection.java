package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListSection extends Section {

    private List<String> content = new ArrayList<>();

    public ListSection() {
    }

    public ListSection(List<String> content) {
        Objects.requireNonNull(content, "ListSection content must not be null.");
        this.content = content;
    }

    public void addItem(String item) {
        if (!content.contains(item)) {
            content.add(item);
        }
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || !this.getClass().equals(obj.getClass())) return false;
        ListSection o = (ListSection) obj;
        return Objects.equals(this.content, o.content);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (String str : content) {
            result.append('\n').append(str);
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

}
