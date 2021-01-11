package org.example.model;
import java.sql.Date;

public class Author {
    private int authorID;
    private String firstName;
    private String lastName;
    private Date birthday;

    public Author(int authorID,String firstName,String lastName, Date birthday) {
        this.authorID = authorID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
    }

    public int getAuthorID() {
        return authorID;
    }

    public void setAuthorID(int authorID) {this.authorID = authorID;}

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public Date getBirthday() {
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
