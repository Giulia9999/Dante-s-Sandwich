package it.develhope.javaTeam2Develhope.books;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface BooksRepo extends JpaRepository<Books,Long> {
    Page<Books> findAll(Specification<Books> spec, Pageable paging);
}




