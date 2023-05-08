package it.develhope.javaTeam2Develhope.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {

  @Autowired
  private CustomerRepo customerRepo;

  //INSERIMENTO NUOVO UTENTE
  @PostMapping("/single")
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) throws Exception {

    try {
      Optional<Customer> existingCustomer = Optional.ofNullable(customerRepo.findByEmail(customer.getEmail()));
      if (existingCustomer.isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
      }
      Customer customerX = new Customer();
      customerX.setName(customer.getName());
      customerX.setSurname(customer.getSurname());
      customerX.setBirthday(customer.getBirthday());
      customerX.setAddress(customer.getAddress());
      customerX.setEmail(customer.getEmail());
      customerX.setPassword(customer.getPassword());
      customerX.setDateOfSubscription(customer.getDateOfSubscription());

      String passwordSalt = generatePasswordSalt();
      String passwordHash = generatePasswordHash(customer.getPassword(), passwordSalt);
      customerX.setPasswordSalt(passwordSalt);
      customerX.setPasswordHash(passwordHash);

      Customer savedCustomer = customerRepo.save(customerX);

      return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);

    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Errore durante l'inserimento del cliente nel database");
    }
  }

  //CERCA UTENTE BY ID
  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) throws Exception {

    try {
      Optional<Customer> customerOptional = customerRepo.findById(id);

      if (customerOptional.isPresent()) {
        return ResponseEntity.ok(customerOptional.get());
      } else{
        return ResponseEntity.notFound().build();
      }


    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception("Errore durante la lettura del database");
    }
  }

  //CERCA UTENTE BY EMAIL

  @GetMapping("/customer/email")
  public ResponseEntity<Customer> getCustomerByEmail(@RequestParam String email){
    try{
      Customer customer = customerRepo.findByEmail(email);
      if(customer != null){
        return ResponseEntity.ok(customer);
      }else{
        return ResponseEntity.notFound().build();
      }
    } catch(Exception e){
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }


  //GET ALL CUSTOMER
  @GetMapping("/multiple")
  public ResponseEntity<Page<Customer>> getAllCustomer(@RequestParam (defaultValue = "0")int page, @RequestParam (defaultValue = "10")int size) {
    try{
      Pageable pegeable = PageRequest.of(page, size);
      Page<Customer> customersPage = customerRepo.findAll(pegeable);
      return ResponseEntity.ok(customersPage);
    } catch (Exception e){
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

  }

  //MODIIFICA CUSTOMER

  @PutMapping("/customer/{id}")
  public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer customerDetails){

    try{
      Optional<Customer> customer = customerRepo.findById(id);
      if(customer.isPresent()){
        Customer customerY = customer.get();

        if(!customerY.getEmail().equals(customerDetails.getEmail())){
          Optional<Customer> existingCustomer = Optional.ofNullable(customerRepo.findByEmail(customerDetails.getEmail()));
          if(existingCustomer.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();

          }
        }

        customerY.setName(customerDetails.getName());
        customerY.setSurname(customerDetails.getSurname());
        customerY.setBirthday(customerDetails.getBirthday());
        customerY.setAddress(customerDetails.getAddress());
        customerY.setEmail(customerDetails.getEmail());
        customerY.setPassword(customerDetails.getPassword());

        customerRepo.save(customerY);

        return ResponseEntity.ok(customerY); //Ok 200 è stato modificato

      } else{
        return ResponseEntity.notFound().build(); //Err 404 Customer non trovato
      }

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //Err 500 errore lettura db
    }
  }




  @DeleteMapping("/customer/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

    try {
      Optional<Customer> customerO = customerRepo.findById(id);

      if(customerO.isPresent()){
        customerRepo.deleteById(id);
        return ResponseEntity.noContent().build(); // Err 204 Elliminato con successo
      } else{
        return ResponseEntity.notFound().build(); //Err 404 Non trovato
      }

    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); //Err 500 errore lettura DB
    }
  }

  //METODI UTILI
  private String generatePasswordSalt() {
    SecureRandom random = new SecureRandom();
    byte[] salt = new byte[16];
    random.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }

  public String getPassword(String email, String password) {
    Customer customer = customerRepo.findByEmail(email);
    if (customer != null) {
      String salt = customer.getPasswordSalt();
      String hash = customer.getPasswordHash();
      String hashedPassword = generatePasswordHash(password, salt);
      if (hashedPassword.equals(hash)) {
        return password;
      }
    }
    return null;
  }

  private String generatePasswordHash(String password, String salt) {
    String algorithm = "PBKDF2WithHmacSHA256";
    int iterations = 65536;
    int keyLength = 128;
    char[] passwordChars = password.toCharArray();
    byte[] saltBytes = Base64.getDecoder().decode(salt);
    SecretKeyFactory factory = null;
    try {
      factory = SecretKeyFactory.getInstance(algorithm);
      PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
      SecretKey key = factory.generateSecret(spec);
      byte[] hashBytes = key.getEncoded();
      return Base64.getEncoder().encodeToString(hashBytes);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      e.printStackTrace();
      return null;
    }
  }


}
