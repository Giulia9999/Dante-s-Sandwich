package it.develhope.javaTeam2Develhope.userCard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserCardController {

    @Autowired
    private UserCardRepo userCardRepo;

}
