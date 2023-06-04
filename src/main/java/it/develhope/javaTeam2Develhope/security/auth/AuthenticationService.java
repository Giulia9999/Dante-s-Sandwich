package it.develhope.javaTeam2Develhope.security.auth;
import it.develhope.javaTeam2Develhope.customer.ConflictException;
import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import it.develhope.javaTeam2Develhope.security.configuration.JWTService;
import it.develhope.javaTeam2Develhope.customer.Roles;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;

@Service
public class AuthenticationService{
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerService customerService1, PasswordEncoder passwordEncoder,
            JWTService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.customerService = customerService1;

        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) throws ConflictException, MessagingException {
        Customer admin = new Customer();
        admin.setName(request.getFirstname());
        admin.setSurname(request.getLastname());
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Roles.ADMIN);
        customerService.createCustomer(admin);

        /*var jwtToken = jwtService.generateToken(admin);*/
        return AuthenticationResponse.builder()
                /*.token(jwtToken)*/
                .build();
    }

    public AuthenticationResponse registerCustomer(RegisterRequest request) throws ConflictException, MessagingException {
        Customer customer = new Customer();
        customer.setName(request.getFirstname());
        customer.setSurname(request.getLastname());
        customer.setUsername(request.getUsername());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setAddress(request.getAddress());
        customer.setDateOfSubscription(LocalDate.now());
        customer.setRole(Roles.READER);
        customerService.createCustomer(customer);

        /*var jwtToken = jwtService.generateToken(customer);*/
        return AuthenticationResponse.builder()
                /*.token(jwtToken)*/
                .build();
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) throws AuthenticationException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var admin = customerService.findByUsername(request.getUsername())
                .orElseThrow(AuthenticationException::new);

        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticateCustomer(AuthenticationRequest request) throws AuthenticationException{
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var customer = customerService.findByUsername(request.getUsername())
                .orElseThrow(AuthenticationException::new);

        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
