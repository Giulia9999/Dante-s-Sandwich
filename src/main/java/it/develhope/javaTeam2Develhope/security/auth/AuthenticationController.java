package it.develhope.javaTeam2Develhope.security.auth;

import it.develhope.javaTeam2Develhope.customer.ConflictException;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:8080")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/admin/register")
    public ResponseEntity<String> registerAdmin(
            @RequestBody RegisterRequest request
    ) throws ConflictException, MessagingException {
        return ResponseEntity.ok(service.registerAdmin(request));
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(service.authenticateAdmin(request));

    }


    @PostMapping("/customer/register")
    public ResponseEntity<String> registerCustomer(
            @RequestBody RegisterRequest request
    ) throws ConflictException, MessagingException {
        return ResponseEntity.ok(service.registerCustomer(request));
    }

    @PostMapping("/customer/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException {
        return ResponseEntity.ok(service.authenticateCustomer(request));

    }
}
