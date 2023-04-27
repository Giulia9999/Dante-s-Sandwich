package it.develhope.javaTeam2Develhope.books;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BooksRepo extends JpaRepository<Books,Long> {

}




