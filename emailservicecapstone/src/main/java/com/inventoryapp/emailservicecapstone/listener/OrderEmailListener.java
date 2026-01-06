package com.inventoryapp.emailservicecapstone.listener;

import com.inventoryapp.emailservicecapstone.config.RabbitMQConfig;
import com.inventoryapp.emailservicecapstone.event.OrderCreatedEvent;
import com.inventoryapp.emailservicecapstone.service.EmailSenderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class OrderEmailListener {

    private final EmailSenderService emailSenderService;

    public OrderEmailListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleOrderCreated(OrderCreatedEvent event) {

        emailSenderService.sendOrderConfirmation(
                event.getUserEmail(),
                event.getOrderId(),
                event.getTotalAmount()
        );
    }
}
