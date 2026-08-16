# Facade Design Pattern

## Definition
The **Facade Design Pattern** is a **Structural Design Pattern** that provides a **simple and unified interface to a complex subsystem** containing multiple classes or services.

In simple words, instead of the client directly calling many different services, we create **one Facade class** that handles those calls internally.

> **Facade = One simple entry point to a complex system.**

---

# Why Facade?
Consider an **Order Processing System**.

To place an order, the client may need to call:

```text
InventoryService
PaymentService
InvoiceService
NotificationService
```

Without Facade:

```text
Client
  |
  ├── InventoryService
  ├── PaymentService
  ├── InvoiceService
  └── NotificationService
```

The client needs to understand the complete order-processing flow.

With Facade:

```text
Client
  |
  v
OrderFacade
  |
  ├── InventoryService
  ├── PaymentService
  ├── InvoiceService
  └── NotificationService
```

Now the client simply calls:

```java
orderFacade.placeOrder();
```

---

# Problem Without Facade

```java
public class Main {
    public static void main(String[] args) {
        InventoryService inventory = new InventoryService();
        PaymentService payment = new PaymentService();
        InvoiceService invoice = new InvoiceService();
        NotificationService notification = new NotificationService();

        inventory.checkStock();
        payment.makePayment();
        invoice.generateInvoice();
        notification.sendNotification();
    }
}
```

Problems:

- Client knows all subsystem classes.
- Client needs to know the correct execution order.
- Tight coupling with subsystem classes.
- Client code becomes complex.
- Changes in the subsystem may affect the client.
- Same workflow may be duplicated in multiple places.

---

# Solution
Introduce an `OrderFacade`.

```java
class OrderFacade {
    private InventoryService inventory = new InventoryService();
    private PaymentService payment = new PaymentService();
    private InvoiceService invoice = new InvoiceService();
    private NotificationService notification = new NotificationService();

    void placeOrder() {
        inventory.checkStock();
        payment.makePayment();
        invoice.generateInvoice();
        notification.sendNotification();
    }
}
```

Client now only needs:

```java
OrderFacade facade = new OrderFacade();
facade.placeOrder();
```

---

# Components of Facade Pattern

## 1. Client
The **Client** uses the Facade instead of directly communicating with multiple subsystem classes.

```text
Main / Controller
```

## 2. Facade
The **Facade** provides a simple interface and coordinates subsystem calls.

```text
OrderFacade
```

## 3. Subsystem Classes
These classes perform the actual work.

```text
InventoryService
PaymentService
InvoiceService
NotificationService
```

Structure:

```text
             Client
                |
                v
          OrderFacade
                |
      ┌─────────┼──────────┐
      |         |          |
      v         v          v
 Inventory   Payment    Invoice
 Service     Service    Service
                           |
                           v
                    Notification
                       Service
```

---

# Simple Java Example

```java
public class FacadeExample {

    /**
     * SUBSYSTEM
     * Handles inventory operations.
     */
    static class InventoryService {
        void checkStock() {
            System.out.println("Stock checked");
        }
    }

    /**
     * SUBSYSTEM
     * Handles payment operations.
     */
    static class PaymentService {
        void makePayment() {
            System.out.println("Payment completed");
        }
    }

    /**
     * SUBSYSTEM
     * Handles invoice generation.
     */
    static class InvoiceService {
        void generateInvoice() {
            System.out.println("Invoice generated");
        }
    }

    /**
     * SUBSYSTEM
     * Handles customer notification.
     */
    static class NotificationService {
        void sendNotification() {
            System.out.println("Notification sent");
        }
    }

    /**
     * FACADE
     * Provides a simple interface to the complex subsystem.
     */
    static class OrderFacade {
        private InventoryService inventory = new InventoryService();
        private PaymentService payment = new PaymentService();
        private InvoiceService invoice = new InvoiceService();
        private NotificationService notification = new NotificationService();

        void placeOrder() {
            inventory.checkStock();
            payment.makePayment();
            invoice.generateInvoice();
            notification.sendNotification();
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        OrderFacade facade = new OrderFacade();
        facade.placeOrder();
    }
}
```

Output:

```text
Stock checked
Payment completed
Invoice generated
Notification sent
```

---

# Internal Flow

Client calls only:

```java
orderFacade.placeOrder();
```

Internally:

```text
placeOrder()
    |
    ├── checkStock()
    |
    ├── makePayment()
    |
    ├── generateInvoice()
    |
    └── sendNotification()
```

The client does not need to understand these internal steps.

---

# Before vs After Facade

## Without Facade

```text
Client
  |
  ├── checkStock()
  ├── makePayment()
  ├── generateInvoice()
  └── sendNotification()
```

Client knows everything.

## With Facade

```text
Client
  |
  v
placeOrder()
  |
OrderFacade
  |
  ├── Inventory
  ├── Payment
  ├── Invoice
  └── Notification
```

Client knows only the Facade.

---

# Real-World Software Examples

## E-Commerce Order

```text
OrderFacade
├── InventoryService
├── PaymentService
├── ShippingService
└── NotificationService
```

## User Registration

```text
RegistrationFacade
├── UserService
├── ValidationService
├── EmailService
└── AuditService
```

Client calls:

