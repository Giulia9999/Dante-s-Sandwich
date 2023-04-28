package it.develhope.javaTeam2Develhope.digitalPurchase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DigitalPurchaseController {

    @Autowired
    private DigitalPurchaseRepo digitalPurchaseRepo;
}
