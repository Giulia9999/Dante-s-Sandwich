package it.develhope.javaTeam2Develhope.paymentCard;
import org.junit.Test;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

@SpringJUnitConfig
public class PaymentCardServiceTest {

    @Test
    public void testValidatePaymentCard_ValidCard() {
        // Arrange
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setBalance(100.0);

        double purchaseAmount = 50.0;

        PaymentCardService paymentCardService = new PaymentCardService(mock(PaymentCardRepo.class));

        // Act and Assert
        assertDoesNotThrow(() -> paymentCardService.validatePaymentCard(paymentCard, purchaseAmount));
    }

    @Test
    public void testValidatePaymentCard_NullCard() {
        // Arrange
        PaymentCard paymentCard = null;

        double purchaseAmount = 50.0;

        PaymentCardService paymentCardService = new PaymentCardService(mock(PaymentCardRepo.class));

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> paymentCardService.validatePaymentCard(paymentCard, purchaseAmount));

        assertEquals("Payment card cannot be null.", exception.getMessage());
    }

    @Test
    public void testValidatePaymentCard_InsufficientBalance() {
        // Arrange
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setBalance(20.0);

        double purchaseAmount = 50.0;

        PaymentCardService paymentCardService = new PaymentCardService(mock(PaymentCardRepo.class));

        // Act and Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> paymentCardService.validatePaymentCard(paymentCard, purchaseAmount));

        assertEquals("Insufficient balance on payment card.", exception.getMessage());
    }

}
