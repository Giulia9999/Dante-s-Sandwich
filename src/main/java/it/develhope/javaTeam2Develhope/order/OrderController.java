package it.develhope.javaTeam2Develhope.order;

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

@RestController
@RequestMapping("/")
public class OrderController {
   @Autowired
    private OrderRepo orderRepo;

    @GetMapping
    public ResponseEntity<Page<Order>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) LocalDateTime dateOfOrder,
            @RequestParam(required = false) LocalDateTime dateOfShipping,
            @RequestParam(required = false) LocalDateTime dateOfArrival,
            @RequestParam(required = false) Boolean isGift,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) Float totalPrice,
            @RequestParam(required = false) Integer quantity) {

        Specification<DigitalPurchase> spec = Specification.where(null);

        if (weight != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("weight"), weight));
        }
        if (dateOfOrder != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfOrder"), dateOfOrder));
        }
        if (dateOfShipping != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfShipping"), dateOfShipping));
        }
        if (dateOfArrival != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("dateOfArrival"), dateOfArrival));
        }

        if (isGift != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("isGift"), isGift));
        }
        if (details != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("details"), details));
        }

        if (totalPrice != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("totalPrice"), totalPrice));

        }
        if (quantity != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("quantity"), quantity));
        }

        Pageable paging = PageRequest.of(page, size);
        Page<Order> orderPage = orderRepo.findAll(spec, paging);
        return ResponseEntity.ok(orderPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order order = optionalOrder.get();

        return ResponseEntity.ok(order);
    }

    @PostMapping("/multiple")
    public ResponseEntity<List<Order>> addMultipleOrders(@RequestBody List<Order> orders) {
        List<Order> savedOrders = orderRepo.saveAllAndFlush(orders);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrders);
    }

    @PostMapping("/single")
    public ResponseEntity<Order> addSingleOrder(@RequestBody Order order) {
        Order savedOrder = orderRepo.saveAndFlush(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        order.setId(id);

        Order updatedOrder = orderRepo.save(order);
        return ResponseEntity.ok(updatedOrder);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable long id, @RequestBody Order order) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Order existingOrder = optionalOrder.get();

        BeanUtils.copyProperties(order, existingOrder, getNullPropertyNames(order));
        Order savedOrder = orderRepo.save(existingOrder);

        return ResponseEntity.ok(savedOrder);
    }

    /**
     * This method returns the null properties of the object book that we create
     * @param source The properties of the updated book
     * @return an array of all the null properties
     */
    private String[] getNullPropertyNames(Order source) {
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
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        Optional<Order> optionalOrder = orderRepo.findById(id);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();

        }

        orderRepo.deleteById(id);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllOrders() {
        orderRepo.deleteAll();

        return ResponseEntity.noContent().build();
    }

}
