package it.develhope.javaTeam2Develhope.order;

import org.springframework.stereotype.Service;
import io.micrometer.common.util.StringUtils;
import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.motionPicture.MotionPicture;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Or;
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
@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public Page<Order> getAllOrders(Double weight, LocalDateTime dateOfOrder, LocalDateTime dateOfShipping,
                                    LocalDateTime dateOfArrival, Boolean isGift, String details, Float totalPrice, Integer quantity, int page,
                                    int size) {
        Specification<Order> spec = Specification.where(null);

        if (weight != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("weight"), weight));
        }
        if (dateOfOrder != null) {
            spec = spec
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfOrder"), dateOfOrder));
        }
        if (dateOfShipping != null) {
            spec = spec.and(
                    (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfShipping"), dateOfShipping));
        }
        if (dateOfArrival != null) {
            spec = spec
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfArrival"), dateOfArrival));
        }

        if (isGift != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        if (details != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("details"), details));
        }

        if (totalPrice != null) {
            spec = spec
                    .and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("totalPrice"), totalPrice));

        }
        if (quantity != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("quantity"), quantity));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepo.findAll(spec, paging);
        return orderPage;
    }

    public Optional<Order> getOrderById(long id) {
        return orderRepo.findById(id);
    }

    public Order addSingleOrder(Order order) {
        return orderRepo.saveAndFlush(order);
    }

    public Optional<Order> updateOrder(Long id, Order order) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return Optional.empty();
        }

        order.setId(id);
        Order updatedOrder = orderRepo.save(order);
        return Optional.of(updatedOrder);
    }

    public Optional<Order> updateOrderPartially(long id, Order order) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return Optional.empty();
        }

        Order existingOrder = optionalOrder.get();

        BeanUtils.copyProperties(order, existingOrder, getEmptyPropertyNames(order));
        Order savedOrder = orderRepo.save(existingOrder);

        return Optional.of(savedOrder);
    }

    /**
     * This method returns the null properties of the object book that we create
     *
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getEmptyPropertyNames(Order source) {
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

    public boolean deleteOrder(long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return false;
        }

        orderRepo.deleteById(id);

        return true;
    }

    public void deleteAllOrders() {
        orderRepo.deleteAll();
    }
}
