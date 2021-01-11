package org.example.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 * 
 * @author anderslm@kth.se
 */
public class Book implements Comparable<Book>{

    private String title;
    private String isbn; // should check format
    private Date publishDate;
    private String genre;
    private String storyLine = "";
    private List<Author> authors;
    private int grade;
    // TODO:
    // Add authors, and corresponding methods, to your implementation 
    // as well, i.e. "private ArrayList<Author> authors;"

    public Book(String title, String isbn, Date publishDate, String genre, String storyLine, int grade) {
        this.title = title;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre.toUpperCase();
        this.storyLine = storyLine;
        this.authors = new ArrayList<>();
        if (grade<1 || grade>5) throw new IllegalArgumentException("Rating is between 1-5");
        this.grade = grade;
    }

    public Book(String title, String isbn, Date published, String genre, int grade) {
        this(title, isbn, published, genre, "this is a book",grade);
    }
    
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public void setTitle(String title){
        this.title = title;
    }
    public void setPublishDate(Date date){
        this.publishDate = date;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }
    public void setGrade(int grade){
        this.grade = grade;
    }
    public Date getPublishDate() { return publishDate; }
    public String getGenre() { return genre; }
    public String getStoryLine() { return storyLine; }
    public int getGrade() { return grade; }

    public void addAuthors(List<Author> authorsToAdd){
        if (authorsToAdd != null){
            authors.addAll(authorsToAdd);
        }
    }

    public void removeAuthors(){
        authors.clear();
    }

    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public List<Author> getAuthors(){
        return new ArrayList<>(authors);
    }

    public void removeAuthor(Author author){
        if (authors.contains(author)){
            authors.remove(author);
        }
    }

    public void addAuthorToBook(Author newAuthor){
        this.authors.add(newAuthor);
    }

    @Override
    public boolean equals(Object object){
        if (object != null && object instanceof Book){
            if (((Book) object).isbn.equals(this.isbn)){
                return true;
            }
        }
        return false;
    }

    @Override
    public int compareTo(Book o) {
        return o.getGrade()-this.grade;
    }

    @Override
    public String toString() {
        return "Book{" +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", published=" + publishDate +
                ", genre='" + genre + '\'' +
                ", storyLine='" + storyLine + '\'' +
                ", authors=" + authors +
                ", grade=" + grade +
                '}';
    }


}
