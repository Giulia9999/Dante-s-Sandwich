package it.develhope.javaTeam2Develhope.order;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double weight;
    private LocalDate dateOfOrder;
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    private boolean isGift;
    private String details;
    @NotNull
    private float totalPrice;
    @NotNull
    private int quantity;
    @OneToOne
    private CustomerCard customerCard;
    @OneToOne
    private Book book;
}
