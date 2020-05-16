package ru.javawebinar.basejava.storage;

import org.junit.*;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.util.DateUtil;

import java.io.File;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    public static final File STORAGE_DIR = new File(".\\storage");
    final Storage storage;
    private final static Resume R_1 = new Resume("uuid1", "Aaron Paul");
    private final static Resume R_2 = new Resume("uuid2", "Nikki Six");
    private final static Resume R_3 = new Resume("uuid3", "Micky Mars");
    private final static Resume R_4 = new Resume("uuid4", "Aaron Paul");

    AbstractStorageTest(Storage test) {
        storage = test;
    }

    public void fillResume(Resume resume) {
        resume.getContacts().put(ContactType.EMAIL, resume.getFullName().replaceAll("\\s", "") + "@email.com");
        resume.getContacts().put(ContactType.PHONE, "123-456-789-" + resume.getFullName().charAt(0) + resume.getFullName().charAt(6));
        Section objective = new StringSection("Team-lead programmer");
        resume.getPersonInfo().put(SectionType.OBJECTIVE, objective);
        Section achievements = new ListSection(Arrays.asList("Achievement1", "Achievement2", "Achievement3"));
        resume.getPersonInfo().put(SectionType.ACHIEVEMENTS, achievements);
        Organization.Position pos1 = new Organization.Position(
                "Student",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 2, 2),
                "Description of study place");
        Organization org = new Organization(new Link("Harvard", "http://www.harvard.edu"), pos1);
        Section education = new OrganizationSection(Arrays.asList(org));
        resume.getPersonInfo().put(SectionType.EDUCATION, education);
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
        fillResume(R_1);
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
        storage.get(R_4.getUuid());
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
