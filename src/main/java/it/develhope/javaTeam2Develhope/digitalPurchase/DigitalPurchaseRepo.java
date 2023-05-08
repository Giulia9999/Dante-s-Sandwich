package it.develhope.javaTeam2Develhope.digitalPurchase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalPurchaseRepo extends JpaRepository<DigitalPurchase,Long> {
    Page<DigitalPurchase> findAll(Specification<DigitalPurchase> spec, Pageable paging);
}
