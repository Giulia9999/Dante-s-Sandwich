package it.develhope.javaTeam2Develhope.subscription.dto;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class SubscriptionDTO {

    private Long id;
    private LocalDateTime dateOfSubscription;
    private boolean isApproved;
    private boolean isCanceled;
    private boolean isRenewed;
    private float monthlyPrice;
    private String details;
    private Long customerCardID;
}
