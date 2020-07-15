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
