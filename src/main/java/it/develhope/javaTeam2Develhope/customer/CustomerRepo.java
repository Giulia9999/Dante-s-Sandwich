package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.admin.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<Customer,Long> {
    Customer findByEmail(String email);
    Optional<Customer> findByUsername(String username);

}
