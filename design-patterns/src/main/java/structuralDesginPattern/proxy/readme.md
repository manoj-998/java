# Proxy Design Pattern

## Definition
The **Proxy Design Pattern** is a **Structural Design Pattern** that provides a **substitute or placeholder for another object to control access to it**.

In simple words, instead of the client directly accessing the real object, the client talks to a **Proxy**. The Proxy can perform extra checks such as **authentication, logging, caching, lazy loading, or access control** before calling the real object.

> **Proxy = Control access to the real object.**

---


# Why Proxy?

Proxy Pattern acts as a middleman between the client and the real object.
It controls access and can add security, caching, logging, or lazy loading.

Proxy = Client → Proxy → Real Object

Real Object → WHAT business operation should happen
Proxy       → WHEN / WHETHER / HOW the client can access it

Suppose we have a `PaymentService`:

```text
Client → PaymentService
```

Before processing payment, we may want to:

- Check authorization
- Log the request
- Check cache
- Delay object creation
- Control remote access

Instead of adding all this logic to `PaymentService`, introduce a Proxy:

```text
Client
  |
  v
PaymentServiceProxy
  |
  | Check Access
  v
RealPaymentService
```

---

# Problem Without Proxy

```java
class PaymentService {
    void pay(double amount) {
        System.out.println("Payment: " + amount);
    }
}
```

Client directly accesses it:

```java
PaymentService service = new PaymentService();
service.pay(1000);
```

If authorization is required:

```java
if (user.isAuthorized()) {
    service.pay(1000);
}
```

Problems:

- Client handles security logic.
- Same checks may be duplicated.
- Client is tightly coupled to the real object.
- Logging/caching/security logic can spread across the application.

---

# Solution
Create a common interface:

```java
interface PaymentService {
    void pay(double amount);
}
```

Real service:

```java
class RealPaymentService implements PaymentService {
    public void pay(double amount) {
        System.out.println("Payment: " + amount);
    }
}
```

Proxy:

```java
class PaymentProxy implements PaymentService {
    private PaymentService service = new RealPaymentService();

    public void pay(double amount) {
        System.out.println("Checking access...");
        service.pay(amount);
    }
}
```

Client:

```java
PaymentService service = new PaymentProxy();
service.pay(1000);
```

The client doesn't directly access `RealPaymentService`.

---

# Main Components

## 1. Subject
Common interface used by both Proxy and Real Subject.

```java
interface PaymentService {
    void pay(double amount);
}
```

## 2. Real Subject
The actual object that performs the real operation.

```text
RealPaymentService
```

## 3. Proxy
Controls access to the Real Subject.

```text
PaymentServiceProxy
```

## 4. Client
Uses the Subject interface.

```text
Client
```

Structure:

```text
             Subject
          PaymentService
             /     \
            /       \
         Proxy    Real Subject
           |           ^
           |           |
           +-----------+
```

Client flow:

```text
Client
  |
  v
Proxy
  |
  | Access Check
  v
Real Object
```

---

# Simple Java Example

```java
public class ProxyExample {

    /**
     * SUBJECT
     * Common interface for Proxy and Real Subject.
     */
    interface PaymentService {
        void pay(double amount);
    }

    /**
     * REAL SUBJECT
     * Performs the actual payment.
     */
    static class RealPaymentService implements PaymentService {
        public void pay(double amount) {
            System.out.println("Payment completed: " + amount);
        }
    }

    /**
     * PROXY
     * Controls access to RealPaymentService.
     */
    static class PaymentProxy implements PaymentService {
        private final PaymentService service = new RealPaymentService();
        private final boolean authorized;

        PaymentProxy(boolean authorized) {
            this.authorized = authorized;
        }

        public void pay(double amount) {
            if (!authorized) {
                System.out.println("Access denied");
                return;
            }

            System.out.println("Access granted");
            service.pay(amount);
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        PaymentService service = new PaymentProxy(true);
        service.pay(1000);
    }
}
```

Output:

```text
Access granted
Payment completed: 1000.0
```

---

# Internal Flow

When client calls:

```java
service.pay(1000);
```

Flow:

