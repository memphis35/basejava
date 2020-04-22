package ru.javawebinar.basejava.model;

public enum ContactType {

    PHONE("Phone: "),
    SKYPE("Skype: "),
    EMAIL("E-mail: "),
    LINKEDIN("Profile on LinkedIn: "),
    GITHUB("Profile on GitHub: "),
    STACKOVERFLOW("Profile on StackOverFlow.com: "),
    HOMEPAGE("Homepage: ");

    private String title;

    ContactType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}