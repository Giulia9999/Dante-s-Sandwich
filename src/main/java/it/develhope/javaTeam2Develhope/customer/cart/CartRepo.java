package it.develhope.javaTeam2Develhope.customer.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<CartCustomer, Long> {
}
