package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.book.BookNotFoundException;
import it.develhope.javaTeam2Develhope.book.BookRepo;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.customer.customerHistory.CustomerHistory;
import it.develhope.javaTeam2Develhope.customer.customerHistory.CustomerHistoryRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchase;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchaseService;
import it.develhope.javaTeam2Develhope.notifications.NotificationService;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.order.OrderService;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import it.develhope.javaTeam2Develhope.subscription.Subscription;
import it.develhope.javaTeam2Develhope.subscription.SubscriptionService;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
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
    private final SubscriptionService subscriptionService;
    private final BookRepo bookRepo;
    private final CustomerHistoryRepo customerHistoryRepo;
    private final NotificationService notificationService;
    public CustomerService(CustomerRepo customerRepo, PaymentCardService paymentCardService,
                           DigitalPurchaseService digitalPurchaseService, CustomerCardRepo customerCardRepo,
                           OrderService orderService, BookService bookService, SubscriptionService subscriptionService,
                           BookRepo bookRepo, CustomerHistoryRepo customerHistoryRepo, NotificationService notificationService) {
        this.customerRepo = customerRepo;
        this.paymentCardService = paymentCardService;
        this.digitalPurchaseService = digitalPurchaseService;
        this.customerCardRepo = customerCardRepo;
        this.orderService = orderService;
        this.bookService = bookService;
        this.subscriptionService = subscriptionService;
        this.bookRepo = bookRepo;
        this.customerHistoryRepo = customerHistoryRepo;
        this.notificationService = notificationService;
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
        paymentCardService.validatePaymentCard(paymentCard);
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
        paymentCardService.validatePaymentCard(paymentCard);
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
        paymentCardService.validatePaymentCard(paymentCard);
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
    public Order orderBook(Long customerCardId, Long bookId) throws ConflictException, MessagingException {
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
        order.setDateOfOrder(LocalDateTime.from(LocalDateTime.now()));
        order.setDateOfShipping(LocalDateTime.now().plusDays(1).toLocalDate());
        order.setDateOfArrival(order.getDateOfShipping().plusDays(2));
        order.setDetails("Ordine effettuato");
        if(book != null){
            //validazione metodo di pagamento
            for (PaymentCard card: customerCard.getPaymentCards()) {
                paymentCardService.validatePaymentCard(card, book.getPrice() + shippingCost);
            }
            order.setTotalPrice(book.getPrice() + shippingCost);
            orderService.addSingleOrder(order);
            notificationService.sendOrderNotification(customerCard.getCustomer().getEmail());
            Customer customer = customerCard.getCustomer();
            Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customer.getId());
            //Add order to customer history
            CustomerHistory customerHistory;
            if(optionalCustomerHistory.isPresent()){
                customerHistory = optionalCustomerHistory.get();
            }else {
                customerHistory = new CustomerHistory();
                customerHistory.setCustomer(customer);
            }
            customerHistory.getOrders().add(order);
            customerHistoryRepo.save(customerHistory);
        }
        return order;
    }

    //--------------------------------LIBRO DIGITALE----------------------------
    public DigitalPurchase buyDigitalBook(Long customerCardId, Long bookId) throws ConflictException, BookNotFoundException, IOException, MessagingException {
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
    digitalPurchase.setDateOfPurchase(LocalDateTime.from(LocalDateTime.now()));
    digitalPurchase.setDetails("E-book acquistato");
    if(book != null){
        //validazione metodo di pagamento
        for (PaymentCard card: customerCard.getPaymentCards()) {
            paymentCardService.validatePaymentCard(card, book.getPrice());
        }
        digitalPurchase.setTotalPrice(book.getPrice());
        digitalPurchaseService.addSingleDigitalPurchase(digitalPurchase);
        notificationService.sendDigitalPurchaseNotification(customerCard.getCustomer().getEmail());
        Customer customer = customerCard.getCustomer();
        Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customer.getId());
        //Add purchase to customer history
        CustomerHistory customerHistory;
        if(optionalCustomerHistory.isPresent()){
            customerHistory = optionalCustomerHistory.get();
        }else {
            customerHistory = new CustomerHistory();
            customerHistory.setCustomer(customer);
        }
        customerHistory.getPurchases().add(digitalPurchase);
        bookService.downloadPDF(book.getId());
        bookService.downloadMP3(book.getId());
        customerHistoryRepo.save(customerHistory);
    }
    return digitalPurchase;
    }

    //--------------------------------ABBONAMENTO EBOOK------------------------------
    public Subscription getSubscription(Long customerCardId, Boolean isCanceled, Boolean isRenewed) throws ConflictException, MessagingException {
        Subscription subscription = new Subscription();
        float monthlyPrice = 5.99f;
        CustomerCard customerCard = customerCardRepo.getReferenceById(customerCardId);
        subscription.setCustomerCard(customerCard);
        List<Book> books = bookRepo.findAll();
        subscription.setBooks(books);
        subscription.setDateOfSubscription(LocalDateTime.from(LocalDateTime.now()));
        subscription.setApproved(true);
        subscription.setCanceled(isCanceled);
        subscription.setRenewed(isRenewed);
        subscription.setMonthlyPrice(monthlyPrice);

        if (isCanceled) {
            subscription.setDetails("Subscription canceled");
        } else if (isRenewed) {
            subscription.setDetails("Subscription renewed");
        } else {
            subscription.setDetails("Active subscription");
        }
        //validazione metodo di pagamento
        for (PaymentCard card: customerCard.getPaymentCards()) {
            paymentCardService.validatePaymentCard(card, monthlyPrice);
        }
        subscriptionService.addSingleSubscription(subscription);
        notificationService.sendSubscriptionNotification(customerCard.getCustomer().getEmail());
        Customer customer = customerCard.getCustomer();
        Optional<CustomerHistory> optionalCustomerHistory = customerHistoryRepo.findById(customer.getId());
        //Add subscription to customer history
        CustomerHistory customerHistory;
        if(optionalCustomerHistory.isPresent()){
            customerHistory = optionalCustomerHistory.get();
        }else {
            customerHistory = new CustomerHistory();
            customerHistory.setCustomer(customer);
        }
        customerHistory.setSubscription(subscription);
        customerHistoryRepo.save(customerHistory);
        return subscription;
    }


    //---------------------METODI CRUD---------------------
    public Customer createCustomer(Customer customer) throws ConflictException, MessagingException {
        Optional<Customer> existingCustomer = Optional.ofNullable(customerRepo.findByEmail(customer.getEmail()));
        if (existingCustomer.isPresent()) {
            throw new ConflictException("A customer with this email already exists");
        }
        notificationService.sendWelcome(customer.getEmail());
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

    /*private String generatePasswordSalt() {
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
    }*/

}

