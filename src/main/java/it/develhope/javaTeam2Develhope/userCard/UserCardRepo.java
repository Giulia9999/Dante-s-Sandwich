package it.develhope.javaTeam2Develhope.userCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCardRepo extends JpaRepository<UserCard,Long> {

    //private List<UserCards> userCards;

}
