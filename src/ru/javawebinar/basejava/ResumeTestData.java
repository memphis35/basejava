package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResumeTestData {

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the name: ");
        Resume test = new Resume(reader.readLine());
        System.out.printf("Creating %s resume ID %s\n", test.getFullName(), test.getUuid());
        for (Contact element : Contact.values()) {
            System.out.print("Enter a " + element.getTitle());
            test.contacts.put(element, reader.readLine());
        }
        for (SectionType element : SectionType.values()) {
            System.out.printf("Enter a %s: \n", element.name());
            if (element.name().equals("PERSONAL") || element.name().equals("OBJECTIVE")) {
                test.personInfo.put(element, new SimpleInfo(reader.readLine()));
            }
            if (element.name().equals("ACHIEVEMENTS") || element.name().equals("QUALIFICATION")) {
                test.personInfo.put(element, new ListInfo());
            }
            if (element.name().equals("EXPERIENCE") || element.name().equals("EDUCATION")) {
                test.personInfo.put(element, new TableInfo());
            }
        }
        reader.close();
        test.show();
    }
}
