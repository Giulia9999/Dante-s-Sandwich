package it.develhope.javaTeam2Develhope.digitalPurchase;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DigitalPurchaseDTO {
    private Long id;
    private Long customerCardId;
    private Long purchasedBookId;
    private LocalDate dateOfPurchase;
    private boolean isGift;
    private String details;
    private float totalPrice;

    public  DigitalPurchaseDTO(){}

    public DigitalPurchaseDTO(DigitalPurchase digitalPurchase){
        this.id = digitalPurchase.getId();
        this.customerCardId = digitalPurchase.getCustomerCard().getId();
        this.purchasedBookId = digitalPurchase.getPurchasedBook().getId();
        this.dateOfPurchase = digitalPurchase.getDateOfPurchase();
        this.isGift = digitalPurchase.isGift();
        this.details = digitalPurchase.getDetails();
        this.totalPrice = digitalPurchase.getTotalPrice();
    }
}
