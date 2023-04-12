package it.develhope.javaTeam2Develhope.orders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Orders {
    private int id;
    private double weight;
    private LocalDate dateOfOrder;
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    private short isGift;
    private String details;
    private float totalPrice;
    private int quantity;
}
