package it.develhope.javaTeam2Develhope.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class SubscriptionController {
    @Autowired
    private SubscriptionRepo subscriptionRepo;


}
