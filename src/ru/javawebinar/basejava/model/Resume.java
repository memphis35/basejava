package ru.javawebinar.basejava.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.*;

/**
 * Initial resume class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Resume implements Comparable<Resume>, Serializable {

    private String uuid;
    private String fullName;
    private Map<ContactType, String> contacts = new EnumMap<>(ContactType.class);
    private Map<SectionType, Section> personInfo = new EnumMap<>(SectionType.class);

    public Resume() {
    }

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

    public void addContact(ContactType type, String value) {
        contacts.put(type, value);
    }

    public <T> void addSection(SectionType type, Section value) {
        personInfo.put(type, value);
    }

    public Map<ContactType, String> getContacts() {
        return contacts;
    }

    public Map<SectionType, Section> getPersonInfo() {
        return personInfo;
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
        for (ContactType contactType : contacts.keySet()) {
            result.append(contactType.getTitle()).append(": ").append(contacts.get(contactType)).append('\n');
        }
        for (SectionType section : personInfo.keySet()) {
            result.append(section.getTitle()).append(": ").append(personInfo.get(section)).append('\n');
        }
        return result.toString();
    }

    @Override
    public int compareTo(Resume o) {
        return uuid.compareTo(o.uuid);
    }
}
