package ru.javawebinar.basejava.model;

import java.io.Serializable;

public enum SectionType implements Serializable {

    PERSONAL("Personal"),
    OBJECTIVE("Objective"),
    ACHIEVEMENTS("Achievements"),
    QUALIFICATION("Qualification"),
    EDUCATION("Education"),
    EXPERIENCE("Experience");

    private final String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

}
