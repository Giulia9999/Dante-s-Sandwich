package it.develhope.javaTeam2Develhope.subscription;

import io.micrometer.common.util.StringUtils;
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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @GetMapping
    public ResponseEntity<Page<Subscription>> getAllSubscriptions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Boolean isApproved,
            @RequestParam(required = false) Boolean isCanceled,
            @RequestParam(required = false) Boolean isRenewed,
            @RequestParam(required = false) Float monthlyPrice) {

        Specification<Subscription> spec = Specification.where(null);

        if (isApproved != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isApproved"), isApproved));
        }
        if (isCanceled != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isCanceled"), isCanceled));
        }
        if (isRenewed != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isRenewed"), isRenewed));
        }
        if (monthlyPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("monthlyPrice"), monthlyPrice));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Subscription> subscriptionsPage = subscriptionRepo.findAll(spec, paging);
        return ResponseEntity.ok(subscriptionsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable long id) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription subscription = optionalSubscription.get();

        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Subscription>> addMultipleSubscriptions(@RequestBody List<Subscription> subscriptions) {
        List<Subscription> savedSubscriptions = subscriptionRepo.saveAllAndFlush(subscriptions);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubscriptions);
    }

    @PostMapping("/single")
    public ResponseEntity<Subscription> addSingleSubscription(@RequestBody Subscription subscription) {
        Subscription savedSubscription = subscriptionRepo.saveAndFlush(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @Valid @RequestBody Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        subscription.setId(id);

        Subscription updatedSubscription = subscriptionRepo.save(subscription);
        return ResponseEntity.ok(updatedSubscription);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable long id, @RequestBody Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Subscription existingSubscription = optionalSubscription.get();

        BeanUtils.copyProperties(subscription, existingSubscription, getEmptyPropertyNames(subscription));
        Subscription savedSubscription = subscriptionRepo.save(existingSubscription);

        return ResponseEntity.ok(savedSubscription);
    }

    private String[] getEmptyPropertyNames(Subscription source) {
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
    public ResponseEntity<Void> deleteSubscription(@PathVariable long id) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        subscriptionRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllSubscriptions() {
        subscriptionRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }
}
