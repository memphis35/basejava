package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Objects;

public class ListSection extends Section<ArrayList<String>> {

    public ListSection(ArrayList<String> content) {
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
        return super.equals(obj);
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
