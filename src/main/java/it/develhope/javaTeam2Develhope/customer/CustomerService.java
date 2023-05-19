package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchaseService;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.order.OrderController;
import it.develhope.javaTeam2Develhope.order.OrderService;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
import it.develhope.javaTeam2Develhope.subscription.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
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
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
@Service
public class CustomerService {
    private final CustomerRepo customerRepo;
    private final PaymentCardService paymentCardService;
    private final DigitalPurchaseService digitalPurchaseService;
    private final CustomerCardRepo customerCardRepo;
    private final OrderService orderService;
    private final BookService bookService;
    private final OrderController orderController;
    private final SubscriptionService subscriptionService;

    public CustomerService(CustomerRepo customerRepo, PaymentCardService paymentCardService,
                           DigitalPurchaseService digitalPurchaseService, CustomerCardRepo customerCardRepo,
                           OrderService orderService, BookService bookService, OrderController orderController, SubscriptionService subscriptionService) {
        this.customerRepo = customerRepo;
        this.paymentCardService = paymentCardService;
        this.digitalPurchaseService = digitalPurchaseService;
        this.customerCardRepo = customerCardRepo;
        this.orderService = orderService;
        this.bookService = bookService;
        this.orderController = orderController;
        this.subscriptionService = subscriptionService;
    }

    //---------------------METODI GESTIONE CARTE DI PAGAMENTO---------------

    /**
     *
     * @param paymentCard The payment card datas to add for the first time
     * @param customerId The id of the costumer who's adding the poyment
     * @return The CustomerCard class
     * @throws Exception Throws exception if the customer does not exist
     */
    public CustomerCard addFirstPaymentMethod(PaymentCard paymentCard, Long customerId) throws Exception {
        CustomerCard customerCard = new CustomerCard();
        customerCard.addPaymentCard(paymentCard);
        paymentCardService.addSinglePaymentCard(paymentCard);
        customerCard.setCustomer(getCustomerById(customerId));
        return customerCard;
    }

    /**
     *
     * @param paymentCard The payment card datas to add
     * @param customerCardId The id of the costumer who's adding the poyment
     * @return The CustomerCard class, wich handles the relation between customer and card
     */
    public CustomerCard addPaymentMethod(Long customerCardId, PaymentCard paymentCard){
        CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
        customerCard.addPaymentCard(paymentCard);
        paymentCardService.addSinglePaymentCard(paymentCard);
        return customerCard;
    }

    /**
     *
     * @param paymentCard The new payment card datas
     * @param paymentCardId The payment card to update
     * @param customerCardId The id of the costumer who's adding the poyment card
     * @return The updated CustomerCard class
     */
    public CustomerCard updatePaymentMethod(Long customerCardId,Long paymentCardId, PaymentCard paymentCard){
        CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
        for (PaymentCard singleCard: customerCard.getPaymentCards()) {
            if(singleCard.getId().equals(paymentCardId)){
                singleCard.setCardType(paymentCard.getCardType());
                singleCard.setCardExpiry(paymentCard.getCardExpiry());
                singleCard.setCardNum(paymentCard.getCardNum());
                singleCard.setCardHolderName(paymentCard.getCardHolderName());
                singleCard.setBalance(paymentCard.getBalance());
                paymentCardService.addSinglePaymentCard(singleCard);
            }
        }
        return customerCard;
    }

    /**
     *
     * @param paymentCardId The id of the paymentCard to be eliminated
     * @param customerCardId The id of CustomerCard class
     * @return The updated CustomerCard class
     */
    public CustomerCard removePaymentMethod(Long customerCardId, Long paymentCardId) throws Exception {
        CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
        boolean paymentMethodExists = false;

        for (PaymentCard singleCard : customerCard.getPaymentCards()) {
            if (singleCard.getId().equals(paymentCardId)) {
                customerCard.removePaymentCard(singleCard);
                paymentCardService.deletePaymentCard(singleCard.getId());
                paymentMethodExists = true;
                break;
            }
        }

        if (!paymentMethodExists) {
            throw new EntityNotFoundException("Payment method does not exist");
        }

        return customerCard;
    }

    //--------------------------METODI DI ACQUISTO--------------------

    //-----------------------------LIBRO FISICO-----------------------
    public Order orderBook(Long customerCardId, Long bookId, Boolean isGift){
        Order order = new Order();
        float shippingCost = 2.5f;
        Book book = null;
        try {
            book = bookService.getBookById(bookId);
        } catch (BookNotFoundException e) {
            e.printStackTrace();
        }
        CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
        order.setCustomerCard(customerCard);
        order.setBook(book);
        order.setGift(isGift);
        order.setDateOfOrder(LocalDateTime.from(LocalDateTime.now()));
        order.setDateOfShipping(LocalDateTime.now().plusDays(1).toLocalDate());
        order.setDateOfArrival(order.getDateOfShipping().plusDays(2));
        order.setDetails("Ordine effettuato");
        if(book != null){
            order.setTotalPrice(book.getPrice() + shippingCost);
            orderService.addSingleOrder(order);
        }
        return order;
    }

    //--------------------------------LIBRO DIGITALE----------------------------
    public DigitalPurchase buyDigitalBook(Long customerCardId, Long bookId, Boolean isGift){
    DigitalPurchase digitalPurchase = new DigitalPurchase();
    Book book = null;
    try{
        book = bookService.getBookById(bookId);
    } catch (BookNotFoundException e) {
        e.printStackTrace();
    }
    CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
    digitalPurchase.setCustomerCard(customerCard);
    digitalPurchase.setPurchasedBook(book);
    digitalPurchase.setGift(isGift);
    digitalPurchase.setDateOfPurchase(LocalDate.from(LocalDateTime.now()));
    digitalPurchase.setDetails("E-book acquistato");
    if(book != null){
        digitalPurchase.setTotalPrice(digitalPurchase.getTotalPrice());
        digitalPurchaseService.addSingleDigitalPurchase(digitalPurchase);
        }
    return digitalPurchase;
    }

    //--------------------------------ABBONAMENTO EBOOK------------------------------
    public Subscription buySubscription(Long customerCardId, Boolean isCanceled, Boolean isRenewed){
        Subscription subscription = new Subscription();
        CustomerCard customerCard = new CustomerCard();
        subscription.setCustomerCard(customerCard);
        List<Book> books = new ArrayList<>();
        subscription.setBooks(books);
        subscription.setDateOfSubscription(LocalDate.from(LocalDateTime.now()));
        subscription.setApproved(true);
        subscription.setCanceled(isCanceled);
        subscription.setRenewed(isRenewed);
        subscription.setMonthlyPrice(5.99f);
        subscription.setDetails("Abbonamento attivo");
        subscriptionService.addSingleSubscription(subscription);
        return subscription;
    }


    //---------------------METODI CRUD---------------------
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
        if (customerOptional.isEmpty()) {
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

