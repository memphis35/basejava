package ru.javawebinar.basejava.sql;

import java.sql.PreparedStatement;

public interface Executor<TYPE> {

    TYPE execute(PreparedStatement ps);
}
