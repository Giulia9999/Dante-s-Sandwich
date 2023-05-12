package it.develhope.javaTeam2Develhope.paymentCard;

import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentCardRepo extends JpaRepository<PaymentCard,Long> {
    Page<PaymentCard> findAll(Specification<PaymentCard> spec, Pageable paging);
}
