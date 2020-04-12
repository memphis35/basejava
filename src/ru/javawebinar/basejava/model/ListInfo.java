package ru.javawebinar.basejava.model;

import java.util.ArrayList;

public class ListInfo extends Information<ArrayList<String>> {

    public ListInfo() {
        data = new ArrayList<>();
    }

    public void addElement(String element) {
        if (!data.contains(element)) {
            data.add(element);
        }
    }

    public void removeElement(int index) {
        if (!data.isEmpty() && index < data.size()) {
            data.remove(index);
        }
    }
}
