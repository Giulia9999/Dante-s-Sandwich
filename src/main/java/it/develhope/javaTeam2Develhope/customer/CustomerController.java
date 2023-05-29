package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.dto.CustomerCardDTO;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.customer.customerCard.dto.CustomerCardMapper;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.digitalPurchase.dto.DigitalPurchaseDTO;
import it.develhope.javaTeam2Develhope.digitalPurchase.dto.DigitalPurchaseMapper;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.order.dto.OrderDTO;
import it.develhope.javaTeam2Develhope.order.dto.OrderMapper;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
import it.develhope.javaTeam2Develhope.subscription.dto.SubscriptionDTO;
import it.develhope.javaTeam2Develhope.subscription.dto.SubscriptionMapper;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  EntityManager entityManager;
  private final CustomerService customerService;
  private final CustomerCardRepo customerCardRepo;
  private final BookService bookService;
  private final DigitalPurchaseMapper digitalPurchaseMapper;
  private final CustomerCardMapper customerCardMapper;
  private final OrderMapper orderMapper;
  private final SubscriptionMapper subscriptionMapper;
  public CustomerController(CustomerService customerService, CustomerCardRepo customerCardRepo,
                            BookService bookService, PaymentCardService paymentCardService, DigitalPurchaseMapper digitalPurchaseMapper, CustomerCardMapper customerCardMapper, OrderMapper orderMapper, SubscriptionMapper subscriptionMapper) {
    this.customerService = customerService;
    this.customerCardRepo = customerCardRepo;
    this.bookService = bookService;
    this.digitalPurchaseMapper = digitalPurchaseMapper;
    this.customerCardMapper = customerCardMapper;
    this.orderMapper = orderMapper;
    this.subscriptionMapper = subscriptionMapper;
  }

  //----------GESTIONE CARTE DI PAGAMENTO--------------
  @PostMapping("/addFirstPayment/{customerId}")
  public ResponseEntity<CustomerCardDTO> addFirstPaymentMethod(@PathVariable Long customerId, @RequestBody PaymentCard paymentCard) throws Exception, ConflictException {
    CustomerCard customerCard = customerService.addFirstPaymentMethod(paymentCard, customerId);
    customerCardRepo.save(customerCard);
    CustomerCardDTO customerCardDTO = customerCardMapper.toDto(customerCard);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerCardDTO);
  }

  @PostMapping("/addPayment/{customerCardId}")
  public ResponseEntity<CustomerCardDTO> addPaymentMethod(@PathVariable Long customerCardId, @RequestBody PaymentCard paymentCard) throws Exception, ConflictException {
    CustomerCard customerCard = customerCardRepo.findById(customerCardId).orElseThrow(EntityNotFoundException::new);
    customerCard = customerService.addPaymentMethod(customerCardId, paymentCard);
    customerCardRepo.save(customerCard);
    CustomerCardDTO customerCardDTO = customerCardMapper.toDto(customerCard);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerCardDTO);
  }
  @PutMapping("/updatePayment/{customerCardId}/{paymentCardId}")
  public ResponseEntity<CustomerCardDTO> addPaymentMethod(@PathVariable Long customerCardId, @PathVariable Long paymentCardId, @RequestBody PaymentCard paymentCard) throws Exception {
    CustomerCard customerCard = customerCardRepo.findById(customerCardId).orElseThrow(EntityNotFoundException::new);
// Call a getter method to initialize the object and force Hibernate to load the actual entity
    customerCard = customerService.updatePaymentMethod(customerCardId, paymentCardId, paymentCard);
    customerCardRepo.save(customerCard);
    CustomerCardDTO customerCardDTO = customerCardMapper.toDto(customerCard);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerCardDTO);
  }

  @DeleteMapping("/deletePayment/{customerCardId}/{paymentCardId}")
  public ResponseEntity<CustomerCardDTO> deletePaymentMethod(@PathVariable Long customerCardId,
                                                             @PathVariable Long paymentCardId) throws Exception {
    CustomerCard customerCard;
    customerCard = customerService.removePaymentMethod(customerCardId, paymentCardId);
    customerCardRepo.save(customerCard);
    CustomerCardDTO customerCardDTO = customerCardMapper.toDto(customerCard);
    return ResponseEntity.status(HttpStatus.OK).body(customerCardDTO);
  }

  //------------------METODI DI ACQUISTO---------------------

  //-----------------------LIBRO FISICO----------------------
  @PostMapping("/orderBook/{customerCardId}")
  public ResponseEntity<OrderDTO> order(@PathVariable Long customerCardId,
                                        @RequestParam Long bookId) throws BookNotFoundException, ConflictException, MessagingException {
    Order order = customerService.orderBook(customerCardId, bookId);
    OrderDTO orderDTO = orderMapper.toDto(order);
    return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
  }

  //-----------------------LIBRO DIGITALE------------------------
  @PostMapping("/digitalPurchase/{customerCardId}")
  public ResponseEntity<DigitalPurchaseDTO> purchaseDigital(@PathVariable Long customerCardId,
                                                            @RequestParam Long bookId) throws ConflictException, BookNotFoundException, IOException, MessagingException {
    DigitalPurchase digitalPurchase = customerService.buyDigitalBook(customerCardId,bookId);
    DigitalPurchaseDTO digitalPurchaseDTO = digitalPurchaseMapper.toDto(digitalPurchase);
    return ResponseEntity.status(HttpStatus.CREATED).body(digitalPurchaseDTO);
  }

  //----------------------ABBONAMENTO EBOOK--------------------
  @PostMapping("/subscription/{customerCardId}")
  public ResponseEntity<SubscriptionDTO> subscription(@PathVariable Long customerCardId,
                                                      @RequestParam(required = false) Boolean isCanceled,
                                                      @RequestParam(required = false) Boolean isRenewed) throws ConflictException {
    if(isRenewed==null) isRenewed=false;
    if(isCanceled==null) isCanceled=false;
    Subscription subscription = customerService.getSubscription(customerCardId,isCanceled, isRenewed);
    SubscriptionDTO subscriptionDTO = subscriptionMapper.toDto(subscription);
    return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionDTO);
  }


  //-----------------------METODI CRUD----------------------

  @PostMapping("/single")
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws Exception, ConflictException {
    Customer savedCustomer = customerService.createCustomer(customer);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) throws Exception {
    Customer customer = customerService.getCustomerById(id);
    if (customer == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(customer);
  }

  @GetMapping("/email")
  public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email) {
    Customer customer = customerService.getCustomerByEmail(email);
    if (customer == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(customer);
  }

  @GetMapping("/multiple")
  public ResponseEntity<Page<Customer>> getAllCustomers(
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    Page<Customer> customersPage = customerService.getAllCustomers(page, size);
    return ResponseEntity.ok(customersPage);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails) throws ConflictException {
    Customer updatedCustomer = customerService.updateCustomer(id, customerDetails);
    if (updatedCustomer == null) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(updatedCustomer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    boolean deleted = customerService.deleteCustomer(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }


  //DOCUMENTAZIONE AGGIORNATA CON USO DI DTO: https://documenter.getpostman.com/view/26043911/2s93eR5bMe

}
