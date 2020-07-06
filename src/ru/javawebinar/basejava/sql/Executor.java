package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;

public interface Executor {

    void execute(PreparedStatement ps);
}
