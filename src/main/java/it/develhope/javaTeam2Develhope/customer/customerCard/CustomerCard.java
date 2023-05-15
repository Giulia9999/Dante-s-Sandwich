package it.develhope.javaTeam2Develhope.customer.customerCard;

import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    @JoinColumn(name = "card_id", referencedColumnName = "id", unique = true)
    private List<PaymentCard> paymentCards = new ArrayList<>();

    public void addPaymentCard(PaymentCard paymentCard) {
        paymentCards.add(paymentCard);
    }

    public void removePaymentCard(PaymentCard paymentCard) {
        paymentCards.remove(paymentCard);
    }
}
