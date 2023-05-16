package it.develhope.javaTeam2Develhope.digitalPurchase;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "digital_purchases")

public class DigitalPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private CustomerCard customerCard;
    @OneToOne
    private Book purchasedBook;
    private LocalDate dateOfPurchase;
    private boolean isGift;
    private String details;
    private float totalPrice;
    @OneToOne
    private Book book;
}
