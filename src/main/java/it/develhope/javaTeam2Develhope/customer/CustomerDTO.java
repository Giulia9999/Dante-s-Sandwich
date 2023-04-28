package it.develhope.javaTeam2Develhope.customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private LocalDate birthday;
    private String address;
    private LocalDate dateOfSubscription;
}
