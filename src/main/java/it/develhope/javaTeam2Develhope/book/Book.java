package it.develhope.javaTeam2Develhope.book;


import it.develhope.javaTeam2Develhope.game.Game;
import it.develhope.javaTeam2Develhope.motionPicture.MotionPicture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String author;
    private String title;
    private String topic;
    private String publisher;
    @Column(unique = true)
    private String ISBN;
    private int year;
    private float price;
    private int numberOfPages;
    private boolean isInStock;
    private String imageCoverFilePath;
    private boolean isEbook;
    @ManyToMany
    @JoinTable(
            name = "book_game",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private List<Game> games;
    @ManyToMany
    @JoinTable(
            name = "book_motionPictures",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "motionPictures_id"))
    private List<MotionPicture> motionPictures;
}