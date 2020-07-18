package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class SqlStorage implements Storage {

    private static final Logger log = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper = new SqlHelper(
            Config.get().getUrl(), Config.get().getUser(), Config.get().getPassword());

    @Override
    public void clear() {
        helper.prepare("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
        log.info("Storage cleared");
    }

    @Override
    public void save(Resume resume) {
        helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume VALUES(?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            }
            insertIntoContactsTable(connection, resume);
            insertIntoSectionTable(connection, resume);
            log.info("Resume successfully added");
            return null;
        });
    }

    @Override
    public void update(Resume resume) {
        helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) throw new NotExistException(resume.getUuid());
            }
            deleteToUpdate(connection, resume.getUuid());
            insertIntoContactsTable(connection, resume);
            insertIntoSectionTable(connection, resume);
            return null;
        });
        log.info("Resume successfully updated");
    }

    @Override
    public Resume get(String uuid) {
        return helper.prepareTransaction(connection -> {
            Resume resume = new Resume();
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM resume r LEFT JOIN contact c ON r.uuid = c.uuid WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    log.info("Resume doesn't exist");
                    throw new NotExistException(uuid);
                }
                String fullName = rs.getString("full_name");
                resume.setUuid(uuid);
                resume.setFullName(fullName);
                do {
                    String type = rs.getString("type");
                    if (type != null) {
                        ContactType contactType = ContactType.valueOf(rs.getString("type").toUpperCase());
                        String value = rs.getString("value");
                        resume.addContact(contactType, value);
                    }
                } while (rs.next());
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM section WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    SectionType sectionType = SectionType.valueOf(rs.getString("type"));
                    String value = rs.getString("value");
                    addSectionToResume(sectionType, value, resume);
                }
            }
            log.info("Resume successfully receive");
            return resume;
        });
    }

    @Override
    public void delete(String uuid) {
        helper.prepare("DELETE FROM resume WHERE uuid = ?", ps -> {
            ps.setString(1, uuid);
            if (ps.executeUpdate() == 0) {
                log.info("Resume doesn't exist");
                throw new NotExistException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return new ArrayList<>(helper.prepareTransaction(connection -> {
            Map<String, Resume> resumes = new LinkedHashMap<>();
            setFromQuery("SELECT * FROM resume ORDER BY full_name, uuid", connection, set -> {
                String uuid = set.getString("uuid");
                String fullName = set.getString("full_name");
                resumes.put(uuid, new Resume(uuid, fullName));
            });
            setFromQuery("SELECT * FROM contact", connection, set -> {
                String uuid = set.getString("uuid");
                ContactType type = ContactType.valueOf(set.getString("type").toUpperCase());
                String value = set.getString("value");
                resumes.get(uuid).addContact(type, value);
            });
            setFromQuery("SELECT * FROM section", connection, set -> {
                String uuid = set.getString("uuid");
                SectionType type = SectionType.valueOf(set.getString("type"));
                String value = set.getString("value");
                addSectionToResume(type, value, resumes.get(uuid));
            });
            log.info("List of resumes successfully received");
            return resumes;
        }).values());
    }

    @Override
    public int size() {
        return helper.prepare("SELECT COUNT(uuid) AS size FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("size");
        });
    }

    private <X, Y> void insertIntoTable(String query, Connection connection, Resume resume, Set<Map.Entry<X, Y>> set, Insertion insertion) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            for (Map.Entry<X, Y> element : set) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, String.valueOf(element.getKey()));
                String value = insertion.insert(ps, element);
                ps.setString(3, value);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertIntoContactsTable(Connection connection, Resume resume) throws SQLException {
        insertIntoTable("INSERT INTO contact (uuid, type, value) VALUES (?, ?, ?)",
                connection, resume, resume.getContacts().entrySet(), (ps, pair) -> (String) pair.getValue());
    }

    private void insertIntoSectionTable(Connection connection, Resume resume) throws SQLException {
        insertIntoTable("INSERT INTO section (uuid, type, value) VALUES (?, ?, ?)",
                connection, resume, resume.getPersonInfo().entrySet(), (ps, pair) -> {
                    SectionType type = (SectionType) pair.getKey();
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL: {
                            return ((StringSection) pair.getValue()).getContent();
                        }
                        case ACHIEVEMENTS:
                        case QUALIFICATION: {
                            return String.join("\n", ((ListSection) pair.getValue()).getContent());
                        }
                        default:
                            throw new IllegalArgumentException(type.toString());
                    }
                });
    }

    private void addSectionToResume(SectionType section, String value, Resume resume) {
        switch (section) {
            case PERSONAL:
            case OBJECTIVE: {
                resume.getPersonInfo().put(section, new StringSection(value));
                break;
            }
            case ACHIEVEMENTS:
            case QUALIFICATION: {
                ListSection list = new ListSection();
                for (String element : value.split("\n")) {
                    list.addItem(element);
                }
                resume.getPersonInfo().put(section, list);
                break;
            }
        }
    }

    private void setFromQuery(String query, Connection connection, Addition addition) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ResultSet set = ps.executeQuery();
            while (set.next()) {
                addition.add(set);
            }
        }
    }

    private void deleteToUpdate(Connection connection, String uuid) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM section WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    @FunctionalInterface
    private interface Addition {
        void add(ResultSet set) throws SQLException;
    }

    @FunctionalInterface
    private interface Insertion {
        String insert(PreparedStatement ps, Map.Entry pair) throws SQLException;
    }
}