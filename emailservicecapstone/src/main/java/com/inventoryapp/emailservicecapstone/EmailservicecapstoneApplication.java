package com.inventoryapp.emailservicecapstone;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableRabbit
public class EmailservicecapstoneApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailservicecapstoneApplication.class, args);
    }

}
