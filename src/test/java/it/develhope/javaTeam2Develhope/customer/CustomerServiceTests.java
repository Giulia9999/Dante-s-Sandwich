package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.Book;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchaseService;
import it.develhope.javaTeam2Develhope.order.Order;
import it.develhope.javaTeam2Develhope.order.OrderController;
import it.develhope.javaTeam2Develhope.order.OrderService;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;
import java.util.Optional;

@SpringJUnitConfig
public class CustomerServiceTests {

    @Mock
    private CustomerRepo customerRepo;

    @Mock
    private PaymentCardService paymentCardService;

    @Mock
    private DigitalPurchaseService digitalPurchaseService;

    @Mock
    private CustomerCardRepo customerCardRepo;

    @Mock
    private OrderService orderService;

    @Mock
    private BookService bookService;

    @Mock
    private OrderController orderController;

    @InjectMocks
    private CustomerService customerService;

    private PaymentCard paymentCard;

    private Customer customer;

    private Book book;

    private CustomerCard customerCard;

    private Order order;

    @Before
    public void setUp() {
        paymentCard = new PaymentCard();
        paymentCard.setId(1L);
        paymentCard.setCardNum(12345678923423L);
        paymentCard.setCardType("VISA");
        paymentCard.setCardExpiry(LocalDate.parse("01-25-2020"));
        paymentCard.setCardHolderName("Test User");

        customer = new Customer();
        customer.setId(1L);
        customer.setEmail("test@example.com");
        customer.setPassword("password");

        book = new Book();
        book.setId(1L);
        book.setPrice(10.0f);

        customerCard = new CustomerCard();
        customerCard.setId(1L);
        customerCard.setCustomer(customer);
        customerCard.addPaymentCard(paymentCard);

        order = new Order();
        order.setBook(book);
        order.setCustomerCard(customerCard);
        order.setGift(false);
        order.setTotalPrice(book.getPrice() + 2.5f);

        Mockito.when(customerRepo.findByEmail(customer.getEmail())).thenReturn(null);
        Mockito.when(customerRepo.findById(customer.getId())).thenReturn(Optional.of(customer));
        Mockito.when(customerCardRepo.getReferenceById(customerCard.getId())).thenReturn(customerCard);
        Mockito.when(bookService.getBookById(book.getId())).thenReturn(book);
    }

    @Test
    public void testAddFirstPaymentMethod() throws Exception {
        Mockito.when(paymentCardService.addSinglePaymentCard(paymentCard)).thenReturn(paymentCard);
        CustomerCard result = customerService.addFirstPaymentMethod(paymentCard, customer.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCustomer().getId(), customer.getId());
        Assertions.assertEquals(result.getPaymentCards().size(), 1);
        Assertions.assertEquals(result.getPaymentCards().get(0).getId(), paymentCard.getId());
    }

    @Test
    public void testAddPaymentMethod() {
        Mockito.when(paymentCardService.addSinglePaymentCard(paymentCard)).thenReturn(paymentCard);
        CustomerCard result = customerService.addPaymentMethod(customerCard.getId(), paymentCard);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCustomer().getId(), customer.getId());
        Assertions.assertEquals(result.getPaymentCards().size(), 2);
        Assertions.assertEquals(result.getPaymentCards().get(1).getId(), paymentCard.getId());
    }

    @Test
    public void testUpdatePaymentMethod() {
        PaymentCard updatedPaymentCard = new PaymentCard();
        updatedPaymentCard.setId(1L);
        updatedPaymentCard.setCardNum("987654321");
        updatedPaymentCard.setCardType("MASTERCARD");
        updatedPaymentCard.setCardExpiry("01/30");
        updatedPaymentCard.setCardHolderName("Test User 2");
        updatedPaymentCard.setBalance(100.0f);

        CustomerCard result = customerService.updatePaymentMethod(customerCard.getId(), paymentCard.getId(), updatedPaymentCard);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCustomer().getId(), customer.getId());
        Assertions.assertEquals(result.getPaymentCards().size(), 1);
        PaymentCard updatedCard = result.getPaymentCards().get(0);
        Assertions.assertEquals(updatedCard.getId(), updatedPaymentCard.getId());
        Assertions.assertEquals(updatedCard.getCardNum(), updatedPaymentCard.getCardNum());
        Assertions.assertEquals(updatedCard.getCardType(), updatedPaymentCard.getCardType());
        Assertions.assertEquals(updatedCard.getCardExpiry(), updatedPaymentCard.getCardExpiry());
        Assertions.assertEquals(updatedCard.getCardHolderName(), updatedPaymentCard.getCardHolderName());
        Assertions.assertEquals(updatedCard.getBalance(), updatedPaymentCard.getBalance());
    }

    @Test
    public void testRemovePaymentMethod() throws Exception {
        CustomerCard result = customerService.removePaymentMethod(customerCard.getId(), paymentCard.getId());
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getCustomer().getId(), customer.getId());
        Assert.assertEquals(result.getPaymentCards().size(), 0);
    }

    @Test
    public void testPlaceOrder() throws Exception {
        Mockito.when(orderService.addSingleOrder(order)).thenReturn(order);
        Order result = customerService.orderBook(customerCard.setId(1L, ord););
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getCustomerCard().getId(), customerCard.getId());
        Assertions.assertEquals(result.getBook().getId(), book.getId());
        Assertions.assertEquals(result.getTotalPrice(), order.getTotalPrice());
    }

    @Test
    public void testGetCustomer() throws Exception {
        Customer result = customerService.getCustomerById(customer.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), customer.getId());
        Assertions.assertEquals(result.getEmail(), customer.getEmail());
        Assertions.assertEquals(result.getPassword(), customer.getPassword());
    }

    @Test
    public void testUpdateCustomer() throws ConflictException {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setEmail("test2@example.com");
        updatedCustomer.setPassword("password2");

        Mockito.when(customerRepo.save(updatedCustomer)).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(updatedCustomer.getId(), updatedCustomer);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getId(), updatedCustomer.getId());
        Assertions.assertEquals(result.getEmail(), updatedCustomer.getEmail());
        Assertions.assertEquals(result.getPassword(), updatedCustomer.getPassword());
    }
}

