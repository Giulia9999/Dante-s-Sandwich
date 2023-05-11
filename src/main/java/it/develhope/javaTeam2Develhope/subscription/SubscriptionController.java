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
    private SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<Page<Subscription>> getAllSubscriptions(
            @RequestParam(required = false) Boolean isApproved,
            @RequestParam(required = false) Boolean isCanceled,
            @RequestParam(required = false) Boolean isRenewed,
            @RequestParam(required = false) Float monthlyPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Subscription> subscriptionsPage = subscriptionService.getAllSubscriptions(isApproved, isCanceled, isRenewed, monthlyPrice, page, size);

        return ResponseEntity.ok(subscriptionsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable long id) {
        Optional<Subscription> optionalSubscription = subscriptionService.getSubscriptionById(id);

        if (optionalSubscription.isPresent()) {
            Subscription subscription = optionalSubscription.get();
            return ResponseEntity.ok(subscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Subscription>> addMultipleSubscriptions(@RequestBody List<Subscription> subscriptions) {
        List<Subscription> savedSubscriptions = subscriptionService.addMultipleSubscriptions(subscriptions);

        return ResponseEntity.ok(savedSubscriptions);
    }

    @PostMapping
    public ResponseEntity<Subscription> addSingleSubscription(@RequestBody Subscription subscription) {
        Subscription savedSubscription = subscriptionService.addSingleSubscription(subscription);

        return ResponseEntity.ok(savedSubscription);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionService.updateSubscription(id, subscription);

        if (optionalSubscription.isPresent()) {
            Subscription updatedSubscription = optionalSubscription.get();
            return ResponseEntity.ok(updatedSubscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Subscription> updateSubscriptionPartially(@PathVariable long id, @RequestBody Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionService.updateSubscriptionPartially(id, subscription);

        if (optionalSubscription.isPresent()) {
            Subscription updatedSubscription = optionalSubscription.get();
            return ResponseEntity.ok(updatedSubscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable long id) {
        subscriptionService.deleteSubscription(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllSubscriptions() {
        subscriptionService.deleteAllSubscriptions();

        return ResponseEntity.noContent().build();
    }
}
