package it.develhope.javaTeam2Develhope.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/admin/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody RegisterRequest request
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(service.registerAdmin(request));
    }

    @PostMapping("/admin/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateAdmin(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(service.authenticateAdmin(request));

    }

    @PostMapping("/customer/register")
    public ResponseEntity<AuthenticationResponse> registerCustomer(
            @RequestBody RegisterRequest request
    ) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(service.registerCustomer(request));
    }

    @PostMapping("/customer/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateCustomer(
            @RequestBody AuthenticationRequest request
    ) throws AuthenticationException, NoSuchAlgorithmException, InvalidKeySpecException {
        return ResponseEntity.ok(service.authenticateCustomer(request));

    }
}
