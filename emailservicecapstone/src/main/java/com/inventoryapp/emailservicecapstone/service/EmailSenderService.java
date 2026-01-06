package com.inventoryapp.emailservicecapstone.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(String to, String orderId, double amount) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Order Confirmed");
        message.setText(
                "Your order has been placed successfully!\n\n" +
                        "Order ID: " + orderId + "\n" +
                        "Total Amount: ₹" + amount
        );

        mailSender.send(message);
    }
}
