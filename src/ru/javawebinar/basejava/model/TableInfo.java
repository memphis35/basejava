package ru.javawebinar.basejava.model;

import java.util.ArrayList;

public class TableInfo extends Section<ArrayList<TableInfo.Position>> {

    public TableInfo() {
        data = new ArrayList<>();
    }

    public void addElement(String name, String date, String position, String description) {
        Position element = new Position(name, date, position, description);
        if (!data.contains(element)) {
            data.add(element);
        }
    }

    public void removeElement(int index) {
        if (index > 0 && index < data.size()) {
            data.remove(index);
        }
    }

    class Position {
        String name;
        String date;
        String position;
        String description;

        Position(String name, String date, String position, String description) {
            this.name = name;
            this.date = date;
            this.position = position;
            this.description = description;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            result.append('\n').append('\t').append("Name: ").append(name).append('\n');
            result.append('\t').append("Period: ").append(date).append('\n');
            result.append('\t').append("Position: ").append(position).append('\n');
            result.append('\t').append("Description: ").append(description).append('\n');
            return result.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Position p : data) {
            result.append(p.toString());
        }
        return result.toString();
    }
}
