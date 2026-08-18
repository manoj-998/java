# Chain of Responsibility Design Pattern

## Definition
The **Chain of Responsibility (CoR)** is a **Behavioral Design Pattern** that passes a request through a chain of handlers until one of them handles it.

In simple words, instead of the client deciding **which object should handle the request**, the request moves from one handler to another until it is processed.

> **Chain of Responsibility = Pass the request through a chain of handlers.**

---

# Why Chain of Responsibility?
Suppose an API request needs multiple checks:

```text
Request
  |
  v
Authentication
  |
  v
Authorization
  |
  v
Validation
  |
  v
Business Logic
```

Without CoR, all checks may be written in one class:

```java
void process(Request request) {
    checkAuthentication();
    checkAuthorization();
    validateRequest();
    processRequest();
}
```

As more checks are added, the method becomes difficult to maintain.

CoR separates every responsibility into its own handler.

---

# Problem Without Chain of Responsibility

```java
class RequestService {
    void process(String request) {
        if (!authenticate(request)) return;
        if (!authorize(request)) return;
        if (!validate(request)) return;

        System.out.println("Processing request");
    }
}
```

Problems:

- One class handles many responsibilities.
- Difficult to add/remove checks.
- Difficult to change execution order.
- Difficult to reuse individual checks.
- Violates Single Responsibility Principle.

---

# Solution
Create separate handlers and connect them as a chain.

```text
Client
  |
  v
AuthenticationHandler
  |
  v
AuthorizationHandler
  |
  v
ValidationHandler
  |
  v
FinalHandler
```

Each handler:

```text
1. Receives request
2. Performs its responsibility
3. Stops the chain if request fails
4. Otherwise passes request to next handler
```

---

# Main Components

## 1. Handler
Defines the common behavior and stores the next handler.

```text
Handler
```

## 2. Concrete Handler
Performs a specific check.

Examples:

```text
AuthenticationHandler
AuthorizationHandler
ValidationHandler
```

## 3. Client
Creates the chain and sends the request to the first handler.

---

# Simple Java Example

```java
public class ChainOfResponsibilityExample {

    /**
     * HANDLER
     * Base class for all request handlers.
     */
    abstract static class Handler {
        protected Handler next;

        Handler setNext(Handler next) {
            this.next = next;
            return next;
        }

        void next(String request) {
            if (next != null) {
                next.handle(request);
            }
        }

        abstract void handle(String request);
    }

    /**
     * CONCRETE HANDLER
     * Checks authentication.
     */
    static class AuthenticationHandler extends Handler {
        void handle(String request) {
            System.out.println("Authentication passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Checks authorization.
     */
    static class AuthorizationHandler extends Handler {
        void handle(String request) {
            System.out.println("Authorization passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Validates request.
     */
    static class ValidationHandler extends Handler {
        void handle(String request) {
            System.out.println("Validation passed");
            next(request);
        }
    }

    /**
     * CONCRETE HANDLER
     * Performs final processing.
     */
    static class RequestHandler extends Handler {
        void handle(String request) {
            System.out.println("Processing: " + request);
        }
    }

    /**
     * CLIENT
     * Creates the chain and sends request to first handler.
     */
    public static void main(String[] args) {
        Handler authentication = new AuthenticationHandler();
        Handler authorization = new AuthorizationHandler();
        Handler validation = new ValidationHandler();
        Handler processor = new RequestHandler();

        authentication
                .setNext(authorization)
                .setNext(validation)
                .setNext(processor);

        authentication.handle("Create Order");
    }
}
```

Output:

```text
Authentication passed
Authorization passed
Validation passed
Processing: Create Order
```

---

# Internal Flow

When:

```java
authentication.handle("Create Order");
```

is called:

```text
AuthenticationHandler
        |
      Passed
        |
        v
AuthorizationHandler
        |
      Passed
        |
        v
ValidationHandler
        |
      Passed
        |
        v
RequestHandler
        |
        v
Process Request
```

---

# What Happens When a Handler Fails?
A handler can stop the chain by **not calling the next handler**.

Example:

```java
static class AuthenticationHandler extends Handler {
    void handle(String request) {
        boolean authenticated = false;

        if (!authenticated) {
            System.out.println("Authentication failed");
            return;
        }

        next(request);
    }
}
```

Flow:

```text
Authentication
      |
    Failed
      |
      X

Authorization is NOT called
Validation is NOT called
```

This is an important feature of CoR.

---

# Real-World Software Examples

## API Request Filters

```text
Request
  |
  v
Authentication Filter
  |
  v
Authorization Filter
  |
  v
Validation Filter
  |
  v
Controller
```

## Logging

```text
Log Message
    |
    v
DEBUG Handler
    |
    v
INFO Handler
    |
    v
ERROR Handler
```

## Approval System

```text
Expense Request
      |
      v
Team Lead
      |
      v
Manager
      |
      v
Director
```

Example:

```text
₹5,000   → Team Lead
₹50,000  → Manager
₹5,00,000 → Director
```

## Support Ticket

