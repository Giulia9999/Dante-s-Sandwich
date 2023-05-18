package it.develhope.javaTeam2Develhope.book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepo bookRepo;

    @Test
    public void testGetAllBooks() {
        // create a list of books
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L,"John Doe", "Java Programming", "Programming", "Penguin",
                "123456789", 2021, 29.99f, 500, true, "images/book1.jpg", false));

        books.add(new Book(2L,"Jane Smith", "Python Programming", "Programming", "O'Reilly",
                "987654321", 2020, 39.99f, 700, false, "images/book2.jpg", true));

        books.add(new Book(3L,"Bob Johnson", "The Art of War", "Philosophy", "Random House",
                "24681012", 2018, 14.99f, 200, true, "images/book3.jpg", true));

        // create a page of books
        Pageable pageable = PageRequest.of(0, 2);
        Page<Book> bookPage = new PageImpl<>(books.subList(0, 2), pageable, books.size());

        // mock the bookRepo to return the page of books when findAll is called
        when(bookRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(bookPage);

        // call the getAllBooks method
        Page<Book> result = bookService.getAllBooks(0, 2, "John Doe", null, null, null, null,
                null, null, null, null);

        // assert that the returned page is equal to the expected page
        assertEquals(bookPage, result);
    }

    @Test
    void testGetEmptyPropertyNames() {
        // Arrange
        Book book = new Book();
        book.setId(1L);
        book.setAuthor("");
        book.setTitle("Title");
        book.setTopic("");
        book.setPublisher("Publisher");
        book.setISBN("1234567890");
        book.setYear(2022);
        book.setPrice(10.99f);
        book.setNumberOfPages(200);
        book.setIsInStock(true);
        book.setImageCoverFilePath(null);
        book.setIsEbook(false);

        // Act
        String[] emptyPropertyNames = bookService.getEmptyPropertyNames(book);

        // Assert
        String[] expectedEmptyPropertyNames = {"topic", "author"};
        Arrays.sort(expectedEmptyPropertyNames);
        Arrays.sort(emptyPropertyNames);
        assertArrayEquals(expectedEmptyPropertyNames, emptyPropertyNames);
    }

}
