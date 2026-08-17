
# Dependency Inversion Principle (DIP)

## Definition

The **Dependency Inversion Principle (DIP)** is the **fifth principle** of SOLID.

> **"High-level modules should not depend on low-level modules. Both should depend on abstractions."**
>
> — Robert C. Martin

Also,

> **"Abstractions should not depend on details. Details should depend on abstractions."**

---

# What is a High-Level Module?

A class that contains **business logic**.

Example:

- CheckoutService
- OrderService
- PaymentService
- NotificationService

---

# What is a Low-Level Module?

A class that performs implementation details.

Example:

- EmailService
- SmsService
- StripePaymentService
- RazorpayPaymentService

---

# Problem Without DIP

Suppose we are building an e-commerce application.

## Bad Example

```java
public class EmailService {

    public void send(String message) {
        System.out.println("Sending Email : " + message);
    }
}
```

```java
public class NotificationService {

    private EmailService emailService = new EmailService();

    public void notifyUser(String message) {
        emailService.send(message);
    }
}
```

### Problem

`NotificationService` directly creates the object.

```java
new EmailService();
```

If tomorrow you want to send SMS instead of Email:

```java
private SmsService smsService = new SmsService();
```

You must modify `NotificationService`.

This violates the Dependency Inversion Principle.

---

# Good Example (Using Interface)

## Step 1: Create an abstraction

```java
public interface Notification {

    void send(String message);
}
```

---

## Step 2: Implement Email

```java
public class EmailService implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending Email : " + message);
    }
}
```

---

## Step 3: Implement SMS

```java
public class SmsService implements Notification {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS : " + message);
    }
}
```

---

## Step 4: High-Level Module

```java
public class NotificationService {

    private Notification notification;

    public NotificationService(Notification notification) {
        this.notification = notification;
    }

    public void notifyUser(String message) {
        notification.send(message);
    }
}
```

Notice:

`NotificationService` depends only on the interface.

It doesn't know whether it is Email or SMS.

---

# Client Creates the Object

The **client** decides which implementation to use.

```java
public class Main {

    public static void main(String[] args) {

        Notification notification = new EmailService();

        NotificationService service =
                new NotificationService(notification);

        service.notifyUser("Welcome");
    }
}
```

Switching to SMS:

```java
Notification notification = new SmsService();

NotificationService service =
        new NotificationService(notification);
```

Only the client changes.

`NotificationService` remains unchanged.

This is the Dependency Inversion Principle.

---

# Spring Boot Example

In Spring Boot, the **Spring IoC Container** acts as the client.

Instead of using `new`, Spring creates the object and injects it.

---

## Step 1: Interface

```java
public interface NotificationService {

    void send(String message);
}
```

---

## Step 2: Email Implementation

```java
@Service
public class EmailNotificationService
        implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending Email : " + message);
    }
}
```

---

## Step 3: SMS Implementation

```java
@Service
public class SmsNotificationService
        implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Sending SMS : " + message);
    }
}
```

---

## Step 4: High-Level Module

```java
@Service
public class AlertService {

    private final NotificationService notificationService;

    public AlertService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void sendAlert(String message) {
        notificationService.send(message);
    }
}
```

Notice:

There is **no `new EmailNotificationService()`**.

`AlertService` only depends on the interface.

---

# Who Creates the Object?

Spring creates the implementation.

```text
Spring Container
        │
        ▼
EmailNotificationService
        │
        ▼
AlertService
```

If Email is replaced with SMS:

```text
Spring Container
        │
        ▼
SmsNotificationService
        │
        ▼
AlertService
```

`AlertService` never changes.

---

# Multiple Implementations

If both implementations exist:

```java
@Service
public class EmailNotificationService
        implements NotificationService { }
```

```java
@Service
public class SmsNotificationService
        implements NotificationService { }
```

Spring won't know which one to inject.

Use `@Qualifier`.

```java
@Service
public class AlertService {

    private final NotificationService notificationService;

    public AlertService(
        @Qualifier("emailNotificationService")
        NotificationService notificationService) {

        this.notificationService = notificationService;
    }
}
```

# Dependency Inversion Example - Client Chooses Implementation

## Step 1: Interface

```java
public interface NotificationService {
    void send(String message);
}
```

---

## Step 2: Implementations

```java
@Service("email")
public class EmailNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Email : " + message);
    }
}
```

```java
@Service("sms")
public class SmsNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("SMS : " + message);
    }
}
```

---

## Step 3: Factory

Spring injects all implementations.

```java
@Component
public class NotificationFactory {

    private final Map<String, NotificationService> services;

    public NotificationFactory(Map<String, NotificationService> services) {
        this.services = services;
    }

    public NotificationService getService(String type) {
        return services.get(type);
    }
}
```

Spring automatically creates:

```text
email -> EmailNotificationService
sms   -> SmsNotificationService
```

---

## Step 4: Business Service

```java
@Service
public class AlertService {

    private final NotificationFactory factory;

    public AlertService(NotificationFactory factory) {
        this.factory = factory;
    }

    public void sendAlert(String type, String message) {

        NotificationService service = factory.getService(type);

        service.send(message);
    }
}
```

Notice:

`AlertService` never knows whether it is Email or SMS.

---

## Step 5: Client

```java
@RestController
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping("/send")
    public void send(
            @RequestParam String type,
            @RequestParam String message) {

        alertService.sendAlert(type, message);
    }
}
```

---

## Request 1

```
POST /send?type=email&message=Hello
```

Output

```
Email : Hello
```

---

## Request 2

```
POST /send?type=sms&message=Hello
```

Output

```
SMS : Hello
```

---

# Flow

```
Client
   │
   │ type=email
   ▼
AlertController
   │
   ▼
AlertService
   │
   ▼
NotificationFactory
   │
   ▼
EmailNotificationService
```

For SMS:

```
Client
   │
   │ type=sms
   ▼
AlertController
   │
   ▼
AlertService
   │
   ▼
NotificationFactory
   │
   ▼
SmsNotificationService
```

---

# Why This Demonstrates DIP

- `AlertService` depends only on the `NotificationService` abstraction.
- The client decides which implementation to use by sending `type=email` or `type=sms`.
- Adding a new implementation (e.g., `WhatsAppNotificationService`) only requires:
    - Creating a new class implementing `NotificationService`.
    - Registering it as `@Service("whatsapp")`.
- No changes are needed in `AlertService`.

Now Spring injects the Email implementation.

---

# Benefits

- Loose coupling
- Easy to replace implementations
- Easier testing (mock interfaces)
- Better maintainability
- High flexibility
- Follows SOLID principles

---

# Interview Answer

## What is the Dependency Inversion Principle?

The Dependency Inversion Principle states that **high-level modules should not depend on low-level modules; both should depend on abstractions**. Instead of creating dependent objects directly using `new`, we depend on interfaces and let the client (or Spring's IoC container) provide the required implementation through dependency injection.

---

# Key Points

- Depend on interfaces, not concrete classes.
- Avoid using `new` inside business logic.
- Use constructor injection.
- In Spring Boot, the IoC container acts as the client and injects dependencies.
- Makes code loosely coupled and easier to test.

---

# Summary

| Without DIP | With DIP |
|-------------|----------|
| Depends on concrete class | Depends on interface |
| Uses `new` | Uses Dependency Injection |
| Tight coupling | Loose coupling |
| Hard to replace implementations | Easy to replace implementations |
| Difficult to test | Easy to mock and test |
````
