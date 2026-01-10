# Smart Order & Inventory Management System – Backend

## Overview

This project is a **microservices-based Smart Order & Inventory Management System** built using **Spring Boot**, **Spring Cloud**, and **Docker**, with **Jenkins CI** for build automation.

The system is designed to handle **orders, products, inventory (warehouse), billing, authentication**, and **service discovery** in a scalable and loosely coupled architecture.

---

## Microservices & Ports

| Microservice                  | Description                                                 | Default Port     |
| ----------------------------- | ----------------------------------------------------------- | ---------------- |
| API Gateway                   | Single entry point for frontend and external clients        | 8080             |
| Auth Service                  | Handles authentication, JWT generation, and user validation | 8081             |
| Order Service                 | Manages order creation, status updates, and order workflow  | 8082             |
| Product Service               | Manages product catalog and product details                 | 8083             |
| Warehouse (Inventory) Service | Manages stock, warehouses, and inventory updates            | 8084             |
| Billing Service               | Handles billing, invoices, and payment-related logic        | 8085             |
| Eureka Server                 | Service discovery and registration                          | 8761             |
| Config Server                 | Centralized configuration management                        | 8888             |
| RabbitMQ                      | Message broker for async notifications                      | 5672 (UI: 15672) |
| PostgreSQL (per service)      | Database for each microservice                              | 5432             |

---

## Microservices Responsibilities

| Microservice      | Core Responsibility                                                         |
| ----------------- | --------------------------------------------------------------------------- |
| API Gateway       | Routes requests to internal services, enforces security, centralizes access |
| Auth Service      | User login, JWT token creation, authentication & authorization              |
| Order Service     | Order placement, order lifecycle, business rules validation                 |
| Product Service   | Product CRUD operations, product availability                               |
| Warehouse Service | Inventory stock management, warehouse-product mapping                       |
| Billing Service   | Bill generation, payment calculation, order billing                         |
| Eureka Server     | Dynamic service registration and discovery                                  |
| Config Server     | Externalized and centralized configuration for all services                 |
| RabbitMQ          | Sends email/notification messages on order events                           |

---

## Security Overview

* **JWT-based authentication**
* JWT stored in **HTTP-only cookies**
* API Gateway validates token before routing requests
* Role-based access for users, admins, and warehouse managers

---

## CI/CD – Jenkins Setup & Build Instructions

### Step 1: Start Jenkins (WSL – WAR based)

Jenkins is installed locally in **WSL** and runs using a WAR file.

```bash
java -jar /usr/share/jenkins/jenkins.war
```

> Jenkins home directory is located at:

```
~/.jenkins
```

---

### Step 2: Open Jenkins Dashboard

Open your browser and go to:

```
http://localhost:8080
```

Login using your Jenkins credentials.

---

### Step 3: Trigger the Build

1. Open your Jenkins job
2. Click **"Build Now"**
3. Jenkins will:

   * Pull latest code from GitHub
   * Build all microservices
   * Run unit tests
   * Generate JAR files

---

## Architecture Highlights

* Microservices architecture with **independent databases**
* Service-to-service communication using **OpenFeign**
* Centralized configuration via **Spring Cloud Config**
* Asynchronous notifications using **RabbitMQ**
* CI automation using **Jenkins**

---

## Summary

This backend system is designed for **scalability, maintainability, and fault isolation**, following real-world enterprise microservices best practices.

---

© Smart Order & Inventory Management System
