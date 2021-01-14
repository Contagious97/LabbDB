package org.example.model;
import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDate;

public class Author {
    private ObjectId authorID;
    private String firstName;
    private String lastName;
    private LocalDate birthday;

    public Author(ObjectId authorID,String firstName,String lastName, LocalDate birthday) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public ObjectId getAuthorID() {
        return authorID;
    }

    public void setAuthorID(ObjectId authorID) {
        this.authorID = authorID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public LocalDate getBirthday() {
        return birthday;
    }

    @Override
    public String toString() {
        return "Author{" +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
