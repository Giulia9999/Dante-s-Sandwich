package it.develhope.javaTeam2Develhope.notifications;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {

    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    AuthCode authCode;

    public void sendWelcome(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Welcome to Dante's Sandwich!";
        String message = "Dear customer, we give you a great welcome to Dante's Sandwich! Discover all our purchase possibilities! Best regards from Dante's Sandwich Team";
        helper.setSubject(subject);
        helper.setText(message);
        helper.setTo(email);
        helper.setFrom("maxpower88999@gmail.com");//da cambiare con la mail di Dante's Sandwich
        javaMailSender.send(mimeMessage);
    }

    public void sendAuthCode(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Authentication code";
        String message = "Insert the code in the authentication: " + authCode.getCode();
        helper.setSubject(subject);
        helper.setText(message);
        helper.setTo(email);
        helper.setFrom("maxpower88999@gmail.com");//da cambiare con la mail di Dante's Sandwich
        javaMailSender.send(mimeMessage);
    }

    public void sendOrderNotification(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Thanks for your purchase!";
        String message = "Dear customer, we are delighted to announce that your order is completed! It will be yours in just a couple of days! Best regards from Dante's Sandwich Team";
        helper.setSubject(subject);
        helper.setText(message);
        helper.setTo(email);
        helper.setFrom("maxpower88999@gmail.com");//da cambiare con la mail di Dante's Sandwich
        javaMailSender.send(mimeMessage);
    }

    public void sendDigitalPurchaseNotification(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Thanks for your purchase! Your e-book is waiting for you!";
        String message = "Dear customer, thanks for for the purchase! We are delighted to announce that your ebook is already available in your account! Enjoy it! Best regards from Dante's Sandwich Team";
        helper.setSubject(subject);
        helper.setText(message);
        helper.setTo(email);
        helper.setFrom("maxpower88999@gmail.com");//da cambiare con la mail di Dante's Sandwich
        javaMailSender.send(mimeMessage);
    }

    public void sendSubscriptionNotification(String email) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String subject = "Thanks for your subscription! A ton of books is just waiting for you!";
        String message = "Dear customer, we are delighted to announce that your subscription is activated! Enjoy tons and tons of books from your tablet! Best regards from Dante's Sandwich Team";
        helper.setSubject(subject);
        helper.setText(message);
        helper.setTo(email);
        helper.setFrom("maxpower88999@gmail.com");//da cambiare con la mail di Dante's Sandwich
        javaMailSender.send(mimeMessage);
    }

}
