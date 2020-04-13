package ru.javawebinar.basejava.model;

import java.util.*;

/**
 * Initial resume class
 */

public class Resume implements Comparable<Resume> {

    private String uuid;
    private String fullName;
    public Map<Contact, String> contacts = new EnumMap<>(Contact.class);
    public Map<SectionType, Section> personInfo = new EnumMap<>(SectionType.class);

    public Resume(String fullName) {
        this(UUID.randomUUID().toString(), fullName);
    }

    public Resume(String uuid, String fullName) {
        this.fullName = Objects.requireNonNull(fullName);
        this.uuid = Objects.requireNonNull(uuid);
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
        return uuid.equals(resume.uuid)
                && fullName.equals(resume.fullName)
                && contacts.equals(resume.contacts)
                && personInfo.equals(resume.personInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fullName, contacts, personInfo);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Name: ").append(fullName).append(". UUID: ").append(uuid).append('\n');
        for (Contact contact : contacts.keySet()) {
            result.append(contact.getTitle()).append(": ").append(contacts.get(contact)).append('\n');
        }
        for (SectionType section : personInfo.keySet()) {
            result.append(section.getTitle()).append(": ").append(personInfo.get(section).toString()).append('\n');
        }
        return result.toString();
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
