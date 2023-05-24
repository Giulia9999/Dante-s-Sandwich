package it.develhope.javaTeam2Develhope.paymentCard;

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
@Table(name = "payment_card")
public class PaymentCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "mandatory")
    private String cardType;
    @NotNull
    @Column(unique = true)
    private Long cardNum;
    @NotNull
    private LocalDate cardExpiry;
    @NotBlank(message = "mandatory")
    private String cardHolderName;
    private double balance;
}
