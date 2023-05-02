package it.develhope.javaTeam2Develhope.book.bookRelations;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.motionPicture.MotionPicture;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_motionPictures")
public class BookMotionPictures {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "motionPictures_id", referencedColumnName = "id")
    private MotionPicture motionPictures;
}
