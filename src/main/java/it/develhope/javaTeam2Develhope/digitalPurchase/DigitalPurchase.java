package it.develhope.javaTeam2Develhope.digitalPurchase;

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

public class DigitalPurchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate dateOfPurchase;
    private short isGift;
    private String details;
    private int quantity;
    private float totalPrice;
}
