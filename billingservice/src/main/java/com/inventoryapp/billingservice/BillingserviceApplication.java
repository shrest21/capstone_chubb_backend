package com.inventoryapp.billingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class BillingserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BillingserviceApplication.class, args);
    }

}
