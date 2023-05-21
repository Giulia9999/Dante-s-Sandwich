package it.develhope.javaTeam2Develhope.subscription;


import lombok.Data;

import java.time.LocalDate;


@Data
public class SubscriptionDTO {

    private Long id;
    private LocalDate dateOfSubscription;
    private boolean isApproved;
    private boolean isCanceled;
    private boolean isRenewed;
    private float monthlyPrice;
    private String details;
    private Long customerCardID;


    public SubscriptionDTO(){}

    public SubscriptionDTO(Subscription subscription){
        this.id = subscription.getId();
        this.customerCardID = subscription.getCustomerCard().getId();
        this.dateOfSubscription = subscription.getDateOfSubscription();
        this.isApproved = subscription.isApproved();
        this.isCanceled = subscription.isCanceled();
        this.isRenewed = subscription.isRenewed();
        this.monthlyPrice = subscription.getMonthlyPrice();
        this.details = subscription.getDetails();

    }
}
