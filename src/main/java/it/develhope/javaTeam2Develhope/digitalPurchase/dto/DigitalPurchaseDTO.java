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

    public DigitalPurchaseDTO(){}

    public DigitalPurchaseDTO(DigitalPurchase digitalPurchase){
        this.id = digitalPurchase.getId();
        this.customerCardId = digitalPurchase.getCustomerCard().getId();
        this.purchasedBookId = digitalPurchase.getPurchasedBook().getId();
        this.dateOfPurchase = digitalPurchase.getDateOfPurchase();
        this.details = digitalPurchase.getDetails();
        this.totalPrice = digitalPurchase.getTotalPrice();
    }
}
