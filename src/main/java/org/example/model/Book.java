package org.example.model;

import org.bson.types.ObjectId;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 * 
 * @author anderslm@kth.se
 */
public class Book implements Comparable<Book>{
    private ObjectId id;
    private String title;
    private String isbn; // should check format
    private LocalDate publishDate;
    private String genre;
    private String storyLine = "";
    private List<Author> authors;
    private int grade;
    // TODO:
    // Add authors, and corresponding methods, to your implementation 
    // as well, i.e. "private ArrayList<Author> authors;"

    public Book(ObjectId id, String isbn,String genre, int grade, LocalDate publishDate,String title) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre.toUpperCase();
        this.storyLine = storyLine;
        this.authors = new ArrayList<>();
        if (grade<1 || grade>5) throw new IllegalArgumentException("Rating is between 1-5");
        this.grade = grade;
    }

//    public Book(ObjectId id, String isbn,String genre, int grade, LocalDate published,String title ) {
//        this(id,isbn,genre,grade,published,title);
//    }
    
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public void setTitle(String title){
        this.title = title;
    }
    public void setPublishDate(LocalDate date){
        this.publishDate = date;
    }
    public void setGenre(String genre){
        this.genre = genre;
    }
    public void setGrade(int grade){
        this.grade = grade;
    }
    public LocalDate getPublishDate() { return publishDate; }
    public String getGenre() { return genre; }
    public String getStoryLine() { return storyLine; }
    public int getGrade() { return grade; }
    public ObjectId getId(){
        return id;
    }

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
