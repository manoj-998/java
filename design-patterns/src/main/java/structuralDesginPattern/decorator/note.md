# Decorator Design Pattern

## Definition
The **Decorator Design Pattern** is a **Structural Design Pattern** that allows us to **add new behavior or functionality to an object dynamically without modifying its original class**.

In simple words, instead of changing the existing class or creating many subclasses, we **wrap the original object with another object (Decorator)** that adds extra functionality.

```text
Original Object
      |
      v
   Decorator
      |
      v
Extra Behavior
```

> **Decorator = Wrap an object to add extra behavior.**

---

# Why Decorator?
Suppose we have a notification service:

```text
Basic Notification
```

Later we want:

```text
Email Notification
SMS Notification
Slack Notification
```

And combinations:

```text
Email + SMS
Email + Slack
Email + SMS + Slack
```

Using inheritance can create many classes:

```text
EmailNotification
SMSNotification
EmailSMSNotification
EmailSlackNotification
EmailSMSSlackNotification
```

This causes **class explosion**.

Decorator solves this by wrapping objects.

```text
BasicNotification
      |
EmailDecorator
      |
SMSDecorator
```

---

# Problem Without Decorator

```java
class Notification {
    void send() {
        System.out.println("Basic notification");
    }
}

class EmailNotification extends Notification {
    void send() {
        super.send();
        System.out.println("Email notification");
    }
}

class EmailSMSNotification extends Notification {
    void send() {
        super.send();
        System.out.println("Email notification");
        System.out.println("SMS notification");
    }
}
```

Problems:

- Too many subclasses
- Difficult to support combinations
- Duplicate code
- Adding new behavior creates more classes
- Inheritance becomes difficult to maintain

---

# Solution
Create a common interface:

```java
interface Notification {
    void send();
}
```

Create the basic implementation:

```java
class BasicNotification implements Notification {
    public void send() {
        System.out.println("Basic Notification");
    }
}
```

Then create decorators that wrap `Notification`.

```text
Notification
     |
     v
BasicNotification
     |
     v
EmailDecorator
     |
     v
SMSDecorator
```

Each Decorator adds its own behavior.

---

# Main Components

## Component
Defines the common interface.

```java
interface Notification {
    void send();
}
```

## Concrete Component
The original object that provides basic functionality.

```java
class BasicNotification implements Notification {
    public void send() {
        System.out.println("Basic Notification");
    }
}
```

## Decorator
Wraps another `Notification`.

```java
class NotificationDecorator implements Notification {
    protected Notification notification;

    NotificationDecorator(Notification notification) {
        this.notification = notification;
    }

    public void send() {
        notification.send();
    }
}
```

## Concrete Decorator
Adds additional behavior.

Examples:

```text
EmailDecorator
SMSDecorator
```

---

# Simple Java Example

## Component

```java
interface Notification {
    void send();
}
```

## Concrete Component

```java
class BasicNotification implements Notification {
    public void send() {
        System.out.println("Basic Notification");
    }
}
```

## Email Decorator

```java
class EmailDecorator implements Notification {
    private Notification notification;

    EmailDecorator(Notification notification) {
        this.notification = notification;
    }

    public void send() {
        notification.send();
        System.out.println("Email Notification");
    }
}
```

## SMS Decorator

```java
class SMSDecorator implements Notification {
    private Notification notification;

    SMSDecorator(Notification notification) {
        this.notification = notification;
    }

    public void send() {
        notification.send();
        System.out.println("SMS Notification");
    }
}
```

## Usage

```java
public class Main {
    public static void main(String[] args) {
        Notification notification = new BasicNotification();

        notification = new EmailDecorator(notification);
        notification = new SMSDecorator(notification);

        notification.send();
    }
}
```

Output:

```text
Basic Notification
Email Notification
SMS Notification
```

---

# Internal Flow

```text
SMSDecorator
     |
     v
EmailDecorator
     |
     v
BasicNotification
```

When:

```java
notification.send();
```

is called:

```text
SMSDecorator.send()
      |
      v
EmailDecorator.send()
      |
      v
BasicNotification.send()
```

Each Decorator adds its own behavior.

---

# Adding Another Decorator
Suppose we want Slack notifications.

```java
class SlackDecorator implements Notification {
    private Notification notification;

    SlackDecorator(Notification notification) {
        this.notification = notification;
    }

    public void send() {
        notification.send();
        System.out.println("Slack Notification");
    }
}
```

Now we can combine:

```java
Notification notification = new BasicNotification();
notification = new EmailDecorator(notification);
notification = new SMSDecorator(notification);
notification = new SlackDecorator(notification);

notification.send();
```

No existing class needs modification.

---

# Real-World Software Example

A very common example is Java I/O.

```java
InputStream input =
    new BufferedInputStream(
        new FileInputStream("file.txt")
    );
```

Here:

```text
FileInputStream
      |
      v
BufferedInputStream
```

`BufferedInputStream` wraps `FileInputStream` and adds buffering behavior.

This is a real example of the **Decorator Pattern** in Java.

