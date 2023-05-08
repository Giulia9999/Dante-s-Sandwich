package it.develhope.javaTeam2Develhope.digitalPurchase;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.game.Game;
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
@RequestMapping("/")
public class DigitalPurchaseController {

    @Autowired
    private DigitalPurchaseRepo digitalPurchaseRepo;
    @GetMapping
    public ResponseEntity<Page<DigitalPurchase>> getAllDigitalPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) LocalDateTime dateOfPurchase,
            @RequestParam(required = false) Boolean isGift,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Integer quantity,
            @RequestParam(required = false) Float totalPrice) {

        Specification<DigitalPurchase> spec = Specification.where(null);

        if (dateOfPurchase != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfPurchaser"), dateOfPurchase));
        }
        if (isGift != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        if (details != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("details"), details));
        }
        if (quantity != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("quantity"), quantity));
        }
        if (totalPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("totalPrice"), totalPrice));

        }

        Pageable paging = PageRequest.of(page, size);
        Page<DigitalPurchase> digitalPurchasePage = digitalPurchaseRepo.findAll(spec, paging);
        return ResponseEntity.ok(digitalPurchasePage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DigitalPurchase> getDigitalPurchaseById(@PathVariable long id) {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DigitalPurchase digitalPurchase = optionalDigitalPurchase.get();

        return ResponseEntity.ok(digitalPurchase);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<DigitalPurchase>> addMultipleDigitalPurchases(@RequestBody List<DigitalPurchase> digitalPurchases) {
        List<DigitalPurchase> savedDigitalPurchases = digitalPurchaseRepo.saveAllAndFlush(digitalPurchases);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDigitalPurchases);
    }

    @PostMapping("/single")
    public ResponseEntity<DigitalPurchase> addSingleDigitalPurchase(@RequestBody DigitalPurchase digitalPurchase) {
        DigitalPurchase savedDigitalPurchase = digitalPurchaseRepo.saveAndFlush(digitalPurchase);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDigitalPurchase);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DigitalPurchase> updateDigitalPurchase(@PathVariable Long id, @Valid @RequestBody DigitalPurchase digitalPurchase) {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        digitalPurchase.setId(id);

        DigitalPurchase updatedDigitalPurchase = digitalPurchaseRepo.save(digitalPurchase);
        return ResponseEntity.ok(updatedDigitalPurchase);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DigitalPurchase> updateDigitalPurchase(@PathVariable long id, @RequestBody DigitalPurchase digitalPurchase) {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        DigitalPurchase existingDigitalPurchase = optionalDigitalPurchase.get();

        BeanUtils.copyProperties(digitalPurchase, existingDigitalPurchase, getNullPropertyNames(digitalPurchase));
        DigitalPurchase savedDigitalPurchase = digitalPurchaseRepo.save(existingDigitalPurchase);

        return ResponseEntity.ok(savedDigitalPurchase);
    }

    /**
     * This method returns the null properties of the object book that we create
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getNullPropertyNames(DigitalPurchase source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDigitalPurchase(@PathVariable long id) {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        digitalPurchaseRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllDigitalPurchases() {
        digitalPurchaseRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }

}
