package it.develhope.javaTeam2Develhope.book;

//import it.develhope.javaTeam2Develhope.InvalidDataException;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


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

    @PatchMapping("/uploadPDF/{id}")
    public ResponseEntity<String> uploadPDF(@PathVariable Long id, @RequestParam("pdf") MultipartFile pdf) {
        File pdfFile = null;

        try {
            if (!pdf.getContentType().equals("application/pdf")) {
                throw new IllegalArgumentException("File must be a PDF type!");
            }
            pdfFile = File.createTempFile("pdf", "tmp");
            pdf.transferTo(pdfFile);
            bookService.uploadPDF(id, pdfFile);
            return ResponseEntity.ok("PDF uploaded correctly!");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR during uploading PDF!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (pdfFile != null) {
                pdfFile.delete();
            }
        }
    }

    @PatchMapping("/uploadMP3/{id}")
    public ResponseEntity<String> uploadMP3(@PathVariable Long id, @RequestParam("mp3") MultipartFile mp3) {
        File mp3File = null;

        try {
            if (!mp3.getContentType().equals("audio/mpeg")) {
                throw new IllegalArgumentException("File must be MP3 type!");
            }
            mp3File = File.createTempFile("mp3", "tmp");
            mp3.transferTo(mp3File);
            bookService.uploadMP3(id, mp3File);
            return ResponseEntity.ok("MP3 uploaded correctly");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR during uploading MP3!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR during uploading PDF!");
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (mp3File != null) {
                mp3File.delete();
            }
        }
    }

    @GetMapping("/downloadPDF/{id}")
    public ResponseEntity<Resource> downloadPDF(@PathVariable Long id) {
        try {
            Optional<Book> optionalBook = bookService.downloadPDF(id);

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                File ebookContent = book.getEBook();

                if (ebookContent.exists()) {
                    FileSystemResource resource = new FileSystemResource(ebookContent);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDisposition(ContentDisposition.attachment().filename(book.getTitle() + ".pdf").build());

                    return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);

                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/downloadMP3/{id}")
    public ResponseEntity<Resource> downloadMP3(@PathVariable Long id) {
        try {
            Optional<Book> optionalBook = bookService.downloadMP3(id);

            if (optionalBook.isPresent()) {
                Book book = optionalBook.get();
                File mp3Content = book.getAudible();
                if (mp3Content.exists()) {
                    FileSystemResource resource = new FileSystemResource(mp3Content);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    headers.setContentDisposition(ContentDisposition.attachment().filename(book.getTitle() + ".mp3").build());

                    return ResponseEntity.ok().headers(headers).contentLength(resource.contentLength()).body(resource);

                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (BookNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}


//DOCUMENTAZIONE API: https://documenter.getpostman.com/view/26043911/2s93eR5bMe