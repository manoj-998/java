# Bridge Design Pattern
## Definition

The **Bridge Design Pattern** is a **Structural Design Pattern** that separates an **abstraction from its implementation**, allowing both to change independently without affecting each other.

In simple words, Bridge separates **WHAT we want to do** from **HOW we want to do it**. For example, `Notification` defines **what** type of notification we want to send, while `MessageSender` defines **how** we send it using Email, SMS, or WhatsApp.

```text
WHAT                         HOW
Notification   ──Bridge──>   MessageSender
   |                            |
   ├── OTP                      ├── Email
   ├── Order                    ├── SMS
   └── Alert                    └── WhatsApp
```

> **Easy Understanding:** Bridge separates two things that can change independently and connects them using composition.

# Why Bridge?
Suppose we are building a Notification System.

We have different notification types:

```text
OTP Notification
Order Notification
Alert Notification
```

And different ways to send them:

```text
Email
SMS
WhatsApp
```

Without Bridge, we may create:

```text
EmailOTPNotification
SMSOTPNotification
WhatsAppOTPNotification

EmailOrderNotification
SMSOrderNotification
WhatsAppOrderNotification
```

As notification types and senders increase, the number of classes increases rapidly.

This is called **Class Explosion**.

---

# Problem Without Bridge

```java
class EmailOTPNotification {
    void send() {
        System.out.println("OTP through Email");
    }
}

class SMSOTPNotification {
    void send() {
        System.out.println("OTP through SMS");
    }
}

class EmailOrderNotification {
    void send() {
        System.out.println("Order through Email");
    }
}
```

Problems:

- Too many classes
- Duplicate code
- Difficult to maintain
- Adding new combinations creates more classes
- Notification and delivery mechanism become tightly coupled

---

# Solution
Separate the two independent parts:

```text
WHAT?
Notification
├── OTPNotification
└── OrderNotification

HOW?
MessageSender
├── EmailSender
├── SMSSender
└── WhatsAppSender
```

Connect them using composition:

```text
Notification
     |
     | Bridge
     v
MessageSender
```

Now both sides can change independently.

---

# Main Components

## Abstraction
The high-level part used by the client.

```text
Notification
```

## Refined Abstraction
Different types of abstraction.

```text
OTPNotification
OrderNotification
```

## Implementor
Defines how the work should be performed.

```text
MessageSender
```

## Concrete Implementor
Actual implementations.

```text
EmailSender
SMSSender
WhatsAppSender
```

---

# Simple Java Example

## Implementor

```java
interface MessageSender {
    void send(String message);
}
```

## Concrete Implementations

```java
class EmailSender implements MessageSender {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}

class SMSSender implements MessageSender {
    public void send(String message) {
        System.out.println("SMS: " + message);
    }
}
```

## Abstraction

```java
abstract class Notification {
    protected MessageSender sender;
    Notification(MessageSender sender) {
        this.sender = sender;
    }
    abstract void notifyUser();
}
```

## Refined Abstraction

```java
class OTPNotification extends Notification {
    OTPNotification(MessageSender sender) {
        super(sender);
    }

    void notifyUser() {
        sender.send("Your OTP is 1234");
    }
}
```

## Usage

```java
public class Main {
    public static void main(String[] args) {
        Notification smsOTP = new OTPNotification(new SMSSender());
        smsOTP.notifyUser();

        Notification emailOTP = new OTPNotification(new EmailSender());
        emailOTP.notifyUser();
    }
}
```

Output:

```text
SMS: Your OTP is 1234
Email: Your OTP is 1234
```

---

# Internal Flow

```text
Client
  |
  v
OTPNotification
  |
  | HAS-A
  v
MessageSender
  |
  +---- EmailSender
  |
  +---- SMSSender
```

The relationship between `Notification` and `MessageSender` forms the **Bridge**.

---

# Adding New Notification Type
Suppose we need an Order Notification.

```java
class OrderNotification extends Notification {
    OrderNotification(MessageSender sender) {
        super(sender);
    }

    void notifyUser() {
        sender.send("Your order is confirmed");
    }
}
```

Now we can use:

```java
Notification notification =
        new OrderNotification(new SMSSender());

notification.notifyUser();
```

Or:

```java
Notification notification =
        new OrderNotification(new EmailSender());

notification.notifyUser();
```

No new classes such as:

```text
SMSOrderNotification
EmailOrderNotification
```

are required.

---

# Adding New Sender
Suppose we add WhatsApp.

```java
class WhatsAppSender implements MessageSender {
    public void send(String message) {
        System.out.println("WhatsApp: " + message);
    }
}
```

Now existing notifications can use it:

```java
Notification notification =
        new OTPNotification(new WhatsAppSender());

notification.notifyUser();
```

No changes are required in `OTPNotification`.

This is the main benefit of Bridge:

> **Both sides can grow independently.**

---

# Without Bridge vs With Bridge

## Without Bridge

