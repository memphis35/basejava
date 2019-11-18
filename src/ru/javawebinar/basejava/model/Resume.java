package ru.javawebinar.basejava.model;

import java.util.Objects;

/**
 * Initial resume class
 */
public class Resume implements Comparable<Resume> {

    private String uuid;
    private String fullName;

    public Resume(String uuid) {
        this.uuid = uuid;
    }

    public Resume(String uuid, String name) {
        this.uuid = uuid;
        fullName = name;
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
        if (o.fullName != null && this.fullName.compareTo(o.fullName) != 0) {
            return this.fullName.compareTo(o.fullName);
        } else return this.uuid.compareTo(o.uuid);
    }
}