---

# Another Example: HTTP Request Processing
Suppose an API request can have:

```text
Basic Request
    |
Authentication
    |
Logging
    |
Metrics
```

Conceptually:

```text
RequestHandler
      |
      v
AuthenticationDecorator
      |
      v
LoggingDecorator
      |
      v
ActualHandler
```

Each layer adds additional behavior.

---

# Advantages

- Adds behavior dynamically.
- Avoids creating too many subclasses.
- Uses composition instead of excessive inheritance.
- Multiple decorators can be combined.
- Existing classes do not need modification.
- Supports Open/Closed Principle.
- Each decorator can have one responsibility.

---

# Disadvantages

- Creates multiple small objects.
- Too many decorators can make debugging difficult.
- Execution flow can become harder to understand.
- Order of decorators may affect behavior.

Example:

```text
Logging → Authentication
```

may behave differently from:

```text
Authentication → Logging
```

---

# When to Use
Use Decorator when:

- You want to add functionality dynamically.
- You don't want to modify the original class.
- Different combinations of behaviors are required.
- Inheritance would create too many subclasses.
- Features should be added or removed at runtime.

Examples:

```text
Notification + Email + SMS
HTTP Handler + Logging + Authentication
FileStream + Buffering + Compression
Coffee + Milk + Sugar
```

---

# When Not to Use
Avoid Decorator when:

- Only one fixed behavior is required.
- There are no combinations of features.
- Wrapping objects makes the design unnecessarily complex.
- Decorator order would make the system difficult to understand.

---

# Decorator vs Inheritance

Inheritance adds behavior at the **class level**.

```text
Parent
  |
  v
Child
```

Decorator adds behavior at the **object level**.

```text
Object
  |
  v
Decorator
```

Remember:

```text
Inheritance → Static behavior
Decorator   → Dynamic behavior
```

---

# Decorator vs Adapter

```text
Decorator → Adds behavior
Adapter   → Changes interface
```

Adapter makes incompatible interfaces work together.

Decorator keeps the same interface and adds functionality.

---

# Decorator vs Proxy
Both wrap another object, but their purpose is different.

```text
Decorator → Add behavior
Proxy     → Control access
```

Example:

```text
Decorator → Add logging/compression
Proxy     → Security/caching/lazy loading
```

---

# Decorator vs Bridge

```text
Decorator → Add behavior dynamically
Bridge    → Separate abstraction from implementation
```

---

# Design Considerations
Decorator and the original object should implement the **same interface**.

```text
             Notification
             /          \
            /            \
BasicNotification    EmailDecorator
                           |
                           v
                     Notification
```

Because they share the same interface, decorators can wrap:

```text
Original Object
```

or even:

```text
Another Decorator
```

This allows chaining.

---

# Pitfalls

- Do not put too many responsibilities in one Decorator.
- Too many nested decorators can become difficult to debug.
- Decorator order can matter.
- Keep the same interface between Component and Decorator.
- Avoid Decorator when simple inheritance is enough.

---

# Interview Questions

## What is Decorator Pattern?
Decorator is a Structural Design Pattern that dynamically adds new behavior to an object without modifying its original class.

## Which category does Decorator belong to?
**Structural Design Pattern**

## What are the main components?
- Component
- Concrete Component
- Decorator
- Concrete Decorator

## Does Decorator use inheritance or composition?
Decorator mainly uses **composition** to wrap another object.

## Why use Decorator instead of inheritance?
Decorator allows behaviors to be combined dynamically without creating many subclasses.

## Can multiple Decorators be combined?
Yes.

```text
SMSDecorator
     |
EmailDecorator
     |
BasicNotification
```

## What is a real Java example?
Java I/O:

```java
new BufferedInputStream(
    new FileInputStream("file.txt")
);
```

## Decorator vs Adapter?

```text
Decorator → Adds behavior
Adapter   → Changes interface
```

## Decorator vs Proxy?

```text
Decorator → Adds functionality
Proxy     → Controls access
```

---

# Key Points

- Category: **Structural Design Pattern**
- Adds behavior dynamically.
- Wraps an existing object.
- Component and Decorator use the same interface.
- Multiple decorators can be chained.
- Uses composition.
- Avoids subclass explosion.
- Supports Open/Closed Principle.
- Java I/O is a common real-world example.

---

# Easy Trick to Remember
Think about decorating a **Christmas Tree**.

```text
Tree
 |
Lights
 |
Stars
 |
Ornaments
```

The original tree remains the same.

Each decoration adds something new.

In software:

```text
Original Object
      |
   Decorator
      |
   Decorator
      |
Extra Behavior
```

> **Decorator = Wrap the object and add extra behavior.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Add behavior dynamically |
| Main Technique | Wrapping + Composition |
| Component | Common interface |
| Concrete Component | Original object |
| Decorator | Wraps Component |
| Concrete Decorator | Adds new behavior |
| Main Problem Solved | Too many subclasses |
| Java Example | BufferedInputStream |
| Easy Trick | Decorator = Wrap + Add Behavior |