# Adapter Design Pattern

## Definition

The **Adapter Design Pattern** is a **Structural Design Pattern** that allows **two incompatible interfaces to work together**.

It acts as a **bridge or translator** between the interface expected by the client and the interface provided by an existing class or third-party service.

> **Adapter converts one interface into another interface expected by the client.**

---

## Class Adapter
A **Class Adapter uses inheritance** to adapt an existing class to the interface expected by the client.
```text
Class Adapter = Inheritance = IS-A
```

## Object Adapter
An **Object Adapter uses composition** to adapt an existing class to the interface expected by the client.

The Adapter contains an object of the Adaptee and delegates the request to it.


# Which is Best?
In Java, **Object Adapter is generally preferred**.
Reasons:
- Uses Composition
- Provides loose coupling
- More flexible
- Easier to test
- Easier to replace the Adaptee
- Follows **Favor Composition over Inheritance**


# Why Adapter?

In real-world applications, we often integrate with:

- Third-Party APIs
- Legacy Systems
- Payment Gateways
- External Libraries
- Cloud Providers
- Email/SMS Services
- Authentication Providers

These systems may expose interfaces different from what our application expects.

For example, our application expects:

```java
interface PaymentProcessor {

    void pay(double amount);
}
```

But Razorpay provides:

```java
class RazorpayGateway {

    public void makePayment(double amount) {
        System.out.println("Payment completed");
    }
}
```

The interfaces are incompatible.

```text
Application expects
pay()
        ↓
Third-party provides
makePayment()
```
The Adapter Pattern makes them compatible without modifying either side.

---

# Problem Without Adapter

Suppose our checkout service directly communicates with different payment providers.

```java
class CheckoutService {
    public void checkout(String type, double amount) {
        if ("RAZORPAY".equals(type)) {
            RazorpayGateway razorpay = new RazorpayGateway();
            razorpay.makePayment(amount);
        } else if ("STRIPE".equals(type)) {
            StripeGateway stripe = new StripeGateway();
            stripe.charge(amount);
        } else if ("PAYPAL".equals(type)) {
            PayPalGateway paypal = new PayPalGateway();
            paypal.sendPayment(amount);
        }
    }
}
```

The business logic now knows about:

```text
Razorpay → makePayment()
Stripe   → charge()
PayPal   → sendPayment()
```

Problems:
- Tight coupling
- Large `if/else` blocks
- Business logic depends on vendor-specific APIs
- Difficult to replace providers
- Difficult to test
- Third-party API changes affect business logic
- Violates the **Open/Closed Principle**

---

# Solution

Create a common interface expected by the application.

```java
interface PaymentProcessor {
    void pay(double amount);
}
```

Then create an Adapter for each incompatible provider.

```text
                PaymentProcessor
                       ▲
                       │
          ┌────────────┴────────────┐
          │                         │
   RazorpayAdapter            StripeAdapter
          │                         │
          ▼                         ▼
   RazorpayGateway            StripeGateway
```

The application communicates only with:

```java
PaymentProcessor
```
The Adapter handles the provider-specific API.

---

# Main Components

The Adapter Pattern contains four main components.

## Client

The **Client** is the class that wants to use the functionality.

Example:

```text
CheckoutService
```

---

## Target

The **Target** is the interface expected by the client.

```java
interface PaymentProcessor {
    void pay(double amount);
}
```

---

## Adaptee

The **Adaptee** is the existing class with an incompatible interface.

```java
class RazorpayGateway {

    public void makePayment(double amount) {

        System.out.println(
                "Payment completed using Razorpay"
        );
    }
}
```

---

## Adapter

The **Adapter** implements the Target interface and internally communicates with the Adaptee.

```java
class RazorpayAdapter implements PaymentProcessor {

    private final RazorpayGateway razorpayGateway;

    public RazorpayAdapter(
            RazorpayGateway razorpayGateway) {
        this.razorpayGateway = razorpayGateway;
    }

    @Override
    public void pay(double amount) {
        razorpayGateway.makePayment(amount);
    }
}
```

---

# Internal Flow

```text
Client
   │
   ▼
PaymentProcessor.pay()
   │
   ▼
RazorpayAdapter
   │
Translate / Convert
   │
   ▼
RazorpayGateway.makePayment()
   │
   ▼
Payment Completed
```

---

# Complete Java Example

## Target Interface

```java
public interface PaymentProcessor {
    void pay(double amount);
}
```

---

## Adaptee

```java
public class RazorpayGateway {
    public void makePayment(double amount) {
        System.out.println("Payment of ₹" + amount + " completed using Razorpay");
    }
}
```

---

## Adapter

```java
public class RazorpayAdapter implements PaymentProcessor {

    private final RazorpayGateway razorpayGateway;
    public RazorpayAdapter(RazorpayGateway razorpayGateway) {
        this.razorpayGateway = razorpayGateway;
    }

    @Override
    public void pay(double amount) {
        razorpayGateway.makePayment(amount);
    }
}
```

---

## Client

```java
public class CheckoutService {

    private final PaymentProcessor paymentProcessor;

    public CheckoutService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public void checkout(double amount) {
        System.out.println("Starting checkout...");
        paymentProcessor.pay(amount);
        System.out.println("Checkout completed.");
    }
}
```

---

## Usage

```java
public class Main {
    public static void main(String[] args) {
        RazorpayGateway razorpayGateway = new RazorpayGateway();
        PaymentProcessor paymentProcessor = new RazorpayAdapter(razorpayGateway);
        CheckoutService checkoutService = new CheckoutService(paymentProcessor);
        checkoutService.checkout(2500);
    }
}
```

Output:

```text
Starting checkout...
Payment of ₹2500.0 completed using Razorpay
Checkout completed.
```

---

# Adding Another Provider

Suppose Stripe provides a different API.
```java
public class StripeGateway {
    public void charge(double amount) {
        System.out.println("Payment of ₹" + amount + " completed using Stripe");
    }
}
```

Create a Stripe Adapter.
```java
public class StripeAdapter implements PaymentProcessor {
    private final StripeGateway stripeGateway;
    public StripeAdapter(StripeGateway stripeGateway) {
        this.stripeGateway = stripeGateway;
    }

    @Override
    public void pay(double amount) {
        stripeGateway.charge(amount);
    }
}
```

Usage:

```java
PaymentProcessor processor = new StripeAdapter(new StripeGateway());
CheckoutService checkoutService = new CheckoutService(processor);
checkoutService.checkout(1000);
```

Notice that:

```java
CheckoutService
``` 
does not need to change.

---

# Object Adapter

The **Object Adapter** uses **Composition**.

```java
class RazorpayAdapter implements PaymentProcessor {
    private final RazorpayGateway gateway;
    public RazorpayAdapter(RazorpayGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public void pay(double amount) {
        gateway.makePayment(amount);
    }
}
```

Relationship:

```text
Adapter
   HAS-A
Adaptee
```

Advantages:
- More flexible
- Loosely coupled
- Easier to test
- Follows composition over inheritance
- Easier to replace the Adaptee

> **Object Adapter is generally preferred in Java.**

---

# Class Adapter

The **Class Adapter** uses **Inheritance**.

```java
class RazorpayAdapter extends RazorpayGateway implements PaymentProcessor {

    @Override
    public void pay(double amount) {
        makePayment(amount);
    }
}
```

Relationship:

```text
Adapter
   IS-A
Adaptee
```

Disadvantages:
- More tightly coupled
- Less flexible
- Depends on inheritance
- Java does not support multiple class inheritance

For most Java applications, prefer:
```text
Object Adapter using Composition
```

---

# Adapter Can Transform Data

Adapter does not only convert method names.

It can also transform:
- Parameters
- Return Values
- Request Objects
- Response Objects
- Data Structures
- Units
- Exceptions
- Protocols

---

# Example: Temperature Adapter
Suppose an external weather service returns temperature in Fahrenheit.

```java
class USWeatherService {
    public double getTemperatureInFahrenheit() {
        return 86;
    }
}
```
Our application expects Celsius.

