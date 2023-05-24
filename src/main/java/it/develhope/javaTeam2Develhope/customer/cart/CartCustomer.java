package it.develhope.javaTeam2Develhope.customer.cart;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.Customer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CartCustomer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", unique = true)
    private Customer customer;

    @ManyToMany
    @JoinColumn(name = "book_id", referencedColumnName = "id", unique = true)
    private List<Book> bookInTheCart = new ArrayList<>();

    public void addToCart(Book book) {
        bookInTheCart.add(book);
    }

    public void removeFromCart(Book book) {
        bookInTheCart.remove(book);
    }
}
