package org.example.model;

import java.time.LocalDate;

public class Author {
    private final String name;
    private final String ssn;

    public Author(String name, String ssn) {
        this.name = name;
        this.ssn = ssn;
    }

    public String getName() {
        return name;
    }

    public String getSsn() {
        return ssn;
    }

    @Override
    public String toString() {
        return "Author{" +
                "name='" + name + '\'' +
                ", ssn='" + ssn + '\'';
    }
}
