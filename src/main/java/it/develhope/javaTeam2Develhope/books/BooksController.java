package it.develhope.javaTeam2Develhope.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksRepo booksRepo;
    @GetMapping
    public ResponseEntity<List<Books>> getAllBooks(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);

        Page<Books> booksPage = booksRepo.findAll(paging);

        List<Books> books = booksPage.getContent();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable long id) {
        Optional<Books> optionalBook = booksRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Books book = optionalBook.get();

        return ResponseEntity.ok(book);
    }

    @PostMapping("/single")
    public ResponseEntity<Books> addSingleBook(@RequestBody Books book) {
        Books savedBook = booksRepo.saveAndFlush(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Books>> addMultipleBooks(@RequestBody List<Books> books) {
        List<Books> savedBooks = booksRepo.saveAllAndFlush(books);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable long id, @RequestBody Books book) {
        Optional<Books> optionalBook = booksRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Books existingBook = optionalBook.get();

        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());

        Books savedBook = booksRepo.saveAndFlush(existingBook);

        return ResponseEntity.ok(savedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        Optional<Books> optionalBook = booksRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        booksRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllBooks() {
        booksRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }

}
