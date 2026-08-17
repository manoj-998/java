
# Interface Segregation Principle (ISP)

## Definition

The **Interface Segregation Principle (ISP)** is the **fourth principle** of SOLID.

> **"Clients should not be forced to depend on interfaces they do not use."**
>
> — Robert C. Martin

Simply put:

> **Create small, specific interfaces instead of one large interface.**

A class should only implement the methods it actually needs.

---

# Why ISP?

Suppose you have a large interface with many methods.

Not every class may need all of them.

If a class is forced to implement unnecessary methods, it violates ISP.

---

# Bad Example (Violates ISP)

```java
interface Worker {

    void work();

    void eat();

    void sleep();
}
```

Human worker:

```java
class HumanWorker implements Worker {

    @Override
    public void work() {
        System.out.println("Working");
    }

    @Override
    public void eat() {
        System.out.println("Eating");
    }

    @Override
    public void sleep() {
        System.out.println("Sleeping");
    }
}
```

Robot worker:

```java
class RobotWorker implements Worker {

    @Override
    public void work() {
        System.out.println("Working");
    }

    @Override
    public void eat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sleep() {
        throw new UnsupportedOperationException();
    }
}
```

### Problem

A robot doesn't eat or sleep.

It is forced to implement methods it doesn't need.

This **violates ISP**.

---

# Good Example (Follows ISP)

Split the large interface into smaller ones.

## Workable

```java
interface Workable {
    void work();
}
```

## Eatable

```java
interface Eatable {
    void eat();
}
```

## Sleepable

```java
interface Sleepable {
    void sleep();
}
```

---

## Human Worker

```java
class HumanWorker implements Workable, Eatable, Sleepable {

    @Override
    public void work() {
        System.out.println("Working");
    }

    @Override
    public void eat() {
        System.out.println("Eating");
    }

    @Override
    public void sleep() {
        System.out.println("Sleeping");
    }
}
```

---

## Robot Worker

```java
class RobotWorker implements Workable {

    @Override
    public void work() {
        System.out.println("Working");
    }
}
```

Now each class implements only the methods it actually needs.

---

# Spring Boot Example

Suppose we have a notification system.

## Bad Design

```java
interface NotificationService {

    void sendEmail();

    void sendSMS();

    void sendPushNotification();
}
```

Email service:

```java
@Service
class EmailService implements NotificationService {

    @Override
    public void sendEmail() {
        System.out.println("Email Sent");
    }

    @Override
    public void sendSMS() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendPushNotification() {
        throw new UnsupportedOperationException();
    }
}
```

This violates ISP.

---

## Good Design

```java
interface EmailNotification {
    void sendEmail();
}
```

```java
interface SmsNotification {
    void sendSMS();
}
```

```java
interface PushNotification {
    void sendPushNotification();
}
```

Email implementation:

```java
@Service
class EmailService implements EmailNotification {

    @Override
    public void sendEmail() {
        System.out.println("Email Sent");
    }
}
```

SMS implementation:

```java
@Service
class SmsService implements SmsNotification {

    @Override
    public void sendSMS() {
        System.out.println("SMS Sent");
    }
}
```

Each service implements only the interface it needs.

---

# Benefits

- Smaller interfaces
- Cleaner code
- Easier maintenance
- Better readability
- Less coupling
- More flexible prototype
- Easier unit testing

---

# Signs That ISP is Violated

- Large interfaces with many methods
- Classes implementing methods they don't use
- Methods throwing `UnsupportedOperationException`
- Empty method implementations

---

# Difference Between OCP and ISP

| OCP | ISP |
|------|-----|
| Extend behavior without modifying existing code | Split large interfaces into smaller ones |
| Focuses on extensibility | Focuses on interface prototype |
| Uses inheritance and polymorphism | Uses small, focused interfaces |

---

# Interview Answer

## What is the Interface Segregation Principle?

The Interface Segregation Principle states that **clients should not be forced to depend on methods they do not use**. Instead of creating one large interface, we should create multiple small, specific interfaces so that implementing classes only implement the methods they actually need.

---

# Key Points

- Prefer many small interfaces over one large interface.
- A class should implement only the methods it requires.
- Avoid `UnsupportedOperationException` and empty method implementations.
- ISP leads to low coupling and high cohesion.

---

# Summary

| Bad Design | Good Design |
|------------|-------------|
| One large interface | Multiple small interfaces |
| Unused methods | Only required methods |
| `UnsupportedOperationException` | Clean implementations |
| High coupling | Low coupling |
| Difficult to maintain | Easy to maintain |
````
