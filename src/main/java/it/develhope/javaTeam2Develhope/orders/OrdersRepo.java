package it.develhope.javaTeam2Develhope.orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersRepo extends JpaRepository<Orders,Long> {

}

