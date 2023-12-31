package it.develhope.javaTeam2Develhope.order.dto;

import it.develhope.javaTeam2Develhope.order.Order;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderDTO {
    private Long id;
    private LocalDateTime dateOfOrder;
    /*
    private String address;
     */
    private LocalDate dateOfShipping;
    private LocalDate dateOfArrival;
    /*
    private Boolean isGift;
     */
    private String details;
    private float totalPrice;
    private Long customerCardId;
    private Long bookId;
}
