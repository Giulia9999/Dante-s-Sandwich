package it.develhope.javaTeam2Develhope.paymentCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
public class PaymentCardDTO {

    private Long id;
    private String cardType;

    public PaymentCardDTO(){}

    public PaymentCardDTO(PaymentCard paymentCard){
        this.id = paymentCard.getId();
        this.cardType = paymentCard.getCardType();
    }

}
