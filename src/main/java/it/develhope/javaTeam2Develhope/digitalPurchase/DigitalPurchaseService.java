package it.develhope.javaTeam2Develhope.digitalPurchase;

import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
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
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
@Service

public class DigitalPurchaseService {
    @Autowired
    private DigitalPurchaseRepo digitalPurchaseRepo;

    public Page<DigitalPurchase> getAllDigitalPurchases(LocalDateTime dateOfPurchase, Boolean isGift, String details, Integer quantity, Float totalPrice, int page, int size) {

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
        return digitalPurchasePage;
    }

    public DigitalPurchase getDigitalPurchaseById(long id) throws ResourceNotFoundException {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            throw new ResourceNotFoundException("DigitalPurchase", "id", id);
        }

        DigitalPurchase digitalPurchase = optionalDigitalPurchase.get();

        return digitalPurchase;
    }

    public List<DigitalPurchase> addMultipleDigitalPurchases(List<DigitalPurchase> digitalPurchases) {
        List<DigitalPurchase> savedDigitalPurchases = digitalPurchaseRepo.saveAllAndFlush(digitalPurchases);
        return savedDigitalPurchases;
    }

    public DigitalPurchase addSingleDigitalPurchase(DigitalPurchase digitalPurchase) {
        DigitalPurchase savedDigitalPurchase = digitalPurchaseRepo.saveAndFlush(digitalPurchase);
        return savedDigitalPurchase;
    }

    public DigitalPurchase updateDigitalPurchase(Long id, DigitalPurchase digitalPurchase) throws ResourceNotFoundException {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            throw new ResourceNotFoundException("DigitalPurchase", "id", id);
        }

        digitalPurchase.setId(id);

        DigitalPurchase updatedDigitalPurchase = digitalPurchaseRepo.save(digitalPurchase);
        return updatedDigitalPurchase;
    }

    public DigitalPurchase patchDigitalPurchase(long id, DigitalPurchase digitalPurchase) throws ResourceNotFoundException {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            throw new ResourceNotFoundException("DigitalPurchase", "id", id);
        }

        DigitalPurchase existingDigitalPurchase = optionalDigitalPurchase.get();

        BeanUtils.copyProperties(digitalPurchase, existingDigitalPurchase, getEmptyPropertyNames(digitalPurchase));

        return digitalPurchaseRepo.save(existingDigitalPurchase);
    }

    /**
     * This method returns the null properties of the object book that we create
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getEmptyPropertyNames(DigitalPurchase source) {
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

    public void deleteDigitalPurchase(long id) throws ResourceNotFoundException {
        Optional<DigitalPurchase> optionalDigitalPurchase = digitalPurchaseRepo.findById(id);

        if (optionalDigitalPurchase.isEmpty()) {
            throw new ResourceNotFoundException("DigitalPurchase", "id", id);
        }

        digitalPurchaseRepo.deleteById(id);
    }

    public void deleteAllDigitalPurchases() {
        digitalPurchaseRepo.deleteAll();
    }
}
