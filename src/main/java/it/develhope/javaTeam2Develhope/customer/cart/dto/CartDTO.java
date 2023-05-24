package it.develhope.javaTeam2Develhope.customer.cart.dto;

import it.develhope.javaTeam2Develhope.book.Book;
import lombok.Data;

import java.util.List;

@Data
public class CartDTO {
    private Long id;
    private String name;
    private String email;
    private List<Book> booksInTheCart;
}
