package it.develhope.javaTeam2Develhope.userCards;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCardsRepo extends JpaRepository<UserCards,Long> {

    //private List<UserCards> userCards;

}
