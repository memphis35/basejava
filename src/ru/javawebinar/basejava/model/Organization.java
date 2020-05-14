package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.util.LocalDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Organization implements Serializable {

    private Link homepage;
    private List<Position> positions = new ArrayList<>();

    public Organization() {
    }

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
        sb.append(homepage);
        for (Position p : positions) {
            sb.append(p.toString());
        }
        return sb.toString();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Position implements Serializable {
        @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
        private LocalDate startDate;
        @XmlJavaTypeAdapter(value = LocalDateAdapter.class)
        private LocalDate endDate;
        private String title;
        private String description;

        public Position() {
        }

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
            return String.format("\n\tPeriod: %s - %s | Title: %s | Description: %s",
                    startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                    title, description);
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
