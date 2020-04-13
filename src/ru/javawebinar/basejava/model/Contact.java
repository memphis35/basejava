package ru.javawebinar.basejava.model;

public enum Contact {

    PHONE("Phone: "),
    SKYPE("Skype: "),
    EMAIL("E-mail: "),
    LINKEDIN("Profile on LinkedIn: "),
    GITHUB("Profile on GitHub: "),
    STACKOVERFLOW("Profile on StackOverFlow.com: "),
    HOMEPAGE("Homepage: ");

    private String title;

    Contact(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}