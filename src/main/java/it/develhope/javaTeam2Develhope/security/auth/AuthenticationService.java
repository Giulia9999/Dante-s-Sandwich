package it.develhope.javaTeam2Develhope.security.auth;
import it.develhope.javaTeam2Develhope.customer.ConflictException;
import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import it.develhope.javaTeam2Develhope.notifications.AuthCode;
import it.develhope.javaTeam2Develhope.notifications.NotificationService;
import it.develhope.javaTeam2Develhope.security.configuration.JWTService;
import it.develhope.javaTeam2Develhope.customer.Roles;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class AuthenticationService{
    private final String adminEmail1;
    private final String adminEmail2;

    private final AuthCode authCode;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotificationService notificationService;

    public AuthenticationService(
            @Value("${adminEmail1}") String adminEmail1, @Value("${adminEmail2}") String adminEmail2,
            AuthCode authCode, CustomerService customerService1,
            PasswordEncoder passwordEncoder, JWTService jwtService,
            AuthenticationManager authenticationManager,
            NotificationService notificationService) {
        this.adminEmail1 = adminEmail1;
        this.adminEmail2 = adminEmail2;
        this.authCode = authCode;
        this.customerService = customerService1;

        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.notificationService = notificationService;
    }

    public String registerAdmin(RegisterRequest request) throws ConflictException, MessagingException {
        Customer admin = new Customer();
        admin.setName(request.getFirstname());
        admin.setSurname(request.getLastname());
        admin.setUsername(request.getUsername());
        admin.setEmail(request.getEmail());
        if(!Objects.equals(request.getEmail(), adminEmail1) && !Objects.equals(request.getEmail(), adminEmail2)){
            throw new ConflictException("Registration denied");
        }
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Roles.ADMIN);
        notificationService.sendWelcome(admin.getEmail());
        notificationService.sendAuthCode(admin.getEmail());
        customerService.createCustomer(admin);

        return "Registration sucessfull. Check your email for the authentication code.";
        /*var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();*/
    }

    public String  registerCustomer(RegisterRequest request) throws ConflictException, MessagingException {
        Customer customer = new Customer();
        customer.setName(request.getFirstname());
        customer.setSurname(request.getLastname());
        customer.setUsername(request.getUsername());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setAddress(request.getAddress());
        customer.setDateOfSubscription(LocalDate.now());
        customer.setRole(Roles.READER);
        notificationService.sendWelcome(customer.getEmail());
        customerService.createCustomer(customer);

        /*var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();*/
        return "Registration successfull. Check your email for the authentication code.";
    }

    public AuthenticationResponse authenticateAdmin(AuthenticationRequest request) throws AuthenticationException{
        if(request.getAuthCode().equals(authCode.getCode())){
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }else {
            throw new AuthenticationException("Authentication denied");
        }

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
