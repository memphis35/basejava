package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.exception.StorageException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {

    private List<Organization> content;

    public OrganizationSection() {
        content = new ArrayList<>();
    }

    public OrganizationSection(List<Organization> content) {
        Objects.requireNonNull(content, "OrganizationSection must not be null.");
        this.content = content;
    }

    public void addOrganization(Organization org) {
        Objects.requireNonNull(org);
        if (!content.contains(org)) {
            content.add(org);
        } else {
            throw new StorageException("Organization already exist.");
        }
    }

    public void addPosition(Organization org, Organization.Position position) {
        if (!content.contains(org)) content.add(org);
        content.get(content.indexOf(org)).addPosition(position);
    }

    public List<Organization> getContent() {
        return content;
    }

    public void setContent(List<Organization> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Organization p : content) {
            result.append(p.toString());
        }
        return result.toString();
    }
}
