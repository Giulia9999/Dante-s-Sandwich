package it.develhope.javaTeam2Develhope.paymentCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepo extends JpaRepository<PaymentCard,Long> {

    //private List<UserCards> userCards;

}
