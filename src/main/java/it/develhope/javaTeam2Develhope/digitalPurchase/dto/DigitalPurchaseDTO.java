package it.develhope.javaTeam2Develhope.digitalPurchase.dto;

import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DigitalPurchaseDTO {
    private Long id;
    private Long customerCardId;
    private Long purchasedBookId;
    private LocalDateTime dateOfPurchase;
    private String details;
    private float totalPrice;
    private String eBook;
    private String audible;
}
