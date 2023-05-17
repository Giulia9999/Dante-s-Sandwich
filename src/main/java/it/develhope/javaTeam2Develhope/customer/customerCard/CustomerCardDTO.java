package it.develhope.javaTeam2Develhope.customer.customerCard;

import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CustomerCardDTO {
    private Long id;
    private String name;
    private String email;
    private List<PaymentCardDTO> paymentCards;

    public CustomerCardDTO() {}

    public CustomerCardDTO(CustomerCard customerCard) {
        this.id = customerCard.getId();
        this.name = customerCard.getCostumer().getName();
        this.email = customerCard.getCostumer().getEmail();
        this.paymentCards = customerCard.getPaymentCards().stream()
                .map(PaymentCardDTO::new)
                .collect(Collectors.toList());
    }

}