```text
Client
  |
  v
PaymentProxy.pay()
  |
  v
Authorized?
 /       \
No       Yes
|         |
Deny      v
      RealPaymentService.pay()
             |
             v
      Payment Completed
```

The Proxy decides whether the Real Subject should be called.

---

# Types of Proxy

## 1. Protection Proxy
Controls access based on permissions or roles.

```text
Client
  |
  v
Security Proxy
  |
Check Permission
  |
  v
Real Service
```

Examples:

```text
Admin-only operation
Authorization
Role-based access
```

---

## 2. Virtual Proxy
Delays creation of an expensive object until it is actually needed.

Example:

```text
Image Proxy
    |
    | load only when required
    v
Large Image
```

```java
class ImageProxy {
    private RealImage image;

    void display() {
        if (image == null) {
            image = new RealImage();
        }

        image.display();
    }
}
```

This is **Lazy Loading**.

---

## 3. Remote Proxy
Represents an object located on another machine/service.

```text
Client
  |
  v
Remote Proxy
  |
  v
Network
  |
  v
Remote Service
```

Examples:

```text
REST Client
gRPC Client
RMI
Remote Microservice
```

The client works with a local proxy while the actual operation happens remotely.

---

## 4. Caching Proxy
Stores results to avoid expensive repeated operations.

```text
Client
  |
  v
Cache Proxy
  |
Cache Hit?
 /      \
Yes      No
 |        |
Return    Real Service
          |
          v
        Cache
```

Example:

```java
if (cache.containsKey(id)) {
    return cache.get(id);
}

User user = service.getUser(id);
cache.put(id, user);

return user;
```

---

## 5. Logging Proxy
Adds logging around calls to the real object.

```text
Client
  |
  v
Logging Proxy
  |
Log Request
  |
  v
Real Service
  |
Log Response
```

---

# Real-World Software Examples

## Spring AOP
Spring can create proxy objects around beans for:

```text
@Transactional
@Cacheable
@Async
@PreAuthorize
```

Conceptually:

```text
Controller
    |
    v
Spring Proxy
    |
    ├── Transaction
    ├── Security
    └── Logging
    |
    v
Actual Service
```

---

# Spring Transaction Proxy

Suppose:

```java
@Transactional
public void transfer() {
    // business logic
}
```

Conceptually Spring does:

```text
Client
  |
  v
Spring Proxy
  |
Start Transaction
  |
  v
transfer()
  |
Commit / Rollback
```

The actual service does not manually manage the transaction around every call.

---

# Hibernate Lazy Loading
Hibernate may use proxy objects for lazy-loaded entities.

```text
Order
  |
  v
Customer Proxy
  |
Access customer data
  |
  v
Load Customer from DB
```

The real data is loaded only when required.

---

# Microservice Example

Suppose:

```text
OrderService
```

needs to call:

```text
PaymentService
```

Conceptually:

```text
OrderService
     |
     v
Payment Client Proxy
     |
     v
Network
     |
     v
Payment Microservice
```

The proxy hides communication details from the client.

---

# Advantages

- Controls access to real objects.
- Supports lazy loading.
- Can add security checks.
- Can add caching.
- Can add logging.
- Can hide remote communication.
- Keeps extra concerns outside the real object.
- Client can use Proxy and Real Subject through the same interface.

---

# Disadvantages

- Adds another class/layer.
- Can make debugging harder.
- Proxy can increase request latency.
- Too much logic inside Proxy can make it complex.
- Multiple proxy layers can make execution flow difficult to understand.

---

# When to Use
Use Proxy when:

- Access control is required.
- Object creation is expensive.
- Lazy loading is required.
- Results should be cached.
- Logging is required around method calls.
- The real object exists remotely.
- You want additional behavior before/after accessing an object.

Examples:

```text
Security
Caching
Lazy Loading
Logging
Remote Calls
Transactions
```

---

# When Not to Use
Avoid Proxy when:

- Direct access is simple and safe.
- No access control or extra behavior is needed.
- The extra layer provides no benefit.
- It unnecessarily complicates the execution flow.

---

