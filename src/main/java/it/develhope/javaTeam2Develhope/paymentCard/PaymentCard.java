package it.develhope.javaTeam2Develhope.paymentCard;

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
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String cardType;
    @Column(unique = true)
    private int cardNum;
    private LocalDate cardExpiry;
    private String cardHolderName;
}