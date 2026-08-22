# State Design Pattern

## Definition
The **State Design Pattern** is a **Behavioral Design Pattern** that allows an object to **change its behavior when its internal state changes**.

In simple words, instead of writing many `if/else` or `switch` conditions for different states, we create a **separate class for each state**. The Context delegates the operation to its current State object.

> **State = Same object behaves differently based on its current state.**

---

# Why State Pattern?
Consider an Order:

```text
CREATED
PAID
SHIPPED
DELIVERED
CANCELLED
```

The allowed behavior depends on the current state.

For example:

```text
CREATED   → Can Pay / Cancel
PAID      → Can Ship / Cancel
SHIPPED   → Can Deliver
DELIVERED → Cannot Cancel
```

Without State Pattern, we may write many conditions:

```java
if (status.equals("CREATED")) {
    // payment logic
} else if (status.equals("PAID")) {
    // shipping logic
} else if (status.equals("SHIPPED")) {
    // delivery logic
}
```

As states increase, this becomes difficult to maintain.

---

# Problem Without State Pattern

```java
class Order {
    String status;

    void process() {
        if ("CREATED".equals(status)) {
            System.out.println("Processing payment");
            status = "PAID";
        } else if ("PAID".equals(status)) {
            System.out.println("Shipping order");
            status = "SHIPPED";
        } else if ("SHIPPED".equals(status)) {
            System.out.println("Delivering order");
            status = "DELIVERED";
        }
    }
}
```

Problems:

- Too many `if/else` or `switch` statements.
- State-specific logic is mixed together.
- Difficult to add new states.
- Difficult to maintain state transitions.
- One class gets too many responsibilities.

---

# Solution
Create a separate class for each state.

```text
              OrderState
                  |
       ┌──────────┼───────────┐
       |          |           |
   Created      Paid       Shipped
    State       State       State
```

The `Order` stores its current State:

```text
Order (Context)
      |
      | HAS-A
      v
OrderState
```

When the state changes:

```text
CreatedState
     ↓
PaidState
     ↓
ShippedState
     ↓
DeliveredState
```

the behavior also changes.

---

# Main Components

## 1. State
Defines common operations for all states.

```java
interface OrderState {
    void next(Order order);
}
```

## 2. Concrete State
Implements behavior for a particular state.

Examples:

```text
CreatedState
PaidState
ShippedState
DeliveredState
```

## 3. Context
The main object whose behavior changes depending on its current state.

Example:

```text
Order
```

The Context stores:

```java
OrderState state;
```

and delegates operations to it.

## 4. Client
Creates and uses the Context.

The client usually doesn't need large state-based `if/else` blocks.

---

# Simple Java Example

```java
public class StatePatternExample {

    /**
     * STATE
     * Common contract for all order states.
     */
    interface OrderState {
        void next(Order order);
    }

    /**
     * CONTEXT
     * Stores the current state.
     */
    static class Order {
        private OrderState state = new CreatedState();

        void setState(OrderState state) {
            this.state = state;
        }

        void next() {
            state.next(this);
        }
    }

    /**
     * CONCRETE STATE
     * Order is created and needs payment.
     */
    static class CreatedState implements OrderState {
        public void next(Order order) {
            System.out.println("Payment completed");
            order.setState(new PaidState());
        }
    }

    /**
     * CONCRETE STATE
     * Paid order can now be shipped.
     */
    static class PaidState implements OrderState {
        public void next(Order order) {
            System.out.println("Order shipped");
            order.setState(new ShippedState());
        }
    }

    /**
     * CONCRETE STATE
     * Shipped order can now be delivered.
     */
    static class ShippedState implements OrderState {
        public void next(Order order) {
            System.out.println("Order delivered");
            order.setState(new DeliveredState());
        }
    }

    /**
     * CONCRETE STATE
     * Final state.
     */
    static class DeliveredState implements OrderState {
        public void next(Order order) {
            System.out.println("Order already delivered");
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        Order order = new Order();

        order.next();
        order.next();
        order.next();
        order.next();
    }
}
```

Output:

```text
Payment completed
Order shipped
Order delivered
Order already delivered
```

---

# What Happens Internally?

Initially:

```text
Order
  |
  v
CreatedState
```

First call:

```java
order.next();
```

actually executes:

```java
CreatedState.next(order);
```

It changes the state:

```text
CreatedState
     |
     v
PaidState
```

Second call:

```java
order.next();
```

now executes:

```java
PaidState.next(order);
```

Flow:

```text
Order.next()
    |
    v
Current State
    |
    v
state.next(order)
    |
    v
Perform Behavior
    |
    v
Change State
```

Complete transition:

```text
CREATED
   |
   | Pay
   v
PAID
   |
   | Ship
   v
SHIPPED
   |
   | Deliver
   v
DELIVERED
```

---

# What is Context?
In State Pattern, **Context is the main object whose behavior changes based on its current State**.

In our example:

```text
Order = Context
```

It contains:

```java
private OrderState state;
```

and delegates:

```java
void next() {
    state.next(this);
}
```

Think:

```text
Context = Holds Current State
State   = Provides Current Behavior
```

---

# Real-World Software Examples

## Order Lifecycle

```text
CREATED → PAID → SHIPPED → DELIVERED
```

## Payment

```text
INITIATED
   ↓
PROCESSING
   ↓
SUCCESS / FAILED
```

