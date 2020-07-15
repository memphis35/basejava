package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.nio.MappedByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
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
            insertContacts(connection, resume);
            insertSection(connection, resume);
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
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact WHERE uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            insertContacts(connection, resume);
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM section WHERE uuid = ?")) {
                ps.setString(1, resume.getUuid());
                ps.execute();
            }
            insertSection(connection, resume);
            return null;
        });
        log.info("Resume successfully updated");
    }

    @Override
    public Resume get(String uuid) {
        return helper.prepareTransaction(connection -> {
            Resume resume = new Resume();
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT r.full_name, c.type, c.value FROM resume r LEFT JOIN contact c ON r.uuid = c.uuid WHERE r.uuid = ?")) {
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
                    "SELECT full_name, type, value FROM resume NATURAL JOIN section WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSection(rs, resume);
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
        List<Resume> resumes = new ArrayList<>();
        return helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT uuid, type, value FROM contact NATURAL JOIN resume ORDER BY full_name, uuid")) {
                addInformation(ps, resumes, "Contact");
            }
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT uuid, type, value FROM section NATURAL JOIN resume ORDER BY full_name, uuid")) {
                addInformation(ps, resumes, "Personal Info");
            }
            log.info("List of resumes successfully received");
            return resumes;
        });
    }

    @Override
    public int size() {
        return helper.prepare("SELECT COUNT(uuid) AS size FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("size");
        });
    }

    private void insertContacts(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact (uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> pair : resume.getContacts().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, pair.getKey().getTitle());
                ps.setString(3, pair.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void insertSection(Connection connection, Resume resume) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO section (uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<SectionType, Section> pair : resume.getPersonInfo().entrySet()) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, pair.getKey().getTitle());
                ps.setString(3, pair.getValue().toString());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void addSection(ResultSet set, Resume resume) throws SQLException {
        SectionType section = SectionType.getType(set.getString("type"));
        switch (section) {
            case PERSONAL:
            case OBJECTIVE: {
                resume.addSection(section, new StringSection(set.getString("value")));
                break;
            }
            case ACHIEVEMENTS:
            case QUALIFICATION: {
                ListSection list = new ListSection();
                for (String element : set.getString("value").split("\n")) {
                    list.addItem(element);
                }
                resume.addSection(section, list);
            }
        }
    }

    private List<Resume> addInformation(PreparedStatement ps, List<Resume> resumes, String type) throws SQLException {
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) return resumes;
        for (int i = 0; i < resumes.size(); ) {
            if (resumes.get(i).getUuid().equals(rs.getString("uuid"))) {
                switch (type) {
                    case "Contact":
                        resumes.get(i).addContact(ContactType.valueOf(rs.getString("type").toUpperCase()), rs.getString("value"));
                        break;
                    case "Personal Info":
                        addSection(rs, resumes.get(i));
                        break;
                    default:
                        throw new IllegalArgumentException(type);
                }
                if (!rs.next()) break;
            } else {
                i++;
            }
        }
        return resumes;
    }
}