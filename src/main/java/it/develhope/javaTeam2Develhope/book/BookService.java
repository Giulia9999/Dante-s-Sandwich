package it.develhope.javaTeam2Develhope.book;

import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class BookService {
    @Autowired
    private BookRepo bookRepo;

    public Page<Book> getAllBooks(int page, int size, String author, String title, String topic,
                                  String publisher, Integer year, Float price, Integer numberOfPages,
                                  Boolean isInStock, Boolean isEbook) {
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
        return booksPage;
    }

    public Book getBookById(long id) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        return optionalBook.get();
    }

    public Book addSingleBook(Book book) {
        return bookRepo.saveAndFlush(book);
    }

    public Book updateBook(Long id, Book book) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        book.setId(id);

        return bookRepo.save(book);
    }

    public Book patchBook(long id, Book book) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        Book existingBook = optionalBook.get();

        BeanUtils.copyProperties(book, existingBook, getEmptyPropertyNames(book));

        return bookRepo.save(existingBook);
    }

    /**
     * This method returns the null properties of the object book that we create
     *
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    protected String[] getEmptyPropertyNames(Book source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue instanceof String && StringUtils.isBlank((String) srcValue)) {
                // Add empty string values
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void deleteBook(Long id) throws BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isEmpty()) {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

        bookRepo.deleteById(id);
    }

    public void deleteAllBooks() {
        bookRepo.deleteAll();
    }


    //UPLOAD and DOWNLOAD METHODS

    //UPLOAD PDF
    public Book uploadPDF(Long id, File pdfFile) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            FileInputStream fileIS = new FileInputStream(pdfFile);
            book.setEBook(pdfFile);
            Book updateBook = bookRepo.save(book);
            fileIS.close();
            return updateBook;
        } else {
            throw new BookNotFoundException("Book not found with id: " + id);
        }

    }

    //UPLOAD MP3
    public Book uploadMP3(Long id, File MP3File) throws IOException, BookNotFoundException {
        Optional<Book> optionalBook = bookRepo.findById(id);

        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            FileInputStream fileIS = new FileInputStream(MP3File);
            book.setAudible(MP3File);
            Book updateBook = bookRepo.save(book);
            fileIS.close();
            return updateBook;
        } else {
            throw new BookNotFoundException("Book not found with id:" + id);
        }

    }



}