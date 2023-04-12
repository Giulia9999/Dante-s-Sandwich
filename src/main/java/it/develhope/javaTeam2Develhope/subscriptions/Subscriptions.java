package it.develhope.javaTeam2Develhope.subscriptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Subscriptions {
    private int id;
    private LocalDate dateOfSubscription;
    private boolean isApproved;
    private boolean isCanceled;
    private boolean isRenewed;
    private float monthlyPrice;
    private String details;
}
