package it.develhope.javaTeam2Develhope.customer;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private CustomerTypeEnum type;
    private String name;
    private String surname;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private String passwordSalt;
    private String passwordHash;
    private LocalDate birthday;
    private String address;
    private LocalDate dateOfSubscription;
}
