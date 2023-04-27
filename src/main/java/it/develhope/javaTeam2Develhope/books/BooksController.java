package it.develhope.javaTeam2Develhope.books;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @GetMapping
    public ResponseEntity<Page<Books>> getAllBooks(
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

        Specification<Books> spec = Specification.where(null);

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
        Page<Books> booksPage = booksRepo.findAll((Example<Books>) spec, paging);

        return ResponseEntity.ok(booksPage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Books> updateBook(@PathVariable long id, @RequestBody Books book) {
        Optional<Books> optionalBook = booksRepo.findById(id);

        if (optionalBook.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Books existingBook = optionalBook.get();
        //Il metodo "copyProperties(Object source, Object target, String ignoreProperties)"
        // permette di copiare le proprietà not-null del nuovo oggetto in quello
        //da aggiornare. Il metodo getNullPropertyNames che ho creato serve per identificare
        //le proprietà nulle che dovranno essere ignorate dal metodo copyproperties.
        BeanUtils.copyProperties(book, existingBook, getNullPropertyNames(book));

        Books savedBook = booksRepo.save(existingBook);

        return ResponseEntity.ok(savedBook);
    }

    private String[] getNullPropertyNames(Books source) {
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