# Proxy vs Decorator
Both wrap another object and normally implement the same interface.

Their intent is different:

```text
Proxy     → Controls access
Decorator → Adds behavior
```

Example:

```text
Proxy     → Check permission before payment
Decorator → Add additional notification behavior
```

Easy trick:

```text
Proxy     = Control
Decorator = Enhance
```

---

# Proxy vs Adapter

```text
Proxy   → Controls access
Adapter → Changes interface
```

Proxy normally keeps the same interface.

Adapter converts one interface into another.

---

# Proxy vs Facade

```text
Proxy  → Controls access to an object
Facade → Simplifies access to a subsystem
```

Facade may hide many services:

```text
Facade
├── Service A
├── Service B
└── Service C
```

Proxy normally represents another object:

```text
Proxy → Real Object
```

---

# Design Considerations
Proxy and Real Subject normally implement the same interface:

```text
             PaymentService
              /         \
             /           \
     PaymentProxy   RealPaymentService
           |
           v
   RealPaymentService
```

This allows the client to depend only on:

```java
PaymentService
```

instead of knowing whether it received:

```text
Proxy
```

or:

```text
Real Object
```

---

# Pitfalls

- Don't put core business logic inside Proxy.
- Keep Proxy focused on access/control concerns.
- Be careful with multiple nested proxies.
- Remember that proxy calls may add latency.
- With Spring proxies, internal/self method calls may bypass proxy behavior in some cases.

---

# Interview Questions

## What is Proxy Pattern?
Proxy is a Structural Design Pattern that provides a substitute for another object and controls access to it.

## Which category does Proxy belong to?
**Structural Design Pattern**

## What are the main components?
- Subject
- Real Subject
- Proxy
- Client

## Why do Proxy and Real Subject implement the same interface?
So the client can use either one without changing its code.

## What are common Proxy types?
- Protection Proxy
- Virtual Proxy
- Remote Proxy
- Caching Proxy
- Logging Proxy

## What is Virtual Proxy?
A Virtual Proxy delays the creation/loading of an expensive object until it is actually required.

```text
Virtual Proxy = Lazy Loading
```

## What is Protection Proxy?
A Protection Proxy checks whether the client has permission to access the real object.

```text
Protection Proxy = Access Control
```

## What is Remote Proxy?
A Remote Proxy represents an object/service located remotely.

```text
Client → Proxy → Network → Remote Service
```

## Give examples of Proxy in Java/Spring.
Common examples:

```text
Spring AOP
@Transactional
@Cacheable
@PreAuthorize
Hibernate Lazy Loading
RMI
```

## Proxy vs Decorator?

```text
Proxy     → Control access
Decorator → Add behavior
```

## Proxy vs Adapter?

```text
Proxy   → Same interface, control access
Adapter → Different interface, convert it
```

## Proxy vs Facade?

```text
Proxy  → Controls access
Facade → Simplifies access
```

---

# Key Points

- Category: **Structural Design Pattern**
- Proxy represents another object.
- Proxy controls access to the real object.
- Proxy and Real Subject usually implement the same interface.
- Protection Proxy provides authorization.
- Virtual Proxy provides lazy loading.
- Remote Proxy hides remote communication.
- Caching Proxy avoids repeated expensive operations.
- Spring AOP heavily uses proxies.
- `@Transactional` commonly works through Spring proxies.

---

# Easy Trick to Remember
Think about a **Security Guard**.

You want to enter an office:

```text
You
 |
 v
Security Guard
 |
Check Access
 |
 v
Office
```

You don't directly enter the office.

The Security Guard decides whether you can access it.

In software:

```text
Client
  |
  v
Proxy
  |
Check / Control
  |
  v
Real Object
```

> **Proxy = Security Guard for the Real Object.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Control access to another object |
| Subject | Common interface |
| Real Subject | Performs actual work |
| Proxy | Controls access |
| Protection Proxy | Security/Authorization |
| Virtual Proxy | Lazy Loading |
| Remote Proxy | Remote communication |
| Caching Proxy | Cache results |
| Spring Example | `@Transactional`, `@Cacheable` |
| Easy Trick | Proxy = Security Guard |