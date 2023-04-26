package it.develhope.javaTeam2Develhope.games;
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
public class Games {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String topic;
    private String producer;
    private int year;
    private String imageCoverFilePath;
    private String siteLink;
    @ManyToMany(mappedBy = "games")
    private List<Books> books;
}
