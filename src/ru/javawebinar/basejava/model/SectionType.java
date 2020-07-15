package ru.javawebinar.basejava.model;

import java.io.Serializable;

public enum SectionType implements Serializable {

    PERSONAL("Личные качества"),
    OBJECTIVE("Позиция"),
    ACHIEVEMENTS("Достижения"),
    QUALIFICATION("Квалификация"),
    EXPERIENCE("Опыт работы"),
    EDUCATION("Образование");

    private String title;

    SectionType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static SectionType getType(String title) {
        switch (title) {
            case "Личные качества": return PERSONAL;
            case "Позиция": return OBJECTIVE;
            case "Достижения": return ACHIEVEMENTS;
            case "Квалификация": return QUALIFICATION;
            case "Опыт работы": return EXPERIENCE;
            case "Образование": return EDUCATION;
            default: throw new IllegalArgumentException(title);
        }
    }
}
