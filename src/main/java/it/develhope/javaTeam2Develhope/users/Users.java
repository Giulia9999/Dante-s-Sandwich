package it.develhope.javaTeam2Develhope.users;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private UsersTypeEnum type;
    private String name;
    private String surname;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    private String passwordSalt;
    private String passwordHash;
    private LocalDate birthday;
    private String address;
    private LocalDate dateOfSubscription;
}