```text
Ticket
  |
  v
Level 1 Support
  |
  v
Level 2 Support
  |
  v
Level 3 Support
```

If Level 1 cannot solve it, the request moves to Level 2.

---

# Java/Spring Examples
Chain of Responsibility concepts are commonly seen in:

```text
Servlet Filters
Spring Security Filter Chain
Middleware
Interceptors
Validation Pipelines
Logging Handlers
```

Example:

```text
HTTP Request
    |
    v
SecurityFilter1
    |
    v
SecurityFilter2
    |
    v
SecurityFilter3
    |
    v
Controller
```

---

# Advantages

- Reduces coupling between sender and receiver.
- Each handler has one responsibility.
- Easy to add new handlers.
- Easy to remove handlers.
- Execution order can be changed.
- Handlers can be reused.
- Request processing becomes flexible.
- Supports Single Responsibility Principle.
- Supports Open/Closed Principle.

---

# Disadvantages

- Request may pass through many handlers.
- Debugging the complete flow can be difficult.
- Incorrect chain configuration can cause issues.
- A request may remain unhandled.
- Long chains may add processing overhead.

---

# When to Use
Use Chain of Responsibility when:

- A request needs multiple processing steps.
- Multiple objects may handle a request.
- The sender should not know the exact handler.
- Handlers need to be added or removed easily.
- Processing order may change.

Examples:

```text
API Filters
Authentication Pipeline
Validation Pipeline
Logging
Approval Workflow
Support Escalation
```

---

# When Not to Use
Avoid CoR when:

- Only one fixed handler exists.
- Processing flow is very simple.
- Every handler must always execute and the pipeline abstraction adds no value.
- Chain configuration would make the system unnecessarily complex.

---

# Design Considerations
Each handler should have **one responsibility**.

Good:

```text
AuthenticationHandler
AuthorizationHandler
ValidationHandler
```

Avoid:

```text
AuthenticationAuthorizationValidationHandler
```

The common structure is:

```text
Handler
  |
  ├── ConcreteHandler1
  ├── ConcreteHandler2
  └── ConcreteHandler3
```

Each Handler has:

```text
HAS-A next Handler
```

---

# Chain of Responsibility vs Decorator
Both can form a chain, but their intent is different.

```text
CoR       → Pass request to handlers
Decorator → Add behavior by wrapping objects
```

In CoR, a handler may **stop the request**.

In Decorator, decorators normally continue wrapping/delegating behavior.

---

# Chain of Responsibility vs Strategy

```text
CoR      → Request may pass through multiple handlers
Strategy → One strategy is selected to perform an operation
```

Example:

```text
CoR:
Request → Auth → Validation → Processing

Strategy:
PaymentService → UPI Strategy
```

---

# Chain of Responsibility vs Command

```text
CoR     → Determines who handles the request
Command → Encapsulates a request as an object
```

The two patterns can also be used together.

---

# Pitfalls

- Don't create unnecessarily long chains.
- Make sure the chain is configured correctly.
- Avoid putting multiple responsibilities in one handler.
- Be clear about when the chain should stop.
- Handle the case where no handler processes the request.
- Keep handler ordering clear.

---

# Interview Questions

## What is Chain of Responsibility?
Chain of Responsibility is a Behavioral Design Pattern where a request passes through a chain of handlers until it is handled.

## Which category does it belong to?
**Behavioral Design Pattern**

## What are the main components?
- Handler
- Concrete Handler
- Client

## How does a handler pass the request?
Each handler stores a reference to the next handler.

```text
Handler HAS-A Next Handler
```

## Can a handler stop the chain?
Yes. It simply does not call the next handler.

## What are real-world examples?
```text
Spring Security Filters
Servlet Filters
Approval Workflow
Logging
Support Escalation
```

## What is the main advantage?
The sender does not need to know which object will handle the request.

## CoR vs Strategy?

```text
CoR      → Multiple handlers can participate
Strategy → One selected strategy handles the operation
```

## CoR vs Decorator?

```text
CoR       → Pass/handle request
Decorator → Add behavior
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Request passes through multiple handlers.
- Each handler has one responsibility.
- Handler keeps a reference to the next handler.
- Handler can process, pass, or stop the request.
- Client sends request only to the first handler.
- Reduces sender-receiver coupling.
- Common in filters and middleware.
- Spring Security Filter Chain is a practical example.

---

# Easy Trick to Remember
Think about a **Support Ticket**:

```text
Customer
   |
   v
Level 1 Support
   |
Cannot Handle
   |
   v
Level 2 Support
   |
Cannot Handle
   |
   v
Level 3 Support
```

Each person either:

```text
Handle Request
     OR
Pass to Next
```

> **Chain of Responsibility = Handle it or pass it to the next handler.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Pass request through multiple handlers |
| Handler | Defines common handling behavior |
| Concrete Handler | Performs specific responsibility |
| Client | Starts the chain |
| Relationship | Handler HAS-A next Handler |
| Can Stop Chain? | Yes |
| Common Example | Spring Security Filter Chain |
| Main Benefit | Loose coupling and flexible processing |
| Easy Trick | Handle or Pass to Next |