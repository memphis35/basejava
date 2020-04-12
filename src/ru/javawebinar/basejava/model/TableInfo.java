package ru.javawebinar.basejava.model;

import javafx.scene.control.TableCell;

import java.util.ArrayList;

public class TableInfo extends Information<ArrayList<TableInfo.TableCell>> {

    public TableInfo() {
        data = new ArrayList<>();
    }

    public void addElement(String name, String date, String position, String description) {
        TableCell element = new TableCell(name, date, position, description);
        if (!data.contains(element)) {
            data.add(element);
        }
    }

    public void removeElement(int index) {
        if (index > 0 && index < data.size()) {
            data.remove(index);
        }
    }

    class TableCell {
        String name;
        String date;
        String position;
        String description;

        TableCell(String name, String date, String position, String description) {
            this.name = name;
            this.date = date;
            this.position = position;
            this.description = description;
        }
    }
}
