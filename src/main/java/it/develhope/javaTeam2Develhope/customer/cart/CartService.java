package it.develhope.javaTeam2Develhope.customer.cart;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class CartService {

    private final CustomerService customerService;
    private final CartRepo cartRepo;
    private final BookService bookService;

    public CartService(CustomerService customerService, CartRepo cartRepo, BookService bookService) {
        this.customerService = customerService;
        this.cartRepo = cartRepo;
        this.bookService = bookService;
    }

    public CartCustomer addToCart(Long customerId, Long bookId) throws Exception, BookNotFoundException {
        Customer customer = customerService.getCustomerById(customerId);
        Book book = bookService.getBookById(bookId);
        Optional<CartCustomer> optionalCustomerCart = cartRepo.findById(customerId);
        CartCustomer cartCustomer;
        if(optionalCustomerCart.isPresent()){
            cartCustomer = optionalCustomerCart.get();
        }else {
            cartCustomer = new CartCustomer();
            cartCustomer.setCustomer(customer);
        }
        cartCustomer.addToCart(book);
        cartRepo.save(cartCustomer);
        return cartCustomer;
    }

    public CartCustomer removeFromCart(Long customerId, Long bookId) throws Exception, BookNotFoundException {
        Customer customer = customerService.getCustomerById(customerId);
        Book book = bookService.getBookById(bookId);
        Optional<CartCustomer> optionalCustomerCart = cartRepo.findById(customerId);
        CartCustomer cartCustomer;
        if(optionalCustomerCart.isPresent()){
            cartCustomer = optionalCustomerCart.get();
        }else {
            throw new EntityNotFoundException("The cart is empty");
        }
        cartCustomer.removeFromCart(book);
        cartRepo.save(cartCustomer);
        return cartCustomer;
    }
}
