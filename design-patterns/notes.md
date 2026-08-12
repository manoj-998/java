
# Design Patterns

## What are Design Patterns?

Design Patterns are **proven, reusable solutions** to common software prototype problems.

They are **templates or best practices**, not ready-to-use code.

### Benefits

- Reusable solutions
- Better code organization
- Easier maintenance
- Improved scalability
- Promotes loose coupling
- Easier communication among developers

---

# Categories of Design Patterns

Design Patterns are divided into **three categories**:

1. **Creational Design Patterns** :- object creation
2. **Structural Design Patterns** :- how classes and objects are combined to form larger structures.
3. **Behavioral Design Patterns** :- communication and interaction between objects

---

# 1. Creational Design Patterns

## Definition

Creational Design Patterns deal with **object creation**.

They provide flexible ways to create objects while hiding the creation logic.

### Common Creational Patterns

| Pattern | Purpose |
|----------|---------|
| Singleton | Ensures only one instance of a class exists |
| Factory Method | Creates objects without exposing creation logic |
| Abstract Factory | Creates families of related objects |
| Builder | Builds complex objects step by step |
| Prototype | Creates objects by cloning existing ones |

### Example

Without Factory:

```java
Car car = new Car();
```

Using Factory:

```java
Vehicle vehicle = VehicleFactory.getVehicle("CAR");
```

### Real-World Example

Think of a **restaurant**.

You order a **Pizza**.

You don't know how it's prepared—you simply receive the finished product.

The kitchen acts like a **Factory**.

---

# 2. Structural Design Patterns

## Definition

Structural Design Patterns deal with **how classes and objects are combined** to form larger structures.

They help make systems flexible and easy to maintain.

### Common Structural Patterns

| Pattern | Purpose |
|----------|---------|
| Adapter | Makes incompatible interfaces work together |
| Bridge | Separates abstraction from implementation |
| Composite | Treats individual objects and groups uniformly |
| Decorator | Adds functionality without modifying the original object |
| Facade | Provides a simplified interface to a complex system |
| Flyweight | Reduces memory usage by sharing objects |
| Proxy | Controls access to another object |

### Example

```java
Coffee coffee = new SimpleCoffee();

coffee = new MilkDecorator(coffee);

coffee = new SugarDecorator(coffee);
```

Instead of modifying `SimpleCoffee`, decorators add extra functionality.

### Real-World Example

A **phone charger adapter**.

Your laptop charger may not fit the wall socket.

An adapter allows them to work together without changing either one.

---

# 3. Behavioral Design Patterns

## Definition

Behavioral Design Patterns deal with **communication and interaction between objects**.

They define how objects collaborate and share responsibilities.

### Common Behavioral Patterns

| Pattern | Purpose |
|----------|---------|
| Strategy | Select an algorithm at runtime |
| Observer | Notify multiple objects when state changes |
| Command | Encapsulate a request as an object |
| State | Change behavior based on current state |
| Iterator | Traverse collections |
| Mediator | Centralize communication |
| Template Method | Define algorithm skeleton |
| Chain of Responsibility | Pass requests through handlers |
| Visitor | Add operations without modifying classes |
| Memento | Save and restore object state |
| Interpreter | Interpret language grammar |

### Example

```java
PaymentStrategy payment = new CreditCardPayment();

payment.pay(1000);
```

Later,

```java
payment = new UpiPayment();

payment.pay(1000);
```

The client code remains unchanged while the payment algorithm changes.

### Real-World Example

Google Maps.

Depending on your choice, it can calculate:

- Car route
- Bike route
- Walking route

Different algorithms are selected at runtime.

---

# Observer Design Pattern

## Definition

The **Observer Pattern** is a **Behavioral Design Pattern**.

It defines a **one-to-many relationship** between objects.

When one object changes its state, all dependent objects are automatically notified.

---

## Participants

### Subject

The object being observed.

Responsibilities:

- Register observers
- Remove observers
- Notify observers

---

### Observer

Objects that receive updates from the subject.

---

## Real-World Example

YouTube

- You subscribe to a channel.
- The channel uploads a new video.
- Every subscriber receives a notification.

Here:

- Channel → Subject
- Subscribers → Observers

---

## Example

### Observer Interface

```java
public interface Observer {

    void update(String message);
}
```

---

### Observer Implementation

```java
public class EmailSubscriber implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Email : " + message);
    }
}
```

---

### Subject

```java
public class YouTubeChannel {

    private List<Observer> observers = new ArrayList<>();

    public void subscribe(Observer observer) {
        observers.add(observer);
    }

    public void notifySubscribers(String message) {

        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void uploadVideo() {

        notifySubscribers("New video uploaded!");
    }
}
```

---

### Client

```java
public class Main {

    public static void main(String[] args) {

        YouTubeChannel channel = new YouTubeChannel();

        channel.subscribe(new EmailSubscriber());

        channel.uploadVideo();
    }
}
```

Output

```
Email : New video uploaded!
```

---

# Advantages

## Creational

- Encapsulates object creation
- Reduces coupling
- Easier maintenance

---

## Structural

- Simplifies object relationships
- Promotes flexibility
- Improves code reuse

---

## Behavioral

- Improves communication between objects
- Promotes loose coupling
- Easier to extend

---

# Interview Questions

## What are Design Patterns?

Design Patterns are reusable solutions to commonly occurring software prototype problems. They provide best practices for designing flexible, maintainable, and scalable applications.

---

## What are the three categories of Design Patterns?

1. Creational
2. Structural
3. Behavioral

---

## Which category does the Observer Pattern belong to?

**Behavioral Design Pattern**

---

# Summary

| Category | Focus | Examples |
|----------|-------|----------|
| **Creational** | Object creation | Singleton, Factory, Builder, Prototype |
| **Structural** | Class and object composition | Adapter, Decorator, Facade, Proxy |
| **Behavioral** | Object communication | Strategy, Observer, Command, State |

---

# Easy Trick to Remember

- **Creational** → **Create Objects**
- **Structural** → **Organize Objects**
- **Behavioral** → **Objects Communicate**
````
