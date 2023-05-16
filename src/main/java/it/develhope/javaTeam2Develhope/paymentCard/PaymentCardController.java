package it.develhope.javaTeam2Develhope.paymentCard;

import io.micrometer.common.util.StringUtils;
import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.game.Game;
import it.develhope.javaTeam2Develhope.order.Order;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/paymentCards")
public class PaymentCardController {
    @Autowired
    private PaymentCardService paymentCardService;
    @Autowired
    CustomerService customerService;

    @GetMapping("")
    public ResponseEntity<Page<PaymentCard>> getAllPaymentCards(
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) Integer cardNum,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime cardExpiry,
            @RequestParam(required = false) String cardHolderName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PaymentCard> paymentCards = paymentCardService.getAllPaymentCards(cardType, cardNum, cardExpiry, cardHolderName, page, size);
        return ResponseEntity.ok(paymentCards);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCard> getPaymentCardById(@PathVariable long id) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardService.getPaymentCardById(id);
        return optionalPaymentCard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/single")
    public ResponseEntity<PaymentCard> addSinglePaymentCard(@RequestBody @Valid PaymentCard paymentCard) {
        PaymentCard savedPaymentCard = paymentCardService.addSinglePaymentCard(paymentCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentCard> updatePaymentCard(@PathVariable long id, @RequestBody PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardService.updatePaymentCard(id, paymentCard);
        if (optionalPaymentCard.isPresent()) {
            return ResponseEntity.ok(optionalPaymentCard.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentCard> updatePaymentCardPartially(@PathVariable long id, @RequestBody PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardService.updatePaymentCardPartially(id, paymentCard);
        if (optionalPaymentCard.isPresent()) {
            return ResponseEntity.ok(optionalPaymentCard.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCard(@PathVariable long id) {
        boolean deleted = paymentCardService.deletePaymentCard(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("")
    public ResponseEntity<Void> deleteAllPaymentCards() {
        paymentCardService.deleteAllPaymentCards();
        return ResponseEntity.noContent().build();
    }
}
