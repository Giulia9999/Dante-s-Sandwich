package it.develhope.javaTeam2Develhope.userCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCardDTO {

    private Long id;
    private String cardType;
    private LocalDate cardExpiry;
    private String cardHolderName;

}
