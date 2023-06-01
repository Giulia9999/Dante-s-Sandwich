package it.develhope.javaTeam2Develhope.customer;
import it.develhope.javaTeam2Develhope.userRoles.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Roles role;
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
    @NotNull
    private LocalDate birthday;
    @NotBlank(message = "mandatory")
    private String address;
    private LocalDate dateOfSubscription;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
