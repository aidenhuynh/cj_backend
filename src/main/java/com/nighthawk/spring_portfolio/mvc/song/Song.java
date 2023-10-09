package com.nighthawk.spring_portfolio.mvc.song;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data  // Annotations to simplify writing code (ie constructors, setters)
@NoArgsConstructor
@AllArgsConstructor
@Entity // Annotation to simplify creating an entity, which is a lightweight persistence domain object. Typically, an entity represents a table in a relational database, and each entity instance corresponds to a row in that table.
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique=true)
    private String song;

    private int like;
    private int dislike;

    // starting songs
    public static String[] init() {
        final String[] songArray = { // final means that this variable cannot change. 
            "Bohemian Rhapsody - Queen",
            "Hotel California - Eagles",
            "Stairway to Heaven - Led Zeppelin",
            "Imagine - John Lennon",
            "Hey Jude - The Beatles",
            "Like a Rolling Stone - Bob Dylan",
            "Thriller - Michael Jackson",
            "Billie Jean - Michael Jackson",
            "Smells Like Teen Spirit - Nirvana",
            "Sweet Child o' Mine - Guns N' Roses",
            "Yesterday - The Beatles",
            "Shape of You - Ed Sheeran",
            "Rolling in the Deep - Adele",
            "Livin' on a Prayer - Bon Jovi",
            "Boogie Wonderland - Earth, Wind & Fire",
            "Never Gonna Give You Up - Rick Astley"
        };
        return songArray;
    }
}
