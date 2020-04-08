package ru.javawebinar.basejava.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TableInfo extends Information<ArrayList<TableInfo.TableCell>> {

    public TableInfo() {
        try {
            fill();
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

    public void fill() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        data = new ArrayList<>();
        while (true) {
            System.out.print("\tPress ENTER to close. Press another key to add info: ");
            if (reader.readLine().isEmpty()) break;
            TableCell element = new TableCell();
            element.fill();
            data.add(element);
        }
        //reader.close();
    }

    class TableCell {
        String name;
        String date;
        String position;
        String description;

        public void fill() throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Enter name: ");
            name = reader.readLine();
            System.out.print("\nEnter period: ");
            date = reader.readLine();
            System.out.print("\nEnter position: ");
            position = reader.readLine();
            System.out.print("\nEnter description: ");
            description = reader.readLine();
        }
    }
}
