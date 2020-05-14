package ru.javawebinar.basejava.model;

import ru.javawebinar.basejava.exception.StorageException;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class OrganizationSection extends Section {

    List<Organization> content;

    public OrganizationSection() {
    }

    public OrganizationSection(List<Organization> content) {
        Objects.requireNonNull(content, "OrganizationSection must not be null.");
        this.content = content;
    }

    public void addOrganization(Organization org) {
        if (!content.contains(org)) {
            content.add(org);
        } else {
            throw new StorageException("Organization already exist.", null);
        }
    }

    public void addPosition(String orgTitle, String title, LocalDate startDate, LocalDate endDate, String description) {
        for (Organization org : content) {
            if (org.getHomepage().getName().equals(orgTitle)) {
                org.getPositions().add(new Organization.Position(title, startDate, endDate, description));
                return;
            }
        }
        throw new StorageException("Can't find organization", null);
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
