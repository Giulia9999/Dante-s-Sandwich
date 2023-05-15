package it.develhope.javaTeam2Develhope.customer;


import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CustomerTypeEnum type;
    @NotBlank(message = "mandatory")
    private String name;
    @NotBlank(message = "mandatory")
    private String surname;
    @NotBlank(message = "mandatory")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "mandatory")
    @Column(unique = true)
    private String email;
    @NotBlank(message = "mandatory")
    private String password;
    private String passwordSalt;
    private String passwordHash;
    @NotNull
    private LocalDate birthday;
    @NotBlank(message = "mandatory")
    private String address;
    private LocalDate dateOfSubscription;
    @OneToMany
    private List<DigitalPurchase> purchases;
    @OneToMany
    private List<Order> orders;
    @OneToOne
    private Subscription subscription;

}
