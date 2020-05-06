package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.exception.StorageException;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Organization implements Serializable {

    private final Link homepage;
    private List<Position> positions = new ArrayList<>();


    public Organization(Link homepage, Position...position) {
        this.homepage = homepage;
        Collections.addAll(positions, position);
    }

    Link getHomepage() {
        return homepage;
    }

    List<Position> getPositions() {
        return positions;
    }

    public void addPosition(LocalDate startDate, LocalDate endDate, String title, String description) {
        positions.add(new Position(title, startDate, endDate,  description));
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Position p : positions) {
            sb.append(p.toString());
        }
        return sb.toString();
    }

    public static class Position implements Serializable {
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        public Position(String title, LocalDate startDate, LocalDate endDate, String description) {
            Objects.requireNonNull(startDate, "Start date must not be null");
            Objects.requireNonNull(endDate, "End date must not be null");
            Objects.requireNonNull(title, "Title must not be null");
            this.startDate = startDate;
            this.endDate = endDate;
            this.title = title;
            this.description = description;
        }



        @Override
        public String toString() {
            return String.format("\n\tPeriod: %s - %s | Title: %s | Description: %s", startDate.toString(), endDate.toString(), title, description);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position o = (Position) obj;
            return (this.startDate.equals(o.startDate) &&
                    this.endDate.equals(o.endDate) &&
                    this.title.equals(o.title) &&
                    this.description.equals(o.description));
        }
    }
}
