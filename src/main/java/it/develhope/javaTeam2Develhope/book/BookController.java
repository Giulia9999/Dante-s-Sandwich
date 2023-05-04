package it.develhope.javaTeam2Develhope.book;

//import it.develhope.javaTeam2Develhope.InvalidDataException;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookRepo bookRepo;

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(
            //Tramite queste requestparam posso prendere tutti i libri che hanno un valore specificato
            //quindi se scrivo "topic='horror' mi darà tutti i libri di genere horror
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

        Specification<Book> spec = Specification.where(null);

        if (author != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("author"), author));
        }
        if (title != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("title"), title));
        }
        if (topic != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("topic"), topic));
        }
        if (publisher != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("publisher"), publisher));
        }
        if (year != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("year"), year));
        }
        if (price != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("price"), price));
        }
        if (numberOfPages != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("numberOfPages"), numberOfPages));
        }
        if (isInStock != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isInStock"), isInStock));
        }
        if (isEbook != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isEbook"), isEbook));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Book> booksPage = bookRepo.findAll(spec, paging);
        return ResponseEntity.ok(booksPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book book = optionalBook.get();

        return ResponseEntity.ok(book);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Book>> addMultipleBooks(@RequestBody List<Book> books) {
        List<Book> savedBooks = bookRepo.saveAllAndFlush(books);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooks);
    }

    @PostMapping("/single")
    public ResponseEntity<Book> addSingleBook(@RequestBody Book book) {
        Book savedBook = bookRepo.saveAndFlush(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        book.setId(id);

        Book updatedBook = bookRepo.save(book);
        return ResponseEntity.ok(updatedBook);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable long id, @RequestBody Book book) {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Book existingBook = optionalBook.get();

        BeanUtils.copyProperties(book, existingBook, getNullPropertyNames(book));
        Book savedBook = bookRepo.save(existingBook);

        return ResponseEntity.ok(savedBook);
    }

    /**
     * This method returns the null properties of the object book that we create
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getNullPropertyNames(Book source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable long id) {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        bookRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllBooks() {
        bookRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }

}

//DOCUMENTAZIONE API: https://documenter.getpostman.com/view/26043911/2s93eR5bMe