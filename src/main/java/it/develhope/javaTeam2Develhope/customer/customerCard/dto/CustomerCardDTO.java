package it.develhope.javaTeam2Develhope.customer.customerCard.dto;

import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
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
}
