package it.develhope.javaTeam2Develhope.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OrderController {
   @Autowired
    private OrderRepo orderRepo;

}
