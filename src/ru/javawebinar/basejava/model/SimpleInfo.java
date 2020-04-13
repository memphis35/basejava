package ru.javawebinar.basejava.model;

public class SimpleInfo extends Section<String> {

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return data;
    }

}
