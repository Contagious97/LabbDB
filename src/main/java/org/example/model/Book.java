package org.example.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Representation of a book.
 * 
 * @author anderslm@kth.se
 */
public class Book {

    private int bookId;
    private String title;
    private String isbn; // should check format
    private Date published;
    private String genre;
    private String storyLine = "";
    private List<Author> authors;
    private int rating;
    // TODO:
    // Add authors, and corresponding methods, to your implementation 
    // as well, i.e. "private ArrayList<Author> authors;"

    public Book(int bookId, String title, String isbn, Date published, String genre, String storyLine, int rating) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.published = published;
        this.genre = genre.toUpperCase();
        this.storyLine = storyLine;
        this.authors = new ArrayList<>();
        if (rating<1 || rating>5) throw new IllegalArgumentException("Rating is between 1-5");
        this.rating = rating;
    }

    public Book(String title, String isbn, Date published, String genre, String storyLine, int rating) {
        this(-1, title, isbn, published, genre, storyLine,rating);
    }
    
    public int getBookId() { return bookId; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public Date getPublished() { return published; }
    public String getGenre() { return genre; }
    public String getStoryLine() { return storyLine; }
    public int getRating() { return rating; }


    public void setStoryLine(String storyLine) {
        this.storyLine = storyLine;
    }

    public void addAuthorToBook(Author newAuthor){
        this.authors.add(newAuthor);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", published=" + published +
                ", genre='" + genre + '\'' +
                ", storyLine='" + storyLine + '\'' +
                ", authors=" + authors +
                ", rating=" + rating +
                '}';
    }
}
