package it.develhope.javaTeam2Develhope.order;

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
@Table
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private double weight;
    @NotNull
    private LocalDate dateOfOrder;
    @NotNull
    private LocalDate dateOfShipping;
    @NotNull
    private LocalDate dateOfArrival;
    @NotNull
    private boolean isGift;
    @NotBlank (message = "mandatory")
    private String details;
    @NotNull
    private float totalPrice;
    @NotNull
    private int quantity;
}
