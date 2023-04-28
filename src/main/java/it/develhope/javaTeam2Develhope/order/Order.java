package it.develhope.javaTeam2Develhope.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double weight;
    private LocalDate dateOfOrder;
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    private short isGift;
    private String details;
    private float totalPrice;
    private int quantity;
}
