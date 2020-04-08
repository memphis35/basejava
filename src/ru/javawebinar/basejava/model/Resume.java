package ru.javawebinar.basejava.model;

import java.util.*;

/**
 * Initial resume class
 */

public class Resume implements Comparable<Resume> {

    private String uuid;
    private String fullName;
    public HashMap<Contact, String> contacts = new HashMap<>();
    public HashMap<SectionType, Information> personInfo = new HashMap<>();

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.fullName = Objects.requireNonNull(fullName);
        this.uuid = Objects.requireNonNull(uuid);
    }

    public void show() {
        System.out.printf("Resume from %s, ID %s\n", fullName, uuid);
        for (Contact contact : contacts.keySet()) {
            System.out.printf("%s: %s\n", contact.getTitle(), contacts.get(contact));
        }
        for (SectionType section : personInfo.keySet()) {
            System.out.printf("%s: %s\n", section.getTitle(), personInfo.get(section).toString());
        }
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setFullName(String name) {
        fullName = name;
    }

    public String getFullName() {
        return fullName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resume resume = (Resume) o;
        return uuid.equals(resume.uuid) && fullName.equals(resume.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
