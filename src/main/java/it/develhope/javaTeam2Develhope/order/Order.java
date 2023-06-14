package it.develhope.javaTeam2Develhope.order;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private LocalDateTime dateOfOrder;
    @JsonIgnore
    private String address;
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    private String details;
    @JsonIgnore
    private Boolean isGift;
    @NotNull
    private float totalPrice;
    @OneToOne
    private CustomerCard customerCard;
    @OneToOne
    private Book book;

}
