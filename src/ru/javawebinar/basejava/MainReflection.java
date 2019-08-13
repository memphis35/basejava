package ru.javawebinar.basejava;

import ru.javawebinar.basejava.model.Resume;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Resume resume = new Resume("getReflection");
        Method method = resume.getClass().getMethod("toString");
        System.out.println(method.invoke(resume));
    }
}
