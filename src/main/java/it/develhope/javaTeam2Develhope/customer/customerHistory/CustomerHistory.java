package it.develhope.javaTeam2Develhope.customer.customerHistory;

import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
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
@Table(name = "customer_history")
public class CustomerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", unique = true)
    private Customer customer;
    @OneToMany
    private List<DigitalPurchase> purchases = new ArrayList<>();
    @OneToMany
    private List<Order> orders = new ArrayList<>();
    @OneToOne
    private Subscription subscription;
}
