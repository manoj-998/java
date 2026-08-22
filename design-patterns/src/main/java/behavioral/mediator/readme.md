# Mediator Design Pattern

## Definition
The **Mediator Design Pattern** is a **Behavioral Design Pattern** that reduces direct communication between multiple objects by making them communicate through a **central Mediator object**.

In simple words, instead of objects talking directly to each other, they send messages to the **Mediator**, and the Mediator decides which objects should receive or react to them.

> **Mediator = Central object that manages communication between other objects.**

---

# Why Mediator?
Suppose we have an Order Processing System:

```text
OrderService
PaymentService
InventoryService
NotificationService
```

Without Mediator, these services may directly communicate with each other:

```text
OrderService ------> PaymentService
     |                    |
     v                    v
InventoryService --> NotificationService
```

Each service starts knowing about many other services.

This creates:

- Tight coupling
- Complex dependencies
- Difficult testing
- Difficult maintenance
- Changes in one service may affect others

Mediator centralizes this communication.

```text
             OrderMediator
            /      |       \
           /       |        \
          v        v         v
       Order    Payment   Inventory
                           |
                           v
                     Notification
```

---

# Problem Without Mediator

```java
class OrderService {
    PaymentService payment;
    InventoryService inventory;
    NotificationService notification;

    void placeOrder() {
        inventory.reserve();
        payment.pay();
        notification.send();
    }
}
```

Now `OrderService` directly depends on:

```text
PaymentService
InventoryService
NotificationService
```

If more services are added:

```text
ShippingService
AuditService
RewardService
```

`OrderService` becomes responsible for coordinating everything.

---

# Solution
Introduce a Mediator:

```text
OrderService
     |
     v
OrderMediator
     |
     ├── InventoryService
     ├── PaymentService
     └── NotificationService
```

Services communicate through:

```text
OrderMediator
```

instead of directly knowing about each other.

---

# Main Components

## 1. Mediator
Defines how components communicate.

```java
interface OrderMediator {
    void notify(Component sender, String event);
}
```

## 2. Concrete Mediator
Contains the communication/coordination logic.

```text
OrderMediatorImpl
```

## 3. Colleague / Component
Objects that communicate through the Mediator.

Examples:

```text
OrderService
PaymentService
InventoryService
NotificationService
```

## 4. Client
Creates and connects the Mediator and components.

---

# Structure

```text
              Mediator
                 |
        ┌────────┼────────┐
        |        |        |
        v        v        v
   ComponentA ComponentB ComponentC
```

Instead of:

```text
ComponentA <----> ComponentB
     ^                 ^
     |                 |
     +---- ComponentC--+
```

we have:

```text
ComponentA ----\
ComponentB ----- > Mediator
ComponentC ----/
```

---

# Simple Java Example

```java
public class MediatorExample {

    /**
     * MEDIATOR
     * Defines communication between components.
     */
    interface Mediator {
        void notify(Component sender, String event);
    }

    /**
     * COMPONENT
     * Base class for objects communicating through Mediator.
     */
    abstract static class Component {
        protected Mediator mediator;

        Component(Mediator mediator) {
            this.mediator = mediator;
        }
    }

    /**
     * COLLEAGUE
     * Handles payment.
     */
    static class PaymentService extends Component {
        PaymentService(Mediator mediator) {
            super(mediator);
        }

        void pay() {
            System.out.println("Payment completed");
            mediator.notify(this, "PAYMENT_COMPLETED");
        }
    }

    /**
     * COLLEAGUE
     * Handles inventory.
     */
    static class InventoryService extends Component {
        InventoryService(Mediator mediator) {
            super(mediator);
        }

        void reserve() {
            System.out.println("Inventory reserved");
        }
    }

    /**
     * COLLEAGUE
     * Handles notifications.
     */
    static class NotificationService extends Component {
        NotificationService(Mediator mediator) {
            super(mediator);
        }

        void send() {
            System.out.println("Order notification sent");
        }
    }

    /**
     * CONCRETE MEDIATOR
     * Decides what should happen when an event occurs.
     */
    static class OrderMediator implements Mediator {
        private InventoryService inventory;
        private NotificationService notification;

        void setInventory(InventoryService inventory) {
            this.inventory = inventory;
        }

        void setNotification(NotificationService notification) {
            this.notification = notification;
        }

        @Override
        public void notify(Component sender, String event) {
            if ("PAYMENT_COMPLETED".equals(event)) {
                inventory.reserve();
                notification.send();
            }
        }
    }

    /**
     * CLIENT
     * Creates and connects all components.
     */
    public static void main(String[] args) {
        OrderMediator mediator = new OrderMediator();

        PaymentService payment = new PaymentService(mediator);
        InventoryService inventory = new InventoryService(mediator);
        NotificationService notification = new NotificationService(mediator);

        mediator.setInventory(inventory);
        mediator.setNotification(notification);

        payment.pay();
    }
}
```

