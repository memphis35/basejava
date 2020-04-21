package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class ComplexSection extends Section<ArrayList<Position>> {

    public ComplexSection(ArrayList<Position> content) {
        super(content);
        Objects.requireNonNull(content, "ComplexSection must not be null.");
    }

    public void addElement(String title, LocalDate startDate, LocalDate endDate, String description) {
        Position element = new Position(null, startDate, endDate, title, description);
        if (!content.contains(element)) {
            content.add(element);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Position p : content) {
            result.append(p.toString());
        }
        return result.toString();
    }
}
