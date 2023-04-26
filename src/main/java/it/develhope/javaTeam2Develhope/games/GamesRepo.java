package it.develhope.javaTeam2Develhope.games;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GamesRepo extends JpaRepository<Games,Long> {


}
