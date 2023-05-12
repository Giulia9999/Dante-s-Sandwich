package it.develhope.javaTeam2Develhope.customer.customerCard;

import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer_card")
public class CustomerCard {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", unique = true)
    private Customer costumer;

    @ManyToOne
    @JoinColumn(name = "card_id", referencedColumnName = "id", unique = true)
    private PaymentCard paymentCard;
}
