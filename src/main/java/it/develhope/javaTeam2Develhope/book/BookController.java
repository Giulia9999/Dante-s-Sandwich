package it.develhope.javaTeam2Develhope.book;

//import it.develhope.javaTeam2Develhope.InvalidDataException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;



@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Float price,
            @RequestParam(required = false) Integer numberOfPages,
            @RequestParam(required = false) Boolean isInStock,
            @RequestParam(required = false) Boolean isEbook) {

        Page<Book> booksPage = bookService.getAllBooks(page, size, author, title, topic, publisher, year, price, numberOfPages, isInStock, isEbook);
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) throws BookNotFoundException {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping("/single")
    public ResponseEntity<Book> addSingleBook(@RequestBody Book book) {
        Book savedBook = bookService.addSingleBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) throws BookNotFoundException {
        Book updatedBook = bookService.updateBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> patchBook(@PathVariable long id, @RequestBody Book book) throws BookNotFoundException {
        Book updatedBook = bookService.patchBook(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) throws BookNotFoundException {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllBooks() {
        bookService.deleteAllBooks();
        return ResponseEntity.noContent().build();
    }


    //UPLOAD and DOWNLOAD METHODS


    //UPLOAD PDF
    @PatchMapping("/{id}/PDF")
    public ResponseEntity<String> uploadPDF(@PathVariable Long id, @RequestParam("pdf") MultipartFile pdfFile) {
        return bookService.uploadPDF(id, pdfFile);

    }

    //UPLOAD MP3
    @PatchMapping("/{id}/audible")
    public ResponseEntity<String> uploadMP3(@PathVariable Long id, @RequestParam("mp3") MultipartFile mp3File) {
        return bookService.uploadMP3(id, mp3File);
    }

    //DOWNLOAD PDF
    @GetMapping("/{id}/PDF")
    public ResponseEntity<Resource> downloadPDF(@PathVariable Long id) throws BookNotFoundException, IOException {
        return bookService.downloadPDF(id);
    }

    //DOWNLOAD MP3
    @GetMapping("/{id}/audible")
    public ResponseEntity<Resource> downloadMP3(@PathVariable Long id) throws BookNotFoundException, IOException {
        return bookService.downloadMP3(id);
    }
}


//DOCUMENTAZIONE API: https://documenter.getpostman.com/view/26043911/2s93eR5bMe