package com.inventoryapp.warehouseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WarehouseserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarehouseserviceApplication.class, args);
    }

}
