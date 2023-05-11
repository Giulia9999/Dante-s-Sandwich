package it.develhope.javaTeam2Develhope.subscription;

import org.springframework.stereotype.Service;
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
@Service
public class SubscriptionService {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    public Page<Subscription> getAllSubscriptions(Boolean isApproved, Boolean isCanceled, Boolean isRenewed, Float monthlyPrice, int page, int size) {
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
        return subscriptionsPage;
    }

    public Optional<Subscription> getSubscriptionById(long id) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);
        return optionalSubscription;
    }

    public List<Subscription> addMultipleSubscriptions(List<Subscription> subscriptions) {
        List<Subscription> savedSubscriptions = subscriptionRepo.saveAllAndFlush(subscriptions);
        return savedSubscriptions;
    }

    public Subscription addSingleSubscription(Subscription subscription) {
        Subscription savedSubscription = subscriptionRepo.saveAndFlush(subscription);
        return savedSubscription;
    }

    public Optional<Subscription> updateSubscription(Long id, Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return Optional.empty();
        }

        subscription.setId(id);

        Subscription updatedSubscription = subscriptionRepo.save(subscription);
        return Optional.of(updatedSubscription);
    }

    public Optional<Subscription> updateSubscriptionPartially(long id, Subscription subscription) {
        Optional<Subscription> optionalSubscription = subscriptionRepo.findById(id);

        if (optionalSubscription.isEmpty()) {
            return Optional.empty();
        }

        Subscription existingSubscription = optionalSubscription.get();

        BeanUtils.copyProperties(subscription, existingSubscription, getEmptyPropertyNames(subscription));
        Subscription savedSubscription = subscriptionRepo.save(existingSubscription);

        return Optional.of(savedSubscription);
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

    public void deleteSubscription(long id) {
        subscriptionRepo.deleteById(id);
    }

    public void deleteAllSubscriptions() {
        subscriptionRepo.deleteAll();
    }
}
