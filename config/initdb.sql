CREATE TABLE resume (
    uuid CHAR(36) NOT NULL,
    full_name TEXT NOT NULL,
    PRIMARY KEY(uuid)
    );

CREATE table contact (
    contact_id SERIAL PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL UNIQUE,
    FOREIGN KEY (uuid) REFERENCES resume (uuid) ON DELETE CASCADE
);

CREATE TABLE section (
  section_id SERIAL PRIMARY KEY,
  uuid CHAR(36) NOT NULL,
  type TEXT NOT NULL,
  value TEXT NOT NULL,
  FOREIGN KEY (uuid) REFERENCES resume(uuid) ON DELETE CASCADE
);

CREATE TABLE organization(
    org_id SERIAL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    url TEXT
);

CREATE TABLE education (
    id SERIAL PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    org_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    title TEXT NOT NULL,
    value TEXT,
    FOREIGN KEY (uuid) REFERENCES resume(uuid) ON DELETE CASCADE,
    FOREIGN KEY (org_id) REFERENCES organization(org_id),
    CHECK ( start_date < end_date )
);

CREATE TABLE experience (
    id SERIAL PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    org_id INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    title TEXT NOT NULL,
    value TEXT,
    FOREIGN KEY (uuid) REFERENCES resume(uuid) ON DELETE CASCADE,
    FOREIGN KEY (org_id) REFERENCES organization(org_id),
    CHECK ( start_date < end_date )
);

CREATE EXTENSION PLPGSQL;

CREATE FUNCTION add_org(org_name TEXT, org_url TEXT) RETURNS SETOF INTEGER AS $FUNC$
DECLARE
    id int;
BEGIN
    id = (SELECT max(org_id) FROM organization);
    IF id IS NULL THEN id = 1;
    ELSE id = id + 1;
    END IF;
    RETURN QUERY INSERT INTO organization VALUES (id, org_name, org_url) RETURNING org_id;
EXCEPTION
    WHEN SQLSTATE '23505' THEN
        IF org_url IS NOT NULL AND (SELECT url FROM organization WHERE name = org_name) IS NULL
        THEN UPDATE organization SET url = org_url WHERE name = org_name;
        END IF;
        RETURN QUERY SELECT org_id FROM organization WHERE name = org_name;
END;
$FUNC$
    LANGUAGE PLPGSQL;
