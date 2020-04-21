package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class ComplexSection extends Section<ArrayList<Organization>> {

    public ComplexSection(ArrayList<Organization> content) {
        super(content);
        Objects.requireNonNull(content, "ComplexSection must not be null.");
    }

    public void addElement(String title, LocalDate startDate, LocalDate endDate, String description) {
        Organization element = new Organization(null, new Organization.Position(startDate, endDate, title, description));
        if (!content.contains(element)) {
            content.add(element);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Organization p : content) {
            result.append(p.toString());
        }
        return result.toString();
    }
}
