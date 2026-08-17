
# Open/Closed Principle (OCP)

## Definition

The **Open/Closed Principle (OCP)** is the second principle of SOLID.

> **"Software entities should be open for extension but closed for modification."**  
> — Robert C. Martin (Uncle Bob)

This means:

- **Open for Extension** → You can add new functionality.
- **Closed for Modification** → You should not change existing, working code.

Instead of modifying a class every time a new requirement comes, extend its behavior.

---

# Why OCP?

Suppose your application supports two payment methods:

- Credit Card
- UPI

Later, the business asks to add:

- PayPal
- Apple Pay
- Google Pay

If you keep modifying the same class every time, you increase the chance of introducing bugs.

OCP says:

> **Don't modify existing code. Extend it.**

---

# Bad Example (Violates OCP)

```java
class PaymentService {

    public void pay(String paymentType) {

        if (paymentType.equals("CARD")) {
            System.out.println("Card Payment");
        } else if (paymentType.equals("UPI")) {
            System.out.println("UPI Payment");
        }
    }
}
```

### Problem

When PayPal is added:

```java
else if(paymentType.equals("PAYPAL")) {
    ...
}
```

When Apple Pay is added:

```java
else if(paymentType.equals("APPLE_PAY")) {
    ...
}
```

The class must be modified every time.

This **violates OCP**.

---

# Good Example (Follows OCP)

## Step 1: Create an interface

```java
interface Payment {
    void pay();
}
```

---

## Step 2: Create implementations

```java
class CardPayment implements Payment {

    @Override
    public void pay() {
        System.out.println("Card Payment");
    }
}
```

```java
class UpiPayment implements Payment {

    @Override
    public void pay() {
        System.out.println("UPI Payment");
    }
}
```

Later, adding PayPal:

```java
class PaypalPayment implements Payment {

    @Override
    public void pay() {
        System.out.println("PayPal Payment");
    }
}
```

Notice:

- No existing class is modified.
- Only a new class is added.

---

## Step 3: Client

```java
class PaymentService {

    public void process(Payment payment) {
        payment.pay();
    }
}
```

Usage:

```java
PaymentService service = new PaymentService();

service.process(new CardPayment());
service.process(new UpiPayment());
service.process(new PaypalPayment());
```

---

# Spring Boot Example

```java
public interface NotificationService {
    void send(String message);
}
```

Email implementation:

```java
@Service
class EmailNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("Email Sent");
    }
}
```

SMS implementation:

```java
@Service
class SmsNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("SMS Sent");
    }
}
```

Client:

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

To support WhatsApp notifications:

```java
class WhatsappNotificationService implements NotificationService {

    @Override
    public void send(String message) {
        System.out.println("WhatsApp Sent");
    }
}
```

No changes are required in `AlertService`.

---

# Benefits

- Easy to extend
- Existing code remains stable
- Fewer bugs
- Better maintainability
- Encourages interfaces and polymorphism
- Easier testing

---

# Interview Answer

**What is the Open/Closed Principle?**

The Open/Closed Principle states that a software entity should be **open for extension but closed for modification**. Instead of changing existing classes whenever a new requirement comes, we should extend the system by creating new implementations using interfaces or abstract classes. This reduces regression bugs and makes the code easier to maintain.

---

# Key Points

- Open = Add new functionality.
- Closed = Don't modify existing code.
- Use interfaces or abstract classes.
- Prefer polymorphism over `if-else` or `switch`.
- Add new classes instead of changing old ones.

---

# Summary

| Bad Design | Good Design |
|------------|-------------|
| Modify existing class | Create a new implementation |
| Large `if-else` blocks | Interfaces + Polymorphism |
| Hard to maintain | Easy to extend |
| Higher risk of bugs | Stable and scalable |
````
