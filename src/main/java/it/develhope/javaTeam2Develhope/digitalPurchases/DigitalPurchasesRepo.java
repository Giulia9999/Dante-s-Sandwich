package it.develhope.javaTeam2Develhope.digitalPurchases;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DigitalPurchasesRepo extends JpaRepository<DigitalPurchases,Long> {
}
