package ru.javawebinar.basejava.model;

import java.util.ArrayList;

public class ListSection extends Section<ArrayList<String>> {

    public ListSection() {
        data = new ArrayList<>();
    }

    public void addItem(String item) {
        if (!data.contains(item)) {
            data.add(item);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append('\n');
        for (String str : data) {
            result.append('\t').append(str).append('\n');
        }
        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
}
