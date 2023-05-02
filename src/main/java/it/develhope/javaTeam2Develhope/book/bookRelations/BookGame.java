package it.develhope.javaTeam2Develhope.book.bookRelations;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.game.Game;
import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book_game")
public class BookGame {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;
}
