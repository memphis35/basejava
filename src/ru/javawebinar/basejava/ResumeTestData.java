package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.io.IOException;

public class ResumeTestData {

    public static void main(String[] args) throws IOException {

        Resume test = new Resume("Alexander Smirnov");

        for (Contact element : Contact.values()) {
            test.contacts.put(element, "something info");
        }
        for (SectionType element : SectionType.values()) {
            if (element.name().equals("PERSONAL") || element.name().equals("OBJECTIVE")) {
                SimpleInfo elementInfo = new SimpleInfo();
                elementInfo.setData("simpleInfo");
                test.personInfo.put(element, elementInfo);
            }
            if (element.name().equals("ACHIEVEMENTS") || element.name().equals("QUALIFICATION")) {
                ListSection elementInfo = new ListSection();
                elementInfo.addItem("someListInfo");
                test.personInfo.put(element, elementInfo);
            }
            if (element.name().equals("EXPERIENCE") || element.name().equals("EDUCATION")) {
                TableInfo elementInfo = new TableInfo();
                elementInfo.addElement("someName", "someDate", "somePosition", "someDescription");
                test.personInfo.put(element, elementInfo);
            }
        }

        System.out.println(test.toString());
    }
}
