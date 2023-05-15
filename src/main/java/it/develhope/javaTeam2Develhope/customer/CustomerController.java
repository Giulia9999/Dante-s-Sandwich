package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer")
public class CustomerController {

  private final CustomerService customerService;
  private final CustomerCardRepo customerCardRepo;
  private final BookService bookService;

  public CustomerController(CustomerService customerService, CustomerCardRepo customerCardRepo,
                            BookService bookService, PaymentCardService paymentCardService) {
    this.customerService = customerService;
    this.customerCardRepo = customerCardRepo;
    this.bookService = bookService;
  }

  //AGGIUNGI METODO DI PAGAMENTO
  @PostMapping("/addFirstPayment/{customerId}")
  public ResponseEntity<CustomerCard> addFirstPaymentMethod(@PathVariable Long customerId, @RequestBody PaymentCard paymentCard) throws Exception, ConflictException {
    CustomerCard customerCard = customerService.addFirstPaymentMethod(paymentCard, customerId);
    customerCardRepo.save(customerCard);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerCard);
  }

  @PostMapping("/addPayment/{customerCardId}")
  public ResponseEntity<CustomerCard> addPaymentMethod(@PathVariable Long customerCardId, @RequestBody PaymentCard paymentCard) throws Exception, ConflictException {
    CustomerCard customerCard = customerCardRepo.findById(customerCardId).orElseThrow(EntityNotFoundException::new);
// Call a getter method to initialize the object and force Hibernate to load the actual entity
    customerCard.getPaymentCards().size();
    customerCard = customerService.addPaymentMethod(customerCardId, paymentCard);
    customerCardRepo.save(customerCard);
    return ResponseEntity.status(HttpStatus.CREATED).body(customerCard);
  }


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

}
