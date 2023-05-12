package it.develhope.javaTeam2Develhope.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

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
