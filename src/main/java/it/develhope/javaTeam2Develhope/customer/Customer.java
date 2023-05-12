package it.develhope.javaTeam2Develhope.customer;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Enumerated(EnumType.STRING)
    @NotBlank(message = "mandatory")
    private CustomerTypeEnum type;
    @NotBlank (message = "mandatory")
    private String name;
    @NotBlank (message = "mandatory")
    private String surname;
    @NotBlank (message = "mandatory")
    @Column(unique = true)
    private String username;
    @NotBlank (message = "mandatory")
    @Column(unique = true)
    private String email;
    @NotBlank (message = "mandatory")
    private String password;
    @NotBlank (message = "mandatory")
    private String passwordSalt;
    @NotBlank (message = "mandatory")
    private String passwordHash;
    @NotNull
    private LocalDate birthday;
    @NotBlank (message = "mandatory")
    private String address;
    @NotNull
    private LocalDate dateOfSubscription;
}
