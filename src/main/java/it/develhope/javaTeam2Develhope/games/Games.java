package it.develhope.javaTeam2Develhope.games;


import it.develhope.javaTeam2Develhope.books.Books;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Games {
    private int id;
    private String title;
    private String topic;
    private String producer;
    private int year;
    private String imageCoverFilePath;
    private String siteLink;
    private List<Books> relatedBooks;


}
