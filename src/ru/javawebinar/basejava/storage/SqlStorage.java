package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact VALUES (?, ?, ?)")) {
                for (Map.Entry<ContactType, String> pair : resume.getContacts().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, pair.getKey().getTitle());
                    ps.setString(3, pair.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
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
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO contact VALUES (?, ?, ?)")) {
                for (Map.Entry<ContactType, String> pair : resume.getContacts().entrySet()) {
                    ps.setString(1, resume.getUuid());
                    ps.setString(2, pair.getKey().getTitle());
                    ps.setString(3, pair.getValue());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            return null;
        });
        log.info("Resume successfully updated");
    }

    @Override
    public Resume get(String uuid) {
        return helper.prepareTransaction(connection -> {
            Resume result = new Resume();
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT r.full_name, c.type, c.value " +
                            "FROM resume AS r " +
                            "NATURAL JOIN contact AS c " +
                            "WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    log.info("Resume doesn't exist");
                    throw new NotExistException(uuid);
                }
                result.setUuid(uuid);
                result.setFullName(rs.getString("full_name"));
                do {
                    result.addContact(ContactType.valueOf(rs.getString("type").toUpperCase()), rs.getString("value"));
                } while (rs.next());
            }
            log.info("Resume successfully receive");
            return result;
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
                ResultSet rs = ps.executeQuery();
                rs.next();
                for (int i = 0; i < resumes.size();) {
                    if (resumes.get(i).getUuid().equals(rs.getString("uuid"))) {
                        resumes.get(i).addContact(
                                ContactType.valueOf(rs.getString("type").toUpperCase()),
                                rs.getString("value"));
                        if (!rs.next()) break;
                    } else {
                        i++;
                    }
                }
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
}