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

}
