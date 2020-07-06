package ru.javawebinar.basejava.storage;

import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.ExistException;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.Resume;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class SqlStorage implements Storage {

    private static final Logger LOGGER = Logger.getLogger(SqlStorage.class.getName());
    private final SqlHelper helper = new SqlHelper(
            Config.get().getUrl(), Config.get().getUser(), Config.get().getPassword());

    @Override
    public void clear() {
        helper.prepare("DELETE FROM resume", ps -> {
            try {
                ps.execute();
                LOGGER.info("Storage cleared");
                return null;
            } catch (SQLException e) {
                LOGGER.warning("SQL Exception: " + e.getSQLState());
                throw new StorageException(e.getSQLState(), e);
            }
        });
    }

    @Override
    public void save(Resume resume) {
        helper.prepare("INSERT INTO resume VALUES(?, ?)", ps -> {
            try {
                ps.setString(1, resume.getUuid());
                ps.setString(2, resume.getFullName());
                ps.execute();
                LOGGER.info("Resume successfully added");
                return null;
            } catch (SQLIntegrityConstraintViolationException e) {
                LOGGER.info("Resume " + resume.getUuid() + " already exist.");
                throw new ExistException(resume.getUuid());
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public void update(Resume resume) {
        helper.prepare("UPDATE resume SET full_name = ? WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, resume.getFullName());
                ps.setString(2, resume.getUuid());
                if (ps.executeUpdate() == 0) {
                    LOGGER.info("Resume doesn't exist");
                    throw new NotExistException(resume.getUuid());
                }
                LOGGER.info("Resume successfully updated");
                return null;
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });

    }

    @Override
    public Resume get(String uuid) {
        return helper.prepare("SELECT * FROM resume WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    LOGGER.info("Resume doesn't exist");
                    throw new NotExistException(uuid);
                }
                LOGGER.info("Resume successfully receive");
                return new Resume(uuid, rs.getString("full_name"));
            } catch (SQLException e) {
                LOGGER.warning("SQL Exception: " + e.getSQLState());
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public void delete(String uuid) {
        helper.prepare("DELETE FROM resume WHERE uuid = ?", ps -> {
            try {
                ps.setString(1, uuid);
                if (ps.executeUpdate() == 0) {
                    LOGGER.info("Resume doesn't exist");
                    throw new NotExistException(uuid);
                }
                return null;
            } catch (SQLException e) {
                LOGGER.warning("SQL Exception: " + e.getSQLState());
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return helper.prepare("SELECT * FROM resume ORDER BY full_name", ps -> {
            try {
                List<Resume> resumes = new ArrayList<>();
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.add(new Resume(rs.getString("uuid"), rs.getString("full_name")));
                }
                LOGGER.info("List of resumes successfully received");
                return resumes;
            } catch (SQLException e) {
                LOGGER.warning("SQL Exception: " + e.getSQLState());
                throw new StorageException(e.getMessage(), e);
            }
        });
    }

    @Override
    public int size() {
        return helper.prepare("SELECT COUNT(uuid) AS size FROM resume", ps -> {
            try {
                ResultSet rs = ps.executeQuery();
                rs.next();
                return rs.getInt("size");
            } catch (SQLException e) {
                throw new StorageException(e.getMessage(), e);
            }
        });
    }
}