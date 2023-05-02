package it.develhope.javaTeam2Develhope.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class GameController {
    @Autowired
    private GameRepo gameRepo;

}