Output:

```text
Payment completed
Inventory reserved
Order notification sent
```

---

# Internal Flow
Client starts:

```java
payment.pay();
```

Then:

```text
PaymentService
      |
      | PAYMENT_COMPLETED
      v
OrderMediator
      |
      ├──> InventoryService.reserve()
      |
      └──> NotificationService.send()
```

Notice:

```text
PaymentService
```

does **not** directly know:

```text
InventoryService
NotificationService
```

It only knows:

```text
Mediator
```

---

# Without Mediator vs With Mediator

## Without Mediator

```text
PaymentService
  |
  ├── InventoryService
  ├── NotificationService
  └── AuditService

InventoryService
  |
  └── NotificationService
```

Many direct dependencies.

## With Mediator

```text
PaymentService -------\
InventoryService ------> OrderMediator
NotificationService --/
AuditService ---------/
```

Components depend mainly on the Mediator.

---

# Real-World Software Examples

## Chat Room
A classic Mediator example.

Without Mediator:

```text
User1 → User2
User1 → User3
User2 → User3
```

With Mediator:

```text
User1 ----\
User2 ----- > ChatRoom
User3 ----/
```

A user sends:

```java
chatRoom.sendMessage(message, user);
```

The ChatRoom decides who receives it.

---

## UI Components
Suppose a form contains:

```text
Country Dropdown
State Dropdown
Submit Button
Text Fields
```

Selecting a country may affect other UI components.

Instead of:

```text
CountryDropdown → StateDropdown
CountryDropdown → SubmitButton
CountryDropdown → TextField
```

use:

```text
CountryDropdown
       |
       v
FormMediator
       |
       ├── StateDropdown
       ├── SubmitButton
       └── TextField
```

---

## Workflow Coordination

```text
PaymentService ----\
InventoryService ---\
ShippingService ----- > OrderMediator
EmailService -------/
AuditService -------/
```

The Mediator coordinates the workflow.

---

# Advantages

- Reduces direct dependencies between objects.
- Provides loose coupling.
- Centralizes communication logic.
- Components become easier to reuse.
- Components become easier to test independently.
- Complex object communication becomes easier to understand.
- Supports Single Responsibility by moving coordination logic out of components.

---

# Disadvantages

- Mediator can become too large.
- Too much coordination logic may create a **God Object**.
- Mediator can become difficult to maintain in very complex systems.
- All communication going through one object may create too much responsibility.

---

# When to Use
Use Mediator when:

- Many objects communicate with each other.
- Objects have too many direct dependencies.
- Communication logic is becoming complex.
- You want to centralize coordination.
- Components should be reusable independently.

Examples:

```text
Chat System
UI Form Components
Order Workflow
Booking Workflow
Workflow Engines
Dialog Boxes
```

---

# When Not to Use
Avoid Mediator when:

- Only two objects communicate.
- Communication is already simple.
- Introducing a Mediator adds unnecessary complexity.
- The Mediator would simply forward every call without providing coordination.

---

# Design Considerations
Components should know:

