package it.develhope.javaTeam2Develhope.digitalPurchases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class DigitalPurchasesController {

    private final DigitalPurchasesRepo digitalPurchasesRepo;

    @Autowired
    public DigitalPurchasesController(DigitalPurchasesRepo digitalPurchasesRepo) {
        this.digitalPurchasesRepo = digitalPurchasesRepo;
    }
}
