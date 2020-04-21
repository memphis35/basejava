package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.Objects;

class Position {
    private final Link homepage;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String title;
    private final String description;

    public Position(Link homepage, LocalDate startDate, LocalDate endDate, String title, String description) {
        Objects.requireNonNull(startDate, "Start date must not be null");
        Objects.requireNonNull(endDate, "End date must not be null");
        Objects.requireNonNull(title, "Title must not be null");
        this.homepage = homepage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.description = description;
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
        String result = String.format("\n\tTitle: %s\n\tPeriod: %s-%s\n\tDescription: %s", title, startDate, endDate, description);
        return result;
    }
}
