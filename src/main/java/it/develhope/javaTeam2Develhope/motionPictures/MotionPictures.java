package it.develhope.motionPictures;

import it.develhope.books.Books;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MotionPictures {
    private int id;
    private String director;
    private String title;
    private String topic;
    private String producer;
    private int year;
    private int duration;
    private int episodes;
    private int seasons;
    private String imageCoverFilePath;
    private String siteLink;
    private List<Books> relatedBooks;


}
