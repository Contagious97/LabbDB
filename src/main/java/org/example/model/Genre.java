package org.example.model;

/**
 * Different types of genres
 */
public enum Genre {
    DRAMA(0),ROMANCE(1),CRIME(2),HORROR(3),COMEDY(4);

    /**
     * gets the genre represented as a value
     * @return int value
     */
    public int getValue(){
        return value;
    }
    private final int value;

    /**
     * initializes value by giving it a value
     * @param value which represents a genre
     */
    Genre(int value){
        this.value = value;
    }
}
