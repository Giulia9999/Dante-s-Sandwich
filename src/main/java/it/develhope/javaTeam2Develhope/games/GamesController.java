package it.develhope.javaTeam2Develhope.games;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GamesController {
    private final GamesRepo gamesRepo;

    @Autowired
    public GamesController(GamesRepo gamesRepo) {
        this.gamesRepo = gamesRepo;
    }

}
