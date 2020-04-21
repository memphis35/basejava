package ru.javawebinar.basejava.model;

import java.time.LocalDate;
import java.util.*;

class Organization {

    private final Link homepage;
    private List<Position> positions = new ArrayList<>();


    public Organization(Link homepage, Position...position) {
        this.homepage = homepage;
        Collections.addAll(Arrays.asList(position));
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

    protected static class Position {
        private final LocalDate startDate;
        private final LocalDate endDate;
        private final String title;
        private final String description;

        public Position(LocalDate startDate, LocalDate endDate, String title, String description) {
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
            StringBuilder sb = new StringBuilder();
            sb.append("\n\t").append(startDate).append('-').append(endDate).append(' ').append(title);
            if (description != null) sb.append("\n\t").append(description);
            return sb.toString();
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
