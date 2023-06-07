package it.develhope.javaTeam2Develhope.security.auth;
import it.develhope.javaTeam2Develhope.customer.ConflictException;
import it.develhope.javaTeam2Develhope.customer.Customer;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import it.develhope.javaTeam2Develhope.notifications.AuthCode;
import it.develhope.javaTeam2Develhope.notifications.NotificationService;
import it.develhope.javaTeam2Develhope.security.configuration.JWTService;
import it.develhope.javaTeam2Develhope.customer.Roles;
import jakarta.mail.MessagingException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class AuthenticationService{
    private static final String adminEmail1 = "alessio.limina90@gmail.com";
    private static final String adminEmail2 = "maxpower88999@gmail.com";

    private final AuthCode authCode;
    private final CustomerService customerService;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final NotificationService notificationService;

    public AuthenticationService(
            AuthCode authCode, CustomerService customerService1, PasswordEncoder passwordEncoder,
            JWTService jwtService,
            AuthenticationManager authenticationManager,
            NotificationService notificationService) {
        this.authCode = authCode;
        this.customerService = customerService1;

        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.notificationService = notificationService;
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) throws ConflictException, MessagingException {
        Customer admin = new Customer();
        admin.setName(request.getFirstname());
        admin.setSurname(request.getLastname());
        admin.setUsername(request.getUsername());
        if(!Objects.equals(request.getEmail(), adminEmail1) || !Objects.equals(request.getEmail(), adminEmail2)){
            throw new ConflictException("Registration denied");
        }
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRole(Roles.ADMIN);
        notificationService.sendWelcome(admin.getEmail());
        customerService.createCustomer(admin);

        var jwtToken = jwtService.generateToken(admin);
        return AuthenticationResponse.builder()
                .token(jwtToken)
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
        notificationService.sendWelcome(customer.getEmail());
        customerService.createCustomer(customer);

        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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
