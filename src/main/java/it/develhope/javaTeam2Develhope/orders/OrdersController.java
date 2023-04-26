package it.develhope.javaTeam2Develhope.orders;

import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class OrdersController {
   /* private final OrdersRepo ordersRepo;

    @Autowired
    public OrdersController(OrdersRepo ordersRepo) {
        this.ordersRepo = ordersRepo;
    }*/
}