```java
interface WeatherService {
    double getTemperatureInCelsius();
}
```

Create an Adapter.

```java
class WeatherAdapter implements WeatherService {
    private final USWeatherService service;
    public WeatherAdapter(USWeatherService service) {
        this.service = service;
    }

    @Override
    public double getTemperatureInCelsius() {
        double fahrenheit = service.getTemperatureInFahrenheit();
        return (fahrenheit - 32) * 5 / 9;
    }
}
```

The Adapter performs:

```text
Fahrenheit
     │
     ▼
Adapter
     │
     ▼
Celsius
```
This shows that an Adapter can perform actual data conversion in addition to method delegation.

---

# Real-World Example

Think of a **Travel Power Adapter**.

Suppose you have:

```text
Indian Plug
```

but the hotel provides:

```text
European Socket
```

They are incompatible.

You do not modify the plug.

You do not modify the socket.

Instead:

```text
Indian Plug
     │
     ▼
Power Adapter
     │
     ▼
European Socket
```
The same concept applies in software.

```text
Client
   │
   ▼
Adapter
   │
   ▼
Incompatible Service
```
---

# Real-World Software Examples

Adapter Pattern is commonly used for:

- Payment Gateway Integration
- Legacy System Integration
- External REST APIs
- SOAP Services
- Cloud Storage APIs
- Email Providers
- SMS Providers
- Authentication Providers
- Logging Libraries
- Database Drivers

---

# Advantages

- Allows incompatible interfaces to work together.
- Reuses existing classes without modifying them.
- Reduces coupling with third-party libraries.
- Keeps business logic clean.
- Isolates vendor-specific code.
- Makes providers easier to replace.
- Improves testability.
- Supports the Open/Closed Principle.
- Supports the Dependency Inversion Principle.

---

# Disadvantages

- Introduces additional classes.
- Adds another abstraction layer.
- Complex interfaces may require complex mapping.
- Too many adapters can increase codebase size.
- Poorly designed adapters can become difficult to maintain.

---

# When to Use

Use Adapter Pattern when:

- Integrating a third-party API.
- Integrating legacy code.
- Two interfaces are incompatible.
- Existing classes cannot be modified.
- Multiple vendors expose different APIs.
- Vendor-specific code should be isolated.
- Providers may need to be replaced later.
- External data needs to be converted into an internal model.

---

# When Not to Use

Avoid Adapter Pattern when:

- Both classes are under your control.
- Interfaces can easily be redesigned.
- There is no compatibility problem.
- Adding an Adapter only introduces unnecessary complexity.

---

# Implementation Considerations

- Define a clean Target interface owned by your application.
- Keep third-party code outside business logic.
- Prefer composition over inheritance.
- Keep each Adapter focused on one integration.
- Convert vendor-specific exceptions when necessary.
- Convert external DTOs into internal models when required.
- Avoid exposing third-party types through your Target interface.

---

# Design Considerations

Business logic should depend on:

```java
PaymentProcessor
```

instead of:

```java
RazorpayGateway
```

Good design:

```text
CheckoutService

      │
      ▼

PaymentProcessor

      ▲
      │

RazorpayAdapter

      │
      ▼
RazorpayGateway
```

This keeps business logic independent of the external provider.

---

# Adapter and SOLID Principles

## Single Responsibility Principle

The Adapter has one main responsibility:

```text
Translate one interface into another.
```

---

## Open/Closed Principle

New integrations can be added by creating new Adapters.

```text
                 PaymentProcessor
                        ▲
                        │
             ┌──────────┼──────────┐
             │          │          │
        Razorpay     Stripe      PayPal
        Adapter      Adapter     Adapter
```

Existing business logic does not need modification.

---

## Dependency Inversion Principle

High-level business logic depends on:

```java
PaymentProcessor
```

instead of:

```java
RazorpayGateway
```

---

# Pitfalls

- Do not put business logic inside the Adapter.
- Do not expose third-party classes through application interfaces.
- Do not create one huge Adapter for every provider.
- Avoid unnecessary Adapter layers.
- Keep each Adapter focused on one integration.
- Handle third-party exceptions properly.
- Avoid leaking vendor-specific DTOs into the business layer.

Bad:

```java
class PaymentAdapter {
    public void pay(String provider, double amount) {
        if ("RAZORPAY".equals(provider)) {
            // Razorpay
        }
        if ("STRIPE".equals(provider)) {
            // Stripe
        }
        if ("PAYPAL".equals(provider)) {
            // PayPal
        }
    }
}
```

Better:
```text
RazorpayAdapter
StripeAdapter
PayPalAdapter
```
---



# Interview Questions

## What is Adapter Pattern?

Adapter is a **Structural Design Pattern** that converts the interface of an existing class into another interface expected by the client.

---

## Why do we need Adapter Pattern?

We use Adapter when two components need to work together but expose incompatible interfaces.

---

## What are the main components of Adapter Pattern?
The four main components are:

- Client
- Target
- Adapter
- Adaptee
---

## What is Target?
Target is the interface expected by the client.
Example:

```java
PaymentProcessor
```

---

## What is Adaptee?
Adaptee is the existing class whose interface is incompatible with what the client expects.

Example:

```java
RazorpayGateway
```

---

## What is the role of Adapter?

The Adapter:

- Implements the Target interface.
- Receives requests from the client.
- Converts those requests when necessary.
- Delegates the request to the Adaptee.

---

## What is the difference between Object Adapter and Class Adapter?

Object Adapter uses:

```text
Composition

HAS-A relationship
```

Class Adapter uses:

```text
Inheritance

IS-A relationship
```

Object Adapter is generally preferred in Java.

---

## Why is Composition preferred?

Composition provides:

- Lower coupling
- Better flexibility
- Easier testing
- Easier replacement
- Less dependency on inheritance

---

## Can Adapter transform data?

Yes.

Adapter can transform:

- Method names
- Parameters
- Request objects
- Response objects
- Units
- Exceptions
- Protocols

---


## Stripe and Razorpay expose different APIs. Which pattern would you use?

Use the **Adapter Pattern**.

Define:

```java
PaymentProcessor
```

Then create:

```text
RazorpayAdapter

StripeAdapter
```

Business logic depends only on:

```java
PaymentProcessor
```

---

## Where can Adapter Pattern be used in Microservices?

Common examples:

- External REST API integrations
- Payment providers
- Legacy SOAP integrations
- Authentication providers
- SMS providers
- Email providers
- Cloud storage providers
- External message brokers
- Third-party vendor APIs

---

# Key Points

- Category: **Structural Design Pattern**
- Adapter connects incompatible interfaces.
- Adapter acts as a translator.
- Client depends on the Target interface.
- Existing incompatible class is the Adaptee.
- Adapter implements the Target interface.
- Adapter delegates calls to the Adaptee.
- Object Adapter uses composition.
- Class Adapter uses inheritance.
- Prefer Object Adapter in Java.
- Commonly used for third-party integrations.
- Commonly used for legacy system integration.
- Supports Open/Closed Principle.
- Supports Dependency Inversion Principle.
- Adapter can transform requests, responses, exceptions, and data.

---

# Easy Trick to Remember

Think of a **Travel Power Adapter**.
```text
Indian Plug
     │
     ▼
Adapter
     │
     ▼
European Socket
```

Neither side needs to change.

The Adapter makes them compatible.

> **Adapter = Translator between two incompatible interfaces.**

Another easy way to remember:

> **"I cannot change either side, so I put a translator in between."**

---

# Summary

| Aspect            | Description                                |
|-------------------|--------------------------------------------|
| Pattern Type      | Structural                                 |
| Purpose           | Make incompatible interfaces work together |
| Main Components   | Client, Target, Adapter, Adaptee           |
| Main Technique    | Interface Conversion                       |
| Object Adapter    | Uses Composition                           |
| Class Adapter     | Uses Inheritance                           |
| Preferred in Java | Object Adapter                             |
| Common Use        | Third-party and Legacy Integration         |
| SOLID Principles  | SRP, OCP, DIP                              |
| Easy Trick        | Adapter = Translator                       |