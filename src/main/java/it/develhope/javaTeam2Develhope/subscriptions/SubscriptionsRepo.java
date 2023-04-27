package it.develhope.javaTeam2Develhope.subscriptions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionsRepo extends JpaRepository<Subscriptions,Long> {
}
