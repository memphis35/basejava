package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.ArrayStorage;
import ru.javawebinar.basejava.storage.MapResumeStorage;
import ru.javawebinar.basejava.storage.SortedArrayStorage;
import ru.javawebinar.basejava.storage.Storage;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for your ru.javawebinar.basejava.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1", "Mick Mars");
        Resume r2 = new Resume("uuid2", "Adrian Paul");
        Resume r3 = new Resume("uuid3","Vince Neil");
        Resume r4 = new Resume("uuid4", "Tommy Lee");
        Resume r5 = new Resume("uuid5", "Adrian Paul");

        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.save(r5);
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    private static void printAll() {
        System.out.println("\nGet All");
        List<Resume> result = new ArrayList<>(ARRAY_STORAGE.getAllSorted());
        for (int i = 0; i < result.size(); i++) {
            System.out.print(result.get(i).getFullName() + "\t");
            System.out.println(result.get(i));
        }
    }
}
