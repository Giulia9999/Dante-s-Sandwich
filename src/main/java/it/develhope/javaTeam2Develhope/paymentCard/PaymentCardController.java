package it.develhope.javaTeam2Develhope.paymentCard;

import io.micrometer.common.util.StringUtils;
import it.develhope.javaTeam2Develhope.book.Book;
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
    private PaymentCardRepo paymentCardRepo;

    @GetMapping
    public ResponseEntity<Page<PaymentCard>> getAllPaymentCards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String cardType,
            @RequestParam(required = false) Integer cardNum,
            @RequestParam(required = false) LocalDateTime cardExpiry,
            @RequestParam(required = false) String cardHolderName) {

        Specification<DigitalPurchase> spec = Specification.where(null);

        if (cardType != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardType"), cardType));
        }
        if (cardNum != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardNum"), cardNum));
        }
        if (cardExpiry != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardExpiry"), cardExpiry));
        }
        if (cardHolderName != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("cardHolderName"), cardHolderName));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<PaymentCard> paymentCardPage = paymentCardRepo.findAll(spec, paging);
        return ResponseEntity.ok(paymentCardPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentCard> getPaymentCardById(@PathVariable long id) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaymentCard paymentCard = optionalPaymentCard.get();

        return ResponseEntity.ok(paymentCard);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<PaymentCard>> addMultiplePaymentCards(@RequestBody List<PaymentCard> paymentCards) {
        List<PaymentCard> savedPaymentCards = paymentCardRepo.saveAllAndFlush(paymentCards);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentCards);
    }

    @PostMapping("/single")
    public ResponseEntity<PaymentCard> addSinglePaymentCard(@RequestBody PaymentCard paymentCard) {
        PaymentCard savedPaymentCard = paymentCardRepo.saveAndFlush(paymentCard);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPaymentCard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentCard> updatePaymentCard(@PathVariable Long id, @Valid @RequestBody PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        paymentCard.setId(id);

        PaymentCard updatedPaymentCard = paymentCardRepo.save(paymentCard);
        return ResponseEntity.ok(updatedPaymentCard);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentCard> updatePaymentCard(@PathVariable long id, @RequestBody PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        PaymentCard existingPaymentCard = optionalPaymentCard.get();

        BeanUtils.copyProperties(paymentCard, existingPaymentCard, getEmptyPropertyNames(paymentCard));
        PaymentCard savedPaymentCard = paymentCardRepo.save(existingPaymentCard);

        return ResponseEntity.ok(savedPaymentCard);
    }

    /**
     * This method returns the null properties of the object book that we create
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getEmptyPropertyNames(PaymentCard source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue instanceof String && StringUtils.isBlank((String) srcValue)) {
                // Ignore empty string values
                continue;
            }
            if (srcValue == null) {
                // Include null values
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePaymentCard(@PathVariable long id) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        paymentCardRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllPaymentCards() {
        paymentCardRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }


}
