package it.develhope.digitalPurchases;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DigitalPurchases {
    private int id;
    private LocalDate dateOfPurchase;
    private short isGift;
    private String details;
    private int quantity;
    private float totalPrice;
}
