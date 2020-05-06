package ru.javawebinar.basejava.model;

import java.util.List;
import java.util.Objects;

public class ListSection extends Section<List<String>> {

    public ListSection(List<String> content) {
        super(content);
        Objects.requireNonNull(content, "ListSection content must not be null.");
    }

    public void addItem(String item) {
        if (!content.contains(item)) {
            content.add(item);
        }
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
        result.append('\n');
        for (String str : content) {
            result.append('\t').append(str).append('\n');
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }

}
