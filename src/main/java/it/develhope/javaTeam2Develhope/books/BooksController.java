package it.develhope.javaTeam2Develhope.books;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BooksController {
    @Autowired
    private BooksRepo booksRepo;

    @GetMapping
    public List<Books> getAllBooks(){
        return booksRepo.findAll();
    }
    @GetMapping("/{id}")
    public Optional<Books> getAllBooks(@PathVariable long id){
        return booksRepo.findById(id);
    }

    @PostMapping
    public Books addSingleBook(@RequestBody Books books){
        return booksRepo.saveAndFlush(books);
    }

    @PostMapping
    public List<Books> addMultipleBook(@RequestBody Iterable<Books> books){
        return booksRepo.saveAllAndFlush(books);
    }

    @PutMapping("/{id}")
    public Books updateBook(@PathVariable long id, @RequestBody Books books){
        books.setId(id);
        return booksRepo.saveAndFlush(books);
    }

    @DeleteMapping("/clear")
    public void deleteAllBooks(){
        booksRepo.deleteAll();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteBook(@PathVariable long id){
        booksRepo.deleteById(id);
    }

}
