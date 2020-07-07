package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.model.ContactType;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SqlStorage implements Storage {

    private static final Logger log = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper = new SqlHelper(
            Config.get().getUrl(), Config.get().getUser(), Config.get().getPassword());

    @Override
    public void clear() {
        helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact")) {
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM resume")) {
                ps.execute();
            }
            log.info("Storage cleared");
            return null;
        });
    }

    @Override
    public void save(Resume resume) {
        helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("INSERT INTO resume VALUES(?, ?)")) {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
            } catch (SQLException e) {
                if (e.getSQLState().equals("23505")) {
                    log.info("Resume " + resume.getUuid() + " already exist.");
                    throw new ExistException(resume.getUuid());
                } else {
                    throw e;
                }
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
                if (ps.executeUpdate() == 0) {
                    log.info("Resume doesn't exist");
                    throw new NotExistException(resume.getUuid());
                }
            }
            try (PreparedStatement ps = connection.prepareStatement("UPDATE contact SET value = ? WHERE uuid = ? AND type = ?")) {
                for (Map.Entry<ContactType, String> pair : resume.getContacts().entrySet()) {
                    ps.setString(1, pair.getValue());
                    ps.setString(2, resume.getUuid());
                    ps.setString(3, pair.getKey().getTitle());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
            log.info("Resume successfully updated");
            return null;
        });
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
        helper.prepareTransaction(connection -> {
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM contact WHERE uuid = ?")) {
                ps.setString(1, uuid);
                ps.execute();
            }
            try (PreparedStatement ps = connection.prepareStatement("DELETE FROM resume WHERE uuid = ?")) {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    log.info("Resume doesn't exist");
                    throw new NotExistException(uuid);
                }
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.prepare("SELECT * FROM resume ORDER BY full_name", ps -> {
            List<Resume> resumes = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resumes.add(get(rs.getString("uuid")));
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