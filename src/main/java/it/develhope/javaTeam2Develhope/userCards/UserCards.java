package it.develhope.javaTeam2Develhope.userCards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserCards {
    private Long id;
    private String cardType;
    private int cardNum;
    private LocalDate cardExpiry;
    private String cardHolderName;


}
