package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.Executor;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

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
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact WHERE uuid = ?; DELETE FROM section WHERE uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getUuid());
                ps.execute();
            }
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
                resume.setUuid(uuid);
                resume.setFullName(rs.getString("full_name"));
                do {
                    if (rs.getString("type") != null) resume.addContact(
                            ContactType.valueOf(rs.getString("type").toUpperCase()), rs.getString("value"));
                } while (rs.next());
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM section WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSectionToResume(SectionType.valueOf(rs.getString("type")), rs.getString("value"), resume);
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
        Map<String, Resume> resumes = new HashMap<>();
        helper.prepareTransaction(connection -> {
            setFromQuery("SELECT * FROM resume ORDER BY full_name, uuid", connection, set ->
                    resumes.put(set.getString("uuid"), new Resume(set.getString("uuid"), set.getString("full_name"))));
            setFromQuery("SELECT * FROM contact", connection, set ->
                    resumes.get(set.getString("uuid")).addContact(ContactType.valueOf(set.getString("type").toUpperCase()), set.getString("value")));
            setFromQuery("SELECT * FROM section", connection, set ->
                    addSectionToResume(SectionType.valueOf(set.getString("type")), set.getString("value"), resumes.get(set.getString("uuid"))));
            log.info("List of resumes successfully received");
            return null;
        });
        List<Resume> list = new ArrayList<>(resumes.values());
        Collections.sort(list);
        return list;
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
                insertion.insert(ps, element);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertIntoContactsTable(Connection connection, Resume resume) throws SQLException {
        insertIntoTable("INSERT INTO contact (uuid, type, value) VALUES (?, ?, ?)",
                connection, resume, resume.getContacts().entrySet(), (ps, pair) ->
                        ps.setString(3, (String) pair.getValue()));
    }

    private void insertIntoSectionTable(Connection connection, Resume resume) throws SQLException {
        insertIntoTable("INSERT INTO section (uuid, type, value) VALUES (?, ?, ?)",
                connection, resume, resume.getPersonInfo().entrySet(), (ps, pair) -> {
                    SectionType type = (SectionType) pair.getKey();
                    switch (type) {
                        case OBJECTIVE:
                        case PERSONAL: {
                            ps.setString(3, ((StringSection) pair.getValue()).getContent());
                            break;
                        }
                        case ACHIEVEMENTS:
                        case QUALIFICATION: {
                            ps.setString(3, String.join("\n", ((ListSection) pair.getValue()).getContent()));
                            break;
                        }
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

    @FunctionalInterface
    private interface Addition {
        void add(ResultSet set) throws SQLException;
    }

    @FunctionalInterface
    private interface Insertion {
        void insert(PreparedStatement ps, Map.Entry pair) throws SQLException;
    }
}