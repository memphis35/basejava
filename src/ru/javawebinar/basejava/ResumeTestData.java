package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class ResumeTestData {

    public static void main(String[] args) {

        Resume test = new Resume("Alexander Smirnov");

        for (ContactType element : ContactType.values()) {
            test.getContacts().put(element, "something info");
        }
        for (SectionType element : SectionType.values()) {
            if (element.name().equals("PERSONAL") || element.name().equals("OBJECTIVE")) {
                StringSection elementInfo = new StringSection("simpleInfo");
                test.getPersonInfo().put(element, elementInfo);
            }
            if (element.name().equals("ACHIEVEMENTS") || element.name().equals("QUALIFICATION")) {
                ListSection elementInfo = new ListSection(new ArrayList<>());
                elementInfo.addItem("someListInfo");
                test.getPersonInfo().put(element, elementInfo);
            }
            if (element.name().equals("EXPERIENCE") || element.name().equals("EDUCATION")) {
                OrganizationSection elementInfo = new OrganizationSection(new ArrayList<>());
                elementInfo.addOrganization(new Organization(new Link("someOrganizatiion", null), new Organization.Position(
                        "someTitle", LocalDate.of(1, 1, 1), LocalDate.of(2, 2, 2), "someDescription")));
                test.getPersonInfo().put(element, elementInfo);
            }
        }

        System.out.println(test.toString());
    }
}
