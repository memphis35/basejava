package ru.javawebinar.basejava.storage;

import com.sun.org.apache.xpath.internal.operations.Or;
import ru.javawebinar.basejava.Config;
import ru.javawebinar.basejava.exception.NotExistException;
import ru.javawebinar.basejava.exception.StorageException;
import ru.javawebinar.basejava.model.*;
import ru.javawebinar.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
            insertContacts(connection, resume);
            insertPersonInfo(connection, resume);
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
            insertContacts(connection, resume);
            insertPersonInfo(connection, resume);
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
            addOrganizationToResume(connection, resume, SectionType.EDUCATION);
            addOrganizationToResume(connection, resume, SectionType.EXPERIENCE);
            log.info("Resume successfully receive");
            return resume;
        });
    }

    private void addOrganizationToResume(Connection connection, Resume resume, SectionType type) throws SQLException {
        String query = type.equals(SectionType.EDUCATION) ?
                "SELECT * FROM education NATURAL JOIN organization WHERE uuid = ? ORDER BY start_date, org_id" :
                "SELECT * FROM experience NATURAL JOIN organization WHERE uuid = ? ORDER BY start_date, org_id";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, resume.getUuid());
            ResultSet rs = ps.executeQuery();
            OrganizationSection orgSection = new OrganizationSection();
            while (rs.next()) {
                String name = rs.getString("name");
                String url = rs.getString("url");
                Organization org = new Organization(new Link(name, url));
                String title = rs.getString("title");
                String description = rs.getString("value");
                LocalDate start_date = getLocalDate(rs.getDate("start_date"));
                LocalDate end_date = getLocalDate(rs.getDate("end_date"));
                Organization.Position position = new Organization.Position(title, start_date, end_date, description);
                orgSection.addPosition(org, position);
            }
            if (orgSection.getContent().size() > 0) resume.getPersonInfo().put(type, orgSection);
        }
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
            setFromQuery("SELECT * FROM education NATURAL JOIN organization", connection, set -> {
                String uuid = set.getString("uuid");
                Map<String, Object> value = formRequest(set);
                this.addSectionToResume(SectionType.EDUCATION, value, resumes.get(uuid));
            });
            setFromQuery("SELECT * FROM experience NATURAL JOIN organization", connection, set -> {
                String uuid = set.getString("uuid");
                Map<String, Object> value = formRequest(set);
                this.addSectionToResume(SectionType.EXPERIENCE, value, resumes.get(uuid));
            });
            log.info("List of resumes successfully received");
            return resumes;
        }).values());
    }

    private Map<String, Object> formRequest(ResultSet set) throws SQLException {
        Map<String, Object> result = new HashMap<>();
        result.put("org_name", set.getString("name"));
        result.put("org_url", set.getString("url"));
        result.put("title", set.getString("title"));
        result.put("description", set.getString("value"));
        result.put("start_date", getLocalDate(set.getDate("start_date")));
        result.put("end_date", getLocalDate(set.getDate("end_date")));
        return result;
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
        for (Map.Entry<ContactType, String> element : resume.getContacts().entrySet()) {
            insertIntoTable("INSERT INTO contact (uuid, type, value) VALUES (?, ?, ?)",
                    connection, resume, element, (ps, pair) -> (String) pair.getValue());
        }
    }

    private void insertPersonInfo(Connection connection, Resume resume) throws SQLException {
        for (Map.Entry<SectionType, Section> entry : resume.getPersonInfo().entrySet()) {
            SectionType type = entry.getKey();
            switch (type) {
                case PERSONAL:
                case OBJECTIVE: {
                    insertIntoTable("INSERT INTO section (uuid, type, value) VALUES (?, ?, ?)",
                            connection, resume, entry, (ps, pair) -> ((StringSection) pair.getValue()).getContent());
                    break;
                }
                case ACHIEVEMENTS:
                case QUALIFICATION: {
                    insertIntoTable("INSERT INTO section (uuid, type, value) VALUES (?, ?, ?)",
                            connection, resume, entry, (ps, pair) -> String.join("\n", ((ListSection) pair.getValue()).getContent()));
                    break;
                }
                case EDUCATION: {
                    insertIntoTable("INSERT INTO education VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", connection, resume, entry);
                    break;
                }
                case EXPERIENCE: {
                    insertIntoTable("INSERT INTO experience VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)", connection, resume, entry);
                    break;
                }
                default:
                    throw new IllegalArgumentException(entry.getKey().toString());
            }
        }
    }

    private void insertIntoTable(String query, Connection connection, Resume resume, Map.Entry pair, Insertion insertion) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, resume.getUuid());
            ps.setString(2, String.valueOf(pair.getKey()));
            String value = insertion.insert(ps, pair);
            ps.setString(3, value);
            ps.execute();
        }
    }

    private void insertIntoTable(String query, Connection connection, Resume resume, Map.Entry<SectionType, Section> pair) throws SQLException {
        List<Organization> orgs = ((OrganizationSection) pair.getValue()).getContent();
        for (Organization org : orgs) {
            int org_id = getOrganizationID(connection, org);
            List<Organization.Position> positions = org.getPositions();
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                for (Organization.Position position : positions) {
                    ps.setString(1, resume.getUuid());
                    ps.setInt(2, org_id);
                    ps.setDate(3, getSqlDate(position.getStartDate()));
                    ps.setDate(4, getSqlDate(position.getEndDate()));
                    ps.setString(5, position.getTitle());
                    ps.setString(6, position.getDescription());
                    ps.addBatch();
                }
                ps.executeBatch();
            }
        }
    }

    private <TYPE> void addSectionToResume(SectionType section, TYPE value, Resume resume) {
        switch (section) {
            case PERSONAL:
            case OBJECTIVE: {
                resume.getPersonInfo().put(section, new StringSection((String) value));
                break;
            }
            case ACHIEVEMENTS:
            case QUALIFICATION: {
                ListSection list = new ListSection();
                for (String element : ((String) value).split("\n")) {
                    list.addItem(element);
                }
                resume.getPersonInfo().put(section, list);
                break;
            }
            case EXPERIENCE:
            case EDUCATION: {
                Map<String, Object> data = (Map<String, Object>) value;
                if (!resume.getPersonInfo().containsKey(section)) resume.getPersonInfo().put(section, new OrganizationSection());
                OrganizationSection orgSection = (OrganizationSection) resume.getPersonInfo().get(section);
                String orgName = (String) data.get("org_name");
                String orgUrl = (String) data.get("org_url");
                Organization org = new Organization(new Link(orgName, orgUrl));
                String title = (String) data.get("title");
                String description = (String) data.get("description");
                LocalDate startDate = (LocalDate) data.get("start_date");
                LocalDate endDate = (LocalDate) data.get("end_date");
                Organization.Position position = new Organization.Position(title, startDate, endDate, description);
                orgSection.addPosition(org, position);
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
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM education WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
        try (PreparedStatement ps = connection.prepareStatement("DELETE FROM experience WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.execute();
        }
    }

    private int getOrganizationID(Connection connection, Organization org) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("SELECT add_org(?, ?) AS org_id")) {
            ps.setString(1, org.getHomepage().getName());
            ps.setString(2, org.getHomepage().getUrl());
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("org_id");
        }
    }

    private java.sql.Date getSqlDate(LocalDate date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date result = sdf.parse(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
            return new java.sql.Date(result.getTime());
        } catch (ParseException e) {
            log.warning("Getting error while parsing date.");
            throw new StorageException(e.getMessage(), e);
        }
    }

    private LocalDate getLocalDate(java.sql.Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return LocalDate.parse(sdf.format(date), DateTimeFormatter.ISO_LOCAL_DATE);
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