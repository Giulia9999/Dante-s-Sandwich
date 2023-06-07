package it.develhope.javaTeam2Develhope.security.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "mandatory")
    private String firstname;
    @NotBlank(message = "mandatory")
    private String lastname;
    @NotBlank(message = "mandatory")
    private String username;
    @NotBlank(message = "mandatory")
    private String email;
    @NotBlank(message = "mandatory")
    private String password;
    private LocalDate birthday;
    private String address;
}