```text
OTP
├── EmailOTP
├── SMSOTP
└── WhatsAppOTP

Order
├── EmailOrder
├── SMSOrder
└── WhatsAppOrder
```

Too many combinations.

## With Bridge

```text
       Notification
       /          \
     OTP          Order
       |
       | Bridge
       v
   MessageSender
   /     |      \
Email   SMS   WhatsApp
```

---

# Real-World Software Examples

## Notification + Sender

```text
Notification
     |
     | Bridge
     v
MessageSender
```

Examples:

```text
OTP + SMS
OTP + Email
Order + WhatsApp
Alert + Email
```

## Report + Exporter

```text
Report
  |
  | Bridge
  v
Exporter
```

Examples:

```text
SalesReport + PDF
SalesReport + Excel
AuditReport + CSV
```

## Payment + Gateway

```text
Payment
   |
   | Bridge
   v
PaymentGateway
```

Examples:

```text
UPIPayment + Razorpay
CardPayment + Stripe
RefundPayment + Razorpay
```

---

# Advantages

- Prevents class explosion.
- Reduces tight coupling.
- Uses composition over excessive inheritance.
- Abstraction can change independently.
- Implementation can change independently.
- Easy to add new abstractions.
- Easy to add new implementations.
- Improves maintainability.

---

# Disadvantages

- Introduces additional interfaces and classes.
- Can make simple applications unnecessarily complex.
- Requires correctly identifying two independent dimensions.

---

# When to Use
Use Bridge when:

- You have two independent dimensions.
- Both dimensions can change independently.
- Many combinations would otherwise require subclasses.
- You want to avoid class explosion.
- You want composition instead of excessive inheritance.

Example:

```text
Notification Type + Delivery Method
Report Type + Export Format
Payment Type + Payment Gateway
Remote Type + Device
```

---

# When Not to Use
Avoid Bridge when:

- There is only one implementation.
- There are no independent dimensions.
- The number of combinations is very small.
- Separation adds unnecessary complexity.

---

# Design Considerations
Instead of:

```text
EmailOTPNotification
SMSOTPNotification
EmailOrderNotification
SMSOrderNotification
```

Prefer:

```text
Notification HAS-A MessageSender
```

Bridge favors:

> **Composition over Inheritance**

---

# Bridge vs Adapter

| Bridge | Adapter |
|---|---|
| Separates abstraction from implementation | Makes incompatible interfaces compatible |
| Usually designed intentionally | Usually added to integrate existing code |
| Prevents class explosion | Solves interface mismatch |
| Both sides can change independently | Acts as a translator |

Easy trick:

```text
Bridge  = Separate
Adapter = Convert
```

---

# Bridge vs Strategy

Both commonly use composition, but their intent is different.

```text
Bridge   → Separates abstraction and implementation
Strategy → Provides interchangeable algorithms/behaviors
```

Example:

```text
Bridge:
Notification + MessageSender

Strategy:
PaymentService + PaymentStrategy
```

---

# Pitfalls

- Do not use Bridge when only one dimension changes.
- Do not create unnecessary abstraction layers.
- Keep abstraction and implementation responsibilities separate.
- Do not put sender-specific logic inside `Notification`.
- Prefer composition between the two hierarchies.

---

# Interview Questions

## What is Bridge Pattern?
Bridge is a Structural Design Pattern that separates abstraction from implementation so both can change independently.

## Which category does Bridge belong to?
**Structural Design Pattern**

## What problem does Bridge solve?
It mainly prevents **class explosion** caused by multiple independent dimensions.

## What are the main components?
- Abstraction
- Refined Abstraction
- Implementor
- Concrete Implementor

## Does Bridge use inheritance or composition?
Bridge mainly uses **composition** to connect abstraction with implementation.

## What is a real-world software example?
```text
Notification + MessageSender
Report + Exporter
Payment + PaymentGateway
```

## Bridge vs Adapter?
```text
Bridge  → Separates two independent dimensions
Adapter → Makes incompatible interfaces compatible
```

## What is the biggest advantage of Bridge?
Abstraction and implementation can **change independently**.

---

# Key Points

- Category: **Structural Design Pattern**
- Separates abstraction from implementation.
- Think **WHAT vs HOW**.
- Prevents class explosion.
- Uses composition.
- Both sides can evolve independently.
- Useful when there are two independent dimensions.
- Easy to add new combinations without creating many subclasses.

---

# Easy Trick to Remember

Think:

```text
WHAT
 |
 | Bridge
 |
HOW
```

For our example:

```text
WHAT?
OTP Notification

      |
      | Bridge
      v

HOW?
SMS Sender
```

> **Bridge = Separate WHAT from HOW so both can change independently.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Separate abstraction from implementation |
| Main Problem | Class Explosion |
| Main Technique | Composition |
| Abstraction | Notification |
| Implementation | MessageSender |
| Example | OTP Notification + SMS Sender |
| Main Benefit | Both sides change independently |
| Easy Trick | Bridge = WHAT + HOW separated |