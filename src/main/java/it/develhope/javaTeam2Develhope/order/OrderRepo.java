package it.develhope.javaTeam2Develhope.order;

import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    Page<Order> findAll(Specification<DigitalPurchase> spec, Pageable paging);
}

