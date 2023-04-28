package it.develhope.javaTeam2Develhope.digitalPurchase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalPurchaseRepo extends JpaRepository<DigitalPurchase,Long> {
}
