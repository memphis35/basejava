package ru.javawebinar.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    public static final File STORAGE_DIR = new File("D:/projects/basejava/storage");
    final Storage storage;
    private final static String UUID1 = "1fa0aee2-1ac7-48f2-9d7f-7e111087f111";
    private final static String UUID2 = "818d5951-da43-4b3f-9a60-33982126ae96";
    private final static String UUID3 = "6b365ab4-b4dc-4e86-8aa3-d7617133ea90";
    private final static String UUID4 = "30bf4389-c8f7-47f8-bc85-469563db7619";
    private final static Resume R_1 = new Resume(UUID1, "Aaron Paul");
    private final static Resume R_2 = new Resume(UUID2, "Nikki Six");
    private final static Resume R_3 = new Resume(UUID3, "Micky Mars");
    private final static Resume R_4 = new Resume(UUID4, "Aaron Paul");

    AbstractStorageTest(Storage test) {
        storage = test;
    }

    public void fillResume(Resume resume) {
        String url1 = "http://www.harvard.edu";
        String url2 = "http://www.cambridge.com";
        String description = Math.random() > 0.5 ? "Description" : null;
        resume.addContact(ContactType.EMAIL,
                resume.getFullName().replaceAll("\\s", "") +
                (int)(Math.random() * 1000) +
                "@email.com");
        resume.addContact(
                ContactType.PHONE,
                "123-456-789-" +
                resume.getFullName().charAt(0) +
                resume.getFullName().charAt(6) +
                (int)(Math.random() * 1000));
        Section objective = new StringSection("Team-lead programmer");
        Section personal = new StringSection("Personal info");
        resume.getPersonInfo().put(SectionType.OBJECTIVE, objective);
        resume.getPersonInfo().put(SectionType.PERSONAL, personal);
        Section achievements = new ListSection(Arrays.asList("Achievement1", "Achievement2", "Achievement3"));
        resume.getPersonInfo().put(SectionType.ACHIEVEMENTS, achievements);

        Organization.Position pos1 = new Organization.Position(
                "Student", LocalDate.of(2000, 1, 1), LocalDate.of(2001, 2, 2), description);
        Organization.Position pos2 = new Organization.Position(
                "Assistant", LocalDate.of(2001, 2, 2), LocalDate.of(2003, 5, 4), description);
        Organization.Position pos3 = new Organization.Position(
                "Java Junior Developer", LocalDate.of(2010, 6, 1), LocalDate.of(2012, 8, 1), description);
        Organization org1 = new Organization(new Link("Harvard", url1), pos1, pos2);
        Organization org2 = new Organization(new Link("Cambridge", url2));
        Organization org3 = new Organization(new Link("Google", "google.com"), pos3);
        org2.addPosition("Laboratory assistant", null, LocalDate.of(2005, 2, 1), LocalDate.of(2008, 9, 1));
        Section education = new OrganizationSection(Arrays.asList(org1, org2));
        Section experience = new OrganizationSection(Collections.singletonList(org3));
        resume.getPersonInfo().put(SectionType.EDUCATION, education);
        resume.getPersonInfo().put(SectionType.EXPERIENCE, experience);
    }

    @Before
    public void setUp() {
        fillResume(R_1);
        fillResume(R_2);
        fillResume(R_3);
        fillResume(R_4);
        storage.clear();
        storage.save(R_1);
        storage.save(R_2);
        storage.save(R_3);
    }

    @Test
    public void clear() {
        storage.clear();
        List<Resume> result = new ArrayList<>();
        assertEquals(result, storage.getAllSorted());
        assertEquals(0, storage.size());
    }

    @Test
    public void saveSuccess() {
        storage.save(R_4);
        assertEquals(R_4, storage.get(R_4.getUuid()));
        assertEquals(4, storage.size());
    }

    @Test(expected = ExistException.class)
    public void saveFailed() {
        storage.save(R_2);
    }

    @Test
    public void updateSuccess() {
        Resume r5 = new Resume(R_2.getUuid(), "fullNameUpdated");
        r5.addContact(ContactType.PHONE, "123-456-789-FNU");
        r5.addContact(ContactType.EMAIL, "fullNameUpdated@email.com");
        storage.update(r5);
        assertEquals(r5, storage.get(R_2.getUuid()));
    }

    @Test(expected = NotExistException.class)
    public void updateFailed() {
        storage.update(R_4);
    }

    @Test
    public void getSuccess() {
        assertEquals(R_1, storage.get(R_1.getUuid()));
    }

    @Test(expected = NotExistException.class)
    public void getFailed() {
        storage.get("DUMMY_e2-1ac7-48f2-9d7f-7e111087f111");
    }

    @Test(expected = NotExistException.class)
    public void deleteSuccess() {
        storage.delete(R_2.getUuid());
        assertEquals(2, storage.size());
        storage.get(R_2.getUuid());
    }

    @Test(expected = NotExistException.class)
    public void deleteFailed() {
        storage.delete(R_4.getUuid());
    }

    @Test
    public void getAllSorted() {
        storage.save(R_4);
        List<Resume> result = new ArrayList<>();
        Collections.addAll(result, R_1, R_4, R_3, R_2);
        List<Resume> test1 = storage.getAllSorted();
        assertEquals(result, test1);
    }

    @Test
    public void size() {
        assertEquals(3, storage.size());
    }
}
