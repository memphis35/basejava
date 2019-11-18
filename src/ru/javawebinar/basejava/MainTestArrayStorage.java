package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.storage.MapResumeStorage;
import ru.javawebinar.basejava.storage.Storage;

/**
 * Test for your ru.javawebinar.basejava.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    private static final Storage ARRAY_STORAGE = new MapResumeStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume("uuid1");
        r1.setFullName("Mick Mars");
        Resume r2 = new Resume("uuid2");
        r2.setFullName("Adrian Paul");
        Resume r3 = new Resume("uuid3");
        r3.setFullName("Vince Neil");
        Resume r4 = new Resume("uuid4");
        r4.setFullName("Tommy Lee");
        Resume r5 = new Resume("uuid5");
        r5.setFullName("Adrian Paul");


        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.size());

        //System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));
        //ARRAY_STORAGE.update(r5); //wrong update
        //r5.setUuid("Alex");
        //ARRAY_STORAGE.update(r5); //right update

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.save(r5);
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    private static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAllSorted()) {
            System.out.println(r);
        }
    }
}
