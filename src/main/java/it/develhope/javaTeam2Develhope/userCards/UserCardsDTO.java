package it.develhope.javaTeam2Develhope.userCards;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardsDTO {

    private Long id;
    private String cardType;
    private LocalDate cardExpiry;
    private String cardHolderName;

}
