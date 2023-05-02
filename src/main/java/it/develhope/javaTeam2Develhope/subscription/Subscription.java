package it.develhope.javaTeam2Develhope.subscription;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate dateOfSubscription;
    private boolean isApproved;
    private boolean isCanceled;
    private boolean isRenewed;
    private float monthlyPrice;
    private String details;
}