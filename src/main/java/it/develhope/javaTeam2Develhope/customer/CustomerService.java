package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchaseService;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final PaymentCardService paymentCardService;
    private final DigitalPurchaseService digitalPurchaseService;
    private final CustomerCardRepo customerCardRepo;

    public CustomerService(CustomerRepo customerRepo, PaymentCardService paymentCardService,
                           DigitalPurchaseService digitalPurchaseService, CustomerCardRepo customerCardRepo) {
        this.customerRepo = customerRepo;
        this.paymentCardService = paymentCardService;
        this.digitalPurchaseService = digitalPurchaseService;
        this.customerCardRepo = customerCardRepo;
    }

    public CustomerCard addCustomerPaymentCard(PaymentCard paymentCard, Long customerId) throws Exception {
        CustomerCard customerCard = new CustomerCard();
        customerCard.setPaymentCard(paymentCard);
        customerCard.setCostumer(getCustomerById(customerId));
        paymentCardService.addSinglePaymentCard(paymentCard);
        customerCardRepo.save(customerCard);
        return customerCard;
    }

    // Method to remove a payment card from the customer's list of cards
    public void removeCustomerCard(Long customerCard) throws Exception {
        customerCardRepo.deleteById(customerCard);
    }
    // Method to add a payment card to the customer's list of cards
    public DigitalPurchase buyDigitalBook(CustomerCard customerCard, Book book) throws ConflictException {
        // Calculate the price of the book
        float totalPrice = book.getPrice();

        // Validate the payment card
        paymentCardService.validatePaymentCard(customerCard.getPaymentCard(), totalPrice);

        // Create a new digital purchase object
        DigitalPurchase digitalPurchase = new DigitalPurchase();
        digitalPurchase.setCustomer(customerCard.getCostumer());
        digitalPurchase.setPurchasedBook(book);
        digitalPurchase.setDateOfPurchase(LocalDate.from(LocalDateTime.now()));
        digitalPurchase.setDetails("Purchased book: " + book.getTitle());
        digitalPurchase.setCustomerCard(customerCard);
        digitalPurchase.setTotalPrice(totalPrice);

        // Save the digital purchase
        digitalPurchaseService.addSingleDigitalPurchase(digitalPurchase);

        // Deduct the total price from the payment card balance
        customerCard.getPaymentCard().setBalance(customerCard.getPaymentCard().getBalance() - totalPrice);
        paymentCardService.updatePaymentCardPartially(customerCard.getPaymentCard().getId(),customerCard.getPaymentCard());

        // Add the digital purchase to the customer's purchase history
        customerCard.getCostumer().getPurchases().add(digitalPurchase);
        updateCustomer(customerCard.getCostumer().getId(), customerCard.getCostumer());
        return digitalPurchase;
    }


    public Customer createCustomer(Customer customer) throws ConflictException {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepo.findByEmail(customer.getEmail()));
        if (existingCustomer.isPresent()) {
            throw new ConflictException("A customer with this email already exists");
        }
        String passwordSalt = generatePasswordSalt();
        String passwordHash = generatePasswordHash(customer.getPassword(), passwordSalt);
        customer.setPasswordSalt(passwordSalt);
        customer.setPasswordHash(passwordHash);
        return customerRepo.save(customer);
    }

    public Customer getCustomerById(Long id) throws Exception {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        return customerOptional.orElse(null);
    }

    public Customer getCustomerByEmail(String email) {
        return customerRepo.findByEmail(email);
    }

    public Page<Customer> getAllCustomers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return customerRepo.findAll(pageable);
    }

    public Customer updateCustomer(Long id, Customer customerDetails) throws ConflictException {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        if (customerOptional.isEmpty()) {
            return null;
        }
        Customer customer = customerOptional.get();
        if (!customer.getEmail().equals(customerDetails.getEmail())) {
            Optional<Customer> existingCustomer = Optional.ofNullable(customerRepo.findByEmail(customerDetails.getEmail()));
            if (existingCustomer.isPresent()) {
                throw new ConflictException("A customer with this email already exists");
            }
        }
        customer.setName(customerDetails.getName());
        customer.setSurname(customerDetails.getSurname());
        customer.setBirthday(customerDetails.getBirthday());
        customer.setAddress(customerDetails.getAddress());
        customer.setEmail(customerDetails.getEmail());
        customer.setPassword(customerDetails.getPassword());
        return customerRepo.save(customer);
    }

    public boolean deleteCustomer(Long id) {
        Optional<Customer> customerOptional = customerRepo.findById(id);
        if (!customerOptional.isPresent()) {
            return false;
        }
        customerRepo.deleteById(id);
        return true;
    }

    private String generatePasswordSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generatePasswordHash(String password, String salt) {
        String algorithm = "PBKDF2WithHmacSHA256";
        int iterations = 65536;
        int keyLength = 128;
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);
        SecretKeyFactory factory;
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

