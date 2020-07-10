CREATE TABLE resume (
    "uuid" CHAR(36) NOT NULL,
    full_name TEXT NOT NULL,
    PRIMARY KEY("uuid")
    );

CREATE table contact (
    "uuid" CHAR(36) NOT NULL,
    type TEXT NOT NULL,
    value TEXT NOT NULL UNIQUE,
    FOREIGN KEY ("uuid") REFERENCES resume ("uuid") ON DELETE CASCADE
);
