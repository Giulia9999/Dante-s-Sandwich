package it.develhope.javaTeam2Develhope.game;

import it.develhope.javaTeam2Develhope.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends JpaRepository<Game,Long> {
    Page<Game> findAll(Specification<Game> spec, Pageable paging);

}
