package it.develhope.javaTeam2Develhope.order;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime dateOfOrder;
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    private boolean isGift;
    private String details;
    private float totalPrice;
    private Long customerCardId;
    private Long bookId;

    public OrderDTO() {}

    public OrderDTO(Order order) {
        this.id = order.getId();
        this.dateOfOrder = order.getDateOfOrder();
        this.dateOfShipping = order.getDateOfShipping();
        this.dateOfArrival = order.getDateOfArrival();
        this.details = order.getDetails();
        this.totalPrice = order.getTotalPrice();
        this.customerCardId = order.getCustomerCard().getId();
        this.bookId = order.getBook().getId();
    }
}