## Ticket

```text
OPEN
 ↓
IN_PROGRESS
 ↓
RESOLVED
 ↓
CLOSED
```

## Document Workflow

```text
DRAFT
 ↓
REVIEW
 ↓
APPROVED
 ↓
PUBLISHED
```

## Media Player

```text
PlayingState
PausedState
StoppedState
```

The same button may behave differently depending on the current state.

---

# Advantages

- Removes large state-based `if/else` blocks.
- Each state has its own behavior.
- State transitions become easier to understand.
- Supports Single Responsibility Principle.
- Easy to add new states.
- Keeps state-specific logic separated.
- Makes complex lifecycle logic cleaner.

---

# Disadvantages

- Creates additional classes.
- Can be overkill when there are only 2 simple states.
- State transitions can become complex.
- Many states can create many small classes.

---

# When to Use
Use State Pattern when:

- Object behavior depends heavily on its current state.
- There are many `if/else` checks based on status.
- There are clear state transitions.
- State-specific logic is becoming complex.
- Different states allow different operations.

Examples:

```text
Order Status
Payment Status
Ticket Status
Document Workflow
Media Player
Connection Status
```

---

# When Not to Use
Avoid State Pattern when:

- There are only a few simple states.
- State-specific behavior is very small.
- A simple enum or `if` condition is enough.
- States rarely change.

---

# Design Considerations
Prefer:

```text
Order
 |
 v
OrderState
 |
 ├── CreatedState
 ├── PaidState
 ├── ShippedState
 └── DeliveredState
```

instead of:

```java
if (status == CREATED) {
    ...
} else if (status == PAID) {
    ...
} else if (status == SHIPPED) {
    ...
}
```

The Context should delegate behavior:

```java
state.next(this);
```

State classes contain state-specific logic.

---

# State vs Strategy
State and Strategy look very similar because both use composition.

The difference is their **intent**.

```text
State    → Behavior changes because internal state changes
Strategy → Client chooses an algorithm/behavior
```

Example:

```text
State:
Order → CREATED → PAID → SHIPPED

Strategy:
PaymentService → UPI / Card / NetBanking
```

Easy trick:

```text
State    = Current condition decides behavior
Strategy = Selected algorithm decides behavior
```

---

# State vs Memento

```text
State   → Controls behavior based on current state
Memento → Saves old state so it can be restored
```

Example:

```text
State:
Order is currently SHIPPED

Memento:
Restore Order to previous saved data
```

---

# State vs Chain of Responsibility

```text
State → One current state handles behavior
CoR   → Request moves through multiple handlers
```

State:

```text
Context → Current State
```

CoR:

```text
Request → Handler1 → Handler2 → Handler3
```

---

# Pitfalls

- Don't use State Pattern for very simple status fields.
- Avoid putting all state logic back inside Context.
- Keep each State focused on its own behavior.
- Keep state transitions clear.
- Avoid circular or invalid state transitions.
- Use enums when you only need to store state and don't have state-specific behavior.

---

# Interview Questions

## What is State Pattern?
State is a Behavioral Design Pattern that allows an object to change its behavior when its internal state changes.

## Which category does State belong to?
**Behavioral Design Pattern**

## What are the main components?
- State
- Concrete State
- Context
- Client

## What is Context?
Context is the object whose behavior changes based on its current State.

Example:

```text
Order
```

## What is Concrete State?
A Concrete State contains behavior specific to one state.

Example:

```text
PaidState
ShippedState
```

## Why use State instead of if/else?
When state-specific conditions become large, State Pattern moves each state's behavior into a separate class.

## Who changes the State?
Depending on the design:

```text
State itself can change the Context state
```

or:

```text
Context can manage transitions
```

Both approaches are possible.

## State vs Strategy?

```text
State    → Changes automatically based on current state
Strategy → Usually selected by client/configuration
```

## Enum vs State Pattern?
Use an enum when you mainly need:

```text
CREATED
PAID
SHIPPED
```

Use State Pattern when each state has significant different behavior:

```text
CREATED → pay()
PAID    → ship()
SHIPPED → deliver()
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Behavior changes based on current state.
- Context stores the current State.
- State is a common interface.
- Concrete States implement state-specific behavior.
- Context delegates behavior to State.
- Useful for lifecycle/workflow systems.
- Reduces large `if/else` and `switch` blocks.
- State transitions should be clearly defined.
- Order lifecycle is an easy real-world example.

---

# Easy Trick to Remember
Think about an **Order**:

```text
CREATED → PAID → SHIPPED → DELIVERED
```

The same Order behaves differently at each stage.

```text
Order
  |
  v
Current State
  |
  v
Current Behavior
```

> **State = Change state → Change behavior.**

Easy memory:

```text
Context = Holds State
State   = Defines Behavior
```

---

# Summary

| Aspect         | Description                     |
|----------------|---------------------------------|
| Pattern Type   | Behavioral                      |
| Purpose        | Change behavior based on state  |
| Context        | Holds current State             |
| State          | Common state interface          |
| Concrete State | State-specific behavior         |
| Main Problem   | Large state-based if/else       |
| Common Example | Order Lifecycle                 |
| State Change   | Context or State can transition |
| Main Benefit   | Clean state-specific behavior   |
| Easy Trick     | Change State = Change Behavior  |