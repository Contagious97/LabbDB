package org.example.model;

import java.time.LocalDate;

public class Author {
    private final String name;
    private final int age;

    public Author(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getSsn() {
        return age;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'';
    }
}
