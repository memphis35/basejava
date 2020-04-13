package ru.javawebinar.basejava.model;

import java.util.ArrayList;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ListSection)) return false;
        ListSection list = (ListSection) obj;
        if (data.size() != list.data.size()) return false;
        boolean isEqual = true;
        for (int i = 0; i < data.size(); i++) {
            if (!data.get(i).equals(list.data.get(i))) {
                isEqual = false;
                break;
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(data);
    }
}
