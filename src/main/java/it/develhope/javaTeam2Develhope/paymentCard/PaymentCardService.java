package it.develhope.javaTeam2Develhope.paymentCard;

import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.CustomerService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service
public class PaymentCardService {

    private final PaymentCardRepo paymentCardRepo;

    public PaymentCardService(PaymentCardRepo paymentCardRepo) {
        this.paymentCardRepo = paymentCardRepo;
    }

    public void validatePaymentCard(PaymentCard paymentCard, double purchaseAmount) {
        // Check that the payment card is not null
        if (paymentCard == null) {
            throw new IllegalArgumentException("Payment card cannot be null.");
        }

        // Check that the payment card has sufficient balance to cover the purchase amount
        if (paymentCard.getBalance() < purchaseAmount) {
            throw new IllegalArgumentException("Insufficient balance on payment card.");
        }
    }


    public Page<PaymentCard> getAllPaymentCards(String cardType, Integer cardNum, LocalDateTime cardExpiry, String cardHolderName, int page, int size) {
        Specification<PaymentCard> spec = Specification.where(null);

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
        return paymentCardRepo.findAll(spec, paging);
    }

    public Optional<PaymentCard> getPaymentCardById(long id) {
        return paymentCardRepo.findById(id);
    }

    public List<PaymentCard> addMultiplePaymentCards(List<PaymentCard> paymentCards) {
        return paymentCardRepo.saveAllAndFlush(paymentCards);
    }

    public PaymentCard addSinglePaymentCard(PaymentCard paymentCard) {
        return paymentCardRepo.saveAndFlush(paymentCard);
    }

    public Optional<PaymentCard> updatePaymentCard(long id, PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return Optional.empty();
        }

        paymentCard.setId(id);
        return Optional.of(paymentCardRepo.save(paymentCard));
    }

    public Optional<PaymentCard> updatePaymentCardPartially(long id, PaymentCard paymentCard) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return Optional.empty();
        }

        PaymentCard existingPaymentCard = optionalPaymentCard.get();

        BeanUtils.copyProperties(paymentCard, existingPaymentCard, getEmptyPropertyNames(paymentCard));
        return Optional.of(paymentCardRepo.save(existingPaymentCard));
    }

    /**
     * This method returns the null properties of the object paymentCard that we create
     * @param source The properties of the updated paymentCard
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

    public boolean deletePaymentCard(long id) {
        Optional<PaymentCard> optionalPaymentCard = paymentCardRepo.findById(id);

        if (optionalPaymentCard.isEmpty()) {
            return false;
        }

        paymentCardRepo.deleteById(id);
        return true;
    }

    public void deleteAllPaymentCards() {
        paymentCardRepo.deleteAll();
    }
}