```java
registrationFacade.registerUser();
```

## Banking Transaction

```text
TransactionFacade
├── AccountService
├── FraudService
├── LedgerService
└── NotificationService
```

Client calls:

```java
transactionFacade.transfer();
```

## Application Startup

```text
ApplicationFacade
├── Database
├── Cache
├── Configuration
└── MessageBroker
```

---

# Advantages

- Provides a simple interface.
- Hides subsystem complexity.
- Reduces client coupling.
- Makes client code cleaner.
- Centralizes complex workflows.
- Makes subsystem changes easier to manage.
- Client doesn't need to know execution details.

---

# Disadvantages

- Facade can become too large.
- Too much logic in Facade can create a **God Class**.
- Adds another abstraction layer.
- A badly designed Facade may become tightly coupled to every subsystem.

---

# When to Use
Use Facade when:

- A subsystem has many classes.
- Client code is becoming complex.
- Client needs to call multiple services for one operation.
- You want a simple entry point to a subsystem.
- You want to hide implementation details.
- The same workflow is repeated in multiple places.

Examples:

```text
Order Processing
User Registration
Payment Processing
Application Startup
Booking System
Banking Transaction
```

---

# When Not to Use
Avoid Facade when:

- The subsystem is already simple.
- Client needs direct control over every subsystem operation.
- Facade only forwards one simple method without providing any simplification.

---

# Design Considerations
Facade should mainly **coordinate** subsystem classes.

Good:

```text
OrderFacade
  |
  ├── InventoryService
  ├── PaymentService
  └── NotificationService
```

Avoid putting all actual business implementation inside:

```text
OrderFacade
```

The subsystem services should still perform their own responsibilities.

> **Facade coordinates; subsystem classes do the actual work.**

---

# Facade vs Adapter

```text
Facade  → Simplifies an interface
Adapter → Converts an interface
```

| Facade | Adapter |
|---|---|
| Simplifies complex subsystem | Makes incompatible interfaces compatible |
| Usually works with many classes | Usually adapts one interface to another |
| Does not mainly change compatibility | Changes interface for compatibility |
| Goal is simplicity | Goal is compatibility |

Easy trick:

```text
Facade  = Simplify
Adapter = Convert
```

---

# Facade vs Proxy

```text
Facade → Simplifies access
Proxy  → Controls access
```

Proxy may provide:

```text
Security
Caching
Lazy Loading
Remote Access
```

Facade mainly provides:

```text
Simple Entry Point
```

---

# Facade vs Decorator

```text
Facade    → Simplifies multiple services
Decorator → Adds behavior to an object
```

---

# Facade vs Mediator

```text
Facade   → Client uses a simple interface to subsystem
Mediator → Objects communicate through a central mediator
```

Facade mainly simplifies how the **client accesses a subsystem**.

Mediator mainly reduces communication dependencies **between multiple objects**.

---

# Pitfalls

- Do not put every application responsibility inside one Facade.
- Avoid creating a God Class.
- Keep subsystem responsibilities separate.
- Facade should simplify and coordinate.
- Do not expose unnecessary subsystem details.
- Create multiple Facades if one becomes too large.

---

# Interview Questions

## What is Facade Pattern?
Facade is a Structural Design Pattern that provides a simple and unified interface to a complex subsystem.

## Which category does Facade belong to?
**Structural Design Pattern**

## What problem does Facade solve?
It hides subsystem complexity and reduces the number of classes the client needs to interact with.

## What are the main components?
- Client
- Facade
- Subsystem Classes

## Does Facade contain business logic?
Facade can contain **coordination/workflow logic**, but the actual responsibilities should remain inside the subsystem classes.

## Can the client directly access subsystem classes?
Yes. Facade does not necessarily prevent direct access. It simply provides an easier way to use the subsystem.

## What is a real-world software example?
```text
OrderFacade
├── InventoryService
├── PaymentService
├── InvoiceService
└── NotificationService
```

## Facade vs Adapter?
```text
Facade  → Simplifies
Adapter → Converts
```

## Facade vs Proxy?
```text
Facade → Simplifies access
Proxy  → Controls access
```

## What is the biggest advantage of Facade?
It hides complexity and gives the client a **single simple entry point**.

---

# Key Points

- Category: **Structural Design Pattern**
- Provides a simple interface to a complex subsystem.
- Client communicates mainly with the Facade.
- Facade coordinates multiple subsystem classes.
- Subsystem classes perform the actual work.
- Reduces client coupling.
- Hides implementation complexity.
- Avoid making Facade a God Class.

---

# Easy Trick to Remember
Think about a **hotel reception desk**.

Instead of contacting:

```text
Housekeeping
Restaurant
Laundry
Taxi Service
```

you contact:

```text
Reception
```

Reception coordinates everything internally.

In software:

```text
Client
  |
  v
Facade
  |
  ├── Service A
  ├── Service B
  └── Service C
```

> **Facade = One simple door to many complex services.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Simplify access to a complex subsystem |
| Client | Uses Facade |
| Facade | Simple entry point/coordinator |
| Subsystem | Performs actual work |
| Main Benefit | Hides complexity |
| Common Example | Order Processing |
| Risk | Facade becoming God Class |
| Easy Trick | Facade = One Door to Many Services |