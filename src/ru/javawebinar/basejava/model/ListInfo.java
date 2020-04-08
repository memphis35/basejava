package ru.javawebinar.basejava.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ListInfo extends Information<ArrayList<String>> {

    public ListInfo() {
        try {
            fill();
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

    private void fill() throws IOException {
        data = new ArrayList<>(5);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("\tPress ENTER to close. Else enter text here: ");
            String input = reader.readLine();
            if (input.isEmpty()) break;
            data.add(input);
        }
        //reader.close();
    }


    public void addElement(String element) {
        if (!data.contains(element)) {
            data.add(element);
        }
    }

    public void removeElement(int index) {
        if (!data.isEmpty() && index < data.size()) {
            data.remove(index);
        }
    }
}
