package it.develhope.javaTeam2Develhope.motionPictures;

import it.develhope.javaTeam2Develhope.books.Books;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class MotionPictures {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
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
    @ManyToMany(mappedBy = "motionPictures")
    private List<Books> books;


}
