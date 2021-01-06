package org.example.model;

/**
 * Different types of genres
 */
public enum Genre {
    Drama(0),Romance(1),Crime(2),Horror(3),Comedy(4),Fantasy(5),
    Adventure(6),Dystopian(7),Mystery(8),Thriller(9),Cooking(10), Educational(11);

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
