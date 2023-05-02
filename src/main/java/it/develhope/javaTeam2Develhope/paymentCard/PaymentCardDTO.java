package it.develhope.javaTeam2Develhope.paymentCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCardDTO {

    private Long id;
    private String cardType;
    private LocalDate cardExpiry;
    private String cardHolderName;

}
