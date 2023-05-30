package it.develhope.javaTeam2Develhope.customer;

import it.develhope.javaTeam2Develhope.book.BookRepo;
import it.develhope.javaTeam2Develhope.book.BookService;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCard;
import it.develhope.javaTeam2Develhope.customer.customerCard.CustomerCardRepo;
import it.develhope.javaTeam2Develhope.customer.customerHistory.CustomerHistoryRepo;
import it.develhope.javaTeam2Develhope.digitalPurchase.DigitalPurchaseService;
import it.develhope.javaTeam2Develhope.notifications.NotificationService;
import it.develhope.javaTeam2Develhope.order.OrderService;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCard;
import it.develhope.javaTeam2Develhope.paymentCard.PaymentCardService;
import it.develhope.javaTeam2Develhope.subscription.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class CustomerServiceTests {
        CustomerCardRepo customerCardRepo = mock(CustomerCardRepo.class);
        PaymentCardService paymentCardService = mock(PaymentCardService.class);
        CustomerRepo customerRepo = mock(CustomerRepo.class);
        DigitalPurchaseService digitalPurchaseService = mock(DigitalPurchaseService.class);
        OrderService orderService = mock(OrderService.class);
        BookService bookService = mock(BookService.class);
        SubscriptionService subscriptionService = mock(SubscriptionService.class);
        BookRepo bookRepo = mock(BookRepo.class);
        CustomerHistoryRepo customerHistoryRepo = mock(CustomerHistoryRepo.class);
        NotificationService notificationService = mock(NotificationService.class);

        CustomerService customerService = new CustomerService(
                customerRepo,
                paymentCardService,
                digitalPurchaseService,
                customerCardRepo,
                orderService,
                bookService,
                subscriptionService,
                bookRepo,
                customerHistoryRepo, notificationService);
    @Test
    public void testUpdatePaymentMethod_ValidPaymentCard() {
        // Arrange
        Long customerCardId = 1L;
        Long paymentCardId = 2L;
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(paymentCardId);
        paymentCard.setCardType("Visa");
        paymentCard.setCardExpiry(LocalDate.parse("2025-08-07"));
        paymentCard.setCardNum(1234567890123456L);
        paymentCard.setCardHolderName("John Doe");
        paymentCard.setBalance(100.0);

        CustomerCard customerCard = new CustomerCard();
        customerCard.setId(customerCardId);
        PaymentCard existingPaymentCard = new PaymentCard();
        existingPaymentCard.setId(paymentCardId);
        existingPaymentCard.setCardType("MasterCard");
        existingPaymentCard.setCardExpiry(LocalDate.parse("2025-05-03"));
        existingPaymentCard.setCardNum(9876543210987654L);
        existingPaymentCard.setCardHolderName("Jane Smith");
        existingPaymentCard.setBalance(50.0);
        customerCard.addPaymentCard(existingPaymentCard);

        when(customerCardRepo.getReferenceById(customerCardId)).thenReturn(customerCard);
        // Act
        CustomerCard updatedCustomerCard = customerService.updatePaymentMethod(customerCardId, paymentCardId, paymentCard);

        // Assert
        Assertions.assertEquals(customerCardId, updatedCustomerCard.getId());
        Assertions.assertEquals(1, updatedCustomerCard.getPaymentCards().size());

        PaymentCard updatedPaymentCard = updatedCustomerCard.getPaymentCards().get(0);
        Assertions.assertEquals(paymentCardId, updatedPaymentCard.getId());
        Assertions.assertEquals("Visa", updatedPaymentCard.getCardType());
        Assertions.assertEquals(LocalDate.parse("2025-08-07"), updatedPaymentCard.getCardExpiry());
        Assertions.assertEquals(1234567890123456L, updatedPaymentCard.getCardNum());
        Assertions.assertEquals("John Doe", updatedPaymentCard.getCardHolderName());
        Assertions.assertEquals(100.0, updatedPaymentCard.getBalance(), 0.01);

        verify(paymentCardService, times(1)).addSinglePaymentCard(updatedPaymentCard);
    }


    @Test
    public void testRemovePaymentMethod_ValidPaymentMethod() throws Exception {
        // Arrange
        Long customerCardId = 1L;
        Long paymentCardId = 2L;

        CustomerCard customerCard = new CustomerCard();
        customerCard.setId(customerCardId);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(paymentCardId);
        customerCard.addPaymentCard(paymentCard);

        when(customerCardRepo.getReferenceById(customerCardId)).thenReturn(customerCard);

        // Act
        CustomerCard updatedCustomerCard = customerService.removePaymentMethod(customerCardId, paymentCardId);

        // Assert
        Assertions.assertEquals(customerCardId, updatedCustomerCard.getId());
        Assertions.assertTrue(updatedCustomerCard.getPaymentCards().isEmpty());

        verify(paymentCardService, times(1)).deletePaymentCard(paymentCardId);
    }

    @Test
    public void testRemovePaymentMethod_NonExistingPaymentMethod() throws Exception {
        // Arrange
        Long customerCardId = 1L;
        Long paymentCardId = 2L;

        CustomerCard customerCard = new CustomerCard();
        customerCard.setId(customerCardId);
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setId(3L);
        customerCard.addPaymentCard(paymentCard);

        when(customerCardRepo.getReferenceById(customerCardId)).thenReturn(customerCard);

        // Act and Assert
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            customerService.removePaymentMethod(customerCardId, paymentCardId);
        });

        verify(paymentCardService, times(0)).deletePaymentCard(anyLong());
    }
}