```text
Mediator
```

instead of knowing every other component.

Prefer:

```text
PaymentService
     |
     v
Mediator
     |
     ├── InventoryService
     └── NotificationService
```

instead of:

```text
PaymentService
     |
     ├── InventoryService
     └── NotificationService
```

Keep the Mediator focused on:

```text
Communication
Coordination
Workflow
```

Keep actual business logic inside the components.

---

# Mediator vs Facade
These patterns may look similar because both can sit between multiple objects.

```text
Mediator → Manages communication BETWEEN components
Facade   → Provides simple access TO a subsystem
```

Facade:

```text
Client
  |
  v
Facade
  |
  ├── Service A
  └── Service B
```

Mediator:

```text
Service A ----\
Service B ----- > Mediator
Service C ----/
```

Easy trick:

```text
Facade   = Simplify access
Mediator = Manage communication
```

---

# Mediator vs Observer

```text
Mediator → Central object coordinates communication
Observer → One object broadcasts changes to subscribers
```

Observer:

```text
Publisher
   |
   ├── Observer1
   ├── Observer2
   └── Observer3
```

Mediator:

```text
Component1 ----\
Component2 ----- > Mediator
Component3 ----/
```

---

# Mediator vs Chain of Responsibility

```text
Mediator → Coordinates communication
CoR      → Passes request through handlers
```

CoR:

```text
Request → Handler1 → Handler2 → Handler3
```

Mediator:

```text
Component1
    \
     Mediator
    /
Component2
```

---

# Pitfalls

- Do not put all business logic inside the Mediator.
- Avoid creating one Mediator for the entire application.
- Split large Mediators by business responsibility.
- Components should remain responsible for their own business logic.
- Mediator should mainly coordinate communication.
- Avoid turning the Mediator into a God Object.

---

# Interview Questions

## What is Mediator Pattern?
Mediator is a Behavioral Design Pattern that centralizes communication between multiple objects to reduce direct dependencies.

## Which category does Mediator belong to?
**Behavioral Design Pattern**

## What are the main components?
- Mediator
- Concrete Mediator
- Colleague/Component
- Client

## What is a Colleague?
A Colleague is an object that communicates with other objects through the Mediator.

Example:

```text
PaymentService
InventoryService
NotificationService
```

## What is the main benefit?
It reduces many-to-many dependencies between components.

## Does Mediator contain business logic?
Normally, it should contain **coordination logic**, while actual business logic remains inside individual components.

## Give a common example.
A **Chat Room**:

```text
Users → ChatRoom → Users
```

## Mediator vs Facade?

```text
Mediator → Communication between components
Facade   → Simple interface for client
```

## Mediator vs Observer?

```text
Mediator → Coordinates objects
Observer → Broadcasts events to subscribers
```

## What is the main disadvantage?
The Mediator itself can become a **God Object** if too much logic is placed inside it.

---

# Key Points

- Category: **Behavioral Design Pattern**
- Centralizes communication between objects.
- Reduces direct object-to-object dependencies.
- Components communicate through the Mediator.
- Mediator contains coordination logic.
- Components contain actual business logic.
- Useful when many objects communicate with each other.
- Chat Room is the classic example.
- Main risk is creating a God Object.

---

# Easy Trick to Remember
Think about an **Air Traffic Controller**.

Planes don't directly coordinate with every other plane:

```text
Plane A ----\
Plane B ----- > Air Traffic Controller
Plane C ----/
```

The controller manages communication.

In software:

```text
Component A ----\
Component B ----- > Mediator
Component C ----/
```

> **Mediator = Central coordinator between multiple objects.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Centralize communication |
| Mediator | Defines communication |
| Concrete Mediator | Coordinates components |
| Colleague | Communicates through Mediator |
| Main Benefit | Loose coupling |
| Common Example | Chat Room / Workflow |
| Main Risk | God Object |
| Facade Difference | Facade simplifies access |
| Easy Trick | Mediator = Central Coordinator |