package it.develhope.javaTeam2Develhope.book;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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


    //UPLOAD

    public ResponseEntity<String> uploadPDF(Long id, MultipartFile pdf) {
        try {
            Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
            if (!Objects.equals(pdf.getContentType(), "application/pdf")) {
                throw new IllegalArgumentException("File must be a PDF type!");
            }
            String originalFileName = pdf.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String randomFileName = book.getTitle() + fileExtension;
            String desiredFolder = "pdfFile/";
            Path folder = Paths.get(desiredFolder);
            if (!Files.exists(folder)) {
                Files.createDirectories(folder);
            }
            String filePath = desiredFolder + randomFileName;
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.copy(pdf.getInputStream(), path);

            book.setEBook(filePath);
            bookRepo.save(book);

            return ResponseEntity.ok("PDF uploaded correctly! Path saved for future download.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR during uploading PDF!");
        } catch (BookNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    public ResponseEntity<String> uploadMP3(Long id, MultipartFile mp3) {
        try {
            Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));
            if (!Objects.equals(mp3.getContentType(), "audio/mpeg")) {
                throw new IllegalArgumentException("File must be an MP3 type!");
            }
            String originalFileName = mp3.getOriginalFilename();
            assert originalFileName != null;
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileName = book.getTitle() + fileExtension;
            String desiredFolder = "mp3File/";
            Path folderPath = Paths.get(desiredFolder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            String filePath = desiredFolder + fileName;
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
            }
            Files.copy(mp3.getInputStream(), path);

            // Save only the relative path in the DB
            book.setAudible(filePath);
            bookRepo.save(book);

            return ResponseEntity.ok("MP3 uploaded correctly! Path saved for future download.");
        } catch (IOException e) {
            // Handle specific IO exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR during uploading MP3: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            // Handle invalid file type exception
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (BookNotFoundException e) {
            // Handle book not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    //DOWNLOAD


    public ResponseEntity<Resource> downloadPDF(Long id) throws BookNotFoundException, IOException {
        try {
            Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

            String filePath = String.valueOf(book.getEBook());
            File file = new File(filePath);

            if (file.exists()) {
                Path path = Paths.get(filePath);
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

                return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
            } else {
                throw new BookNotFoundException("PDF file not found for book with id: " + id);
            }
        } catch (IOException e) {
            throw new IOException("Error during downloading PDF!", e);
        }
    }


    public ResponseEntity<Resource> downloadMP3(Long id) throws BookNotFoundException, IOException {
        try {
            Book book = bookRepo.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

            String filePath = String.valueOf(book.getAudible());
            File file = new File(filePath);

            if (file.exists()) {
                Path path = Paths.get(filePath);
                ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename" + file.getName());
                headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");

                return ResponseEntity.ok().headers(headers).contentLength(file.length()).body(resource);
            } else {
                throw new BookNotFoundException("Audible file not found for book with id: " + id);
            }
        } catch (IOException e) {
            throw new IOException("Error during downloading MP3!", e);
        }

    }


}