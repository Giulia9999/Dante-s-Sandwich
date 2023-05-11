package it.develhope.javaTeam2Develhope.digitalPurchase;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/digitalPurchase")
public class DigitalPurchaseController {

    @Autowired
    private DigitalPurchaseService digitalPurchaseService;

    @GetMapping
    public ResponseEntity<Page<DigitalPurchase>> getAllDigitalPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDateTime dateOfPurchase,
            @RequestParam(required = false) Boolean isGift,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Float totalPrice) {

        Page<DigitalPurchase> digitalPurchasePage = digitalPurchaseService.getAllDigitalPurchases(dateOfPurchase, isGift, details, quantity, totalPrice, page, size);
        return ResponseEntity.ok(digitalPurchasePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalPurchase> getDigitalPurchaseById(@PathVariable long id) throws ResourceNotFoundException {
        DigitalPurchase digitalPurchase = digitalPurchaseService.getDigitalPurchaseById(id);
        return ResponseEntity.ok(digitalPurchase);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<DigitalPurchase>> addMultipleDigitalPurchases(@RequestBody List<DigitalPurchase> digitalPurchases) {
        List<DigitalPurchase> savedDigitalPurchases = digitalPurchaseService.addMultipleDigitalPurchases(digitalPurchases);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDigitalPurchases);
    }

    @PostMapping("/single")
    public ResponseEntity<DigitalPurchase> addSingleDigitalPurchase(@RequestBody DigitalPurchase digitalPurchase) {
        DigitalPurchase savedDigitalPurchase = digitalPurchaseService.addSingleDigitalPurchase(digitalPurchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDigitalPurchase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalPurchase> updateDigitalPurchase(@PathVariable Long id, @Valid @RequestBody DigitalPurchase digitalPurchase) throws ResourceNotFoundException {
        DigitalPurchase updatedDigitalPurchase = digitalPurchaseService.updateDigitalPurchase(id, digitalPurchase);
        return ResponseEntity.ok(updatedDigitalPurchase);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DigitalPurchase> patchDigitalPurchase(@PathVariable long id, @RequestBody DigitalPurchase digitalPurchase) throws ResourceNotFoundException {
        DigitalPurchase savedDigitalPurchase = digitalPurchaseService.patchDigitalPurchase(id, digitalPurchase);
        return ResponseEntity.ok(savedDigitalPurchase);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDigitalPurchase(@PathVariable long id) throws ResourceNotFoundException {
        digitalPurchaseService.deleteDigitalPurchase(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllDigitalPurchases() {
        digitalPurchaseService.deleteAllDigitalPurchases();
        return ResponseEntity.noContent().build();
    }


}
