package it.develhope.javaTeam2Develhope.books;


import it.develhope.games.Games;
import it.develhope.motionPictures.MotionPictures;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Books {

    private int id;
    private String author;
    private String title;
    private String topic;
    private String publisher;
    private String ISBN;
    private int year;
    private float price;
    private int numberOfPages;
    private boolean isInStock;
    private String imageCoverFilePath;
    private boolean isEbook;
    private List<Games> relatedGames;
    private List<MotionPictures> relatedMovies;


}