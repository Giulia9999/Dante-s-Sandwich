package it.develhope.javaTeam2Develhope.subscription;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    private LocalDateTime dateOfSubscription;
    @NotNull
    private boolean isApproved;
    @NotNull
    private boolean isCanceled;
    @NotNull
    private boolean isRenewed;
    @NotNull
    private float monthlyPrice;
    private String details;
    @OneToMany
    private List<Book> books;
    @OneToOne
    private CustomerCard customerCard;
}
