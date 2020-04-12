package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.io.IOException;
import java.util.ArrayList;

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
                ListInfo elementInfo = new ListInfo();
                elementInfo.addElement("someListInfo");
                test.personInfo.put(element, elementInfo);
            }
            if (element.name().equals("EXPERIENCE") || element.name().equals("EDUCATION")) {
                TableInfo elementInfo = new TableInfo();
                elementInfo.addElement("someName", "someDate", "somePosition", "someDescription");
                test.personInfo.put(element, elementInfo);
            }
        }

        test.show();
    }
}
