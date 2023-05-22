package it.develhope.javaTeam2Develhope.customer.customerHistory;

import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.digitalPurchase.dto.DigitalPurchaseDTO;
import it.develhope.javaTeam2Develhope.digitalPurchase.dto.DigitalPurchaseMapper;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.order.dto.OrderDTO;
import it.develhope.javaTeam2Develhope.order.dto.OrderMapper;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
import it.develhope.javaTeam2Develhope.subscription.dto.SubscriptionDTO;
import it.develhope.javaTeam2Develhope.subscription.dto.SubscriptionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerHistoryController {
    @Autowired
    CustomerHistoryRepo customerHistoryRepo;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    DigitalPurchaseMapper digitalPurchaseMapper;
    @Autowired
    SubscriptionMapper subscriptionMapper;

    @GetMapping("/orders/{customerId}")
    public ResponseEntity<List<OrderDTO>> getCustomerOrders(@PathVariable Long customerId){
        Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customerId);
        if(optionalCustomerHistory.isPresent()){
            CustomerHistory customerHistory = optionalCustomerHistory.get();
            List<Order> orders = customerHistory.getOrders();
            List<OrderDTO> ordersDTO = new ArrayList<>();
            for (Order singleOrder: orders) {
                OrderDTO orderDTO = orderMapper.toDto(singleOrder);
                ordersDTO.add(orderDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(ordersDTO);
        }else {
            throw new NoSuchElementException();
        }
    }

    @GetMapping("/digital/{customerId}")
    public ResponseEntity<List<DigitalPurchaseDTO>> getDigitalPurchases(@PathVariable Long customerId){
        Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customerId);
        if(optionalCustomerHistory.isPresent()){
            CustomerHistory customerHistory = optionalCustomerHistory.get();
            List<DigitalPurchase> purchases = customerHistory.getPurchases();
            List<DigitalPurchaseDTO> purchasesDTO = new ArrayList<>();
            for (DigitalPurchase singlePurchase: purchases) {
                DigitalPurchaseDTO digitalPurchaseDTO = digitalPurchaseMapper.toDto(singlePurchase);
                purchasesDTO.add(digitalPurchaseDTO);
            }
            return ResponseEntity.status(HttpStatus.OK).body(purchasesDTO);
        }else {
            throw new NoSuchElementException();
        }
    }

    @GetMapping("/subscription/{customerId}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Long customerId){
        Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customerId);
        if(optionalCustomerHistory.isPresent()){
            CustomerHistory customerHistory = optionalCustomerHistory.get();
            Subscription subscription = customerHistory.getSubscription();
            SubscriptionDTO subscriptionDTO = subscriptionMapper.toDto(subscription);
            return ResponseEntity.status(HttpStatus.OK).body(subscriptionDTO);
        }else {
            throw new NoSuchElementException();
        }
    }
}
