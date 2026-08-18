# Command Design Pattern

## Definition
The **Command Design Pattern** is a **Behavioral Design Pattern** that converts a **request or action into a separate object**.

In simple words, instead of the client directly calling a service method, we create a **Command object** that contains the request. Another object called the **Invoker** executes that command.

> **Command = Wrap a request/action inside an object.**

---

# Why Command Pattern?
Suppose we have an Order Service:

```java
orderService.createOrder();
orderService.cancelOrder();
orderService.refundOrder();
```

The client directly knows which service and method to call.

```text
Client
  |
  v
OrderService
  |
  ├── createOrder()
  ├── cancelOrder()
  └── refundOrder()
```

But sometimes we need to:

- Queue requests
- Schedule requests
- Retry failed requests
- Log requests
- Undo operations
- Store request history
- Execute requests later

Command Pattern converts each action into an object:

```text
CreateOrderCommand
CancelOrderCommand
RefundOrderCommand
```

---

# Problem Without Command

```java
class OrderController {
    OrderService service = new OrderService();

    void create() {
        service.createOrder();
    }

    void cancel() {
        service.cancelOrder();
    }
}
```

Problems:

- Client directly depends on service methods.
- Difficult to queue operations.
- Difficult to store request history.
- Difficult to implement undo.
- Difficult to schedule/retry commands.
- Sender and receiver are tightly coupled.

---

# Solution
Convert every request into a Command object.

```text
Client
  |
  v
Command
  |
  v
Invoker
  |
  v
Receiver
```

Example:

```text
Client
  |
  v
CreateOrderCommand
  |
  v
OrderService.createOrder()
```

The command knows **what action should be executed** and which receiver performs it.

---

# Main Components

## 1. Command
Defines the common operation for commands.

```java
interface Command {
    void execute();
}
```

## 2. Concrete Command
Implements a specific request.

Examples:

```text
CreateOrderCommand
CancelOrderCommand
```

## 3. Receiver
Contains the actual business logic.

Example:

```text
OrderService
```

## 4. Invoker
Triggers the Command.

Example:

```text
CommandExecutor
```

## 5. Client
Creates the Receiver, Command and Invoker.

---

# Structure

```text
Client
  |
  v
Invoker
  |
  v
Command
  |
  v
ConcreteCommand
  |
  v
Receiver
```

Example:

```text
Client
  |
  v
CommandExecutor
  |
  v
CreateOrderCommand
  |
  v
OrderService
```

---

# Simple Java Example

```java
public class CommandExample {

    /**
     * COMMAND
     * Common interface for all commands.
     */
    interface Command {
        void execute();
    }

    /**
     * RECEIVER
     * Contains actual business logic.
     */
    static class OrderService {
        void createOrder() {
            System.out.println("Order created");
        }

        void cancelOrder() {
            System.out.println("Order cancelled");
        }
    }

    /**
     * CONCRETE COMMAND
     * Encapsulates create-order request.
     */
    static class CreateOrderCommand implements Command {
        private final OrderService service;

        CreateOrderCommand(OrderService service) {
            this.service = service;
        }

        public void execute() {
            service.createOrder();
        }
    }

    /**
     * CONCRETE COMMAND
     * Encapsulates cancel-order request.
     */
    static class CancelOrderCommand implements Command {
        private final OrderService service;

        CancelOrderCommand(OrderService service) {
            this.service = service;
        }

        public void execute() {
            service.cancelOrder();
        }
    }

    /**
     * INVOKER
     * Executes the command without knowing its implementation.
     */
    static class CommandExecutor {
        void execute(Command command) {
            command.execute();
        }
    }

    /**
     * CLIENT
     * Creates receiver, commands and invoker.
     */
    public static void main(String[] args) {
        OrderService service = new OrderService();

        Command create = new CreateOrderCommand(service);
        Command cancel = new CancelOrderCommand(service);

        CommandExecutor executor = new CommandExecutor();

        executor.execute(create);
        executor.execute(cancel);
    }
}
```

Output:

```text
Order created
Order cancelled
```

---

# Internal Flow
When:

```java
executor.execute(create);
```

is called:

```text
CommandExecutor
      |
      v
CreateOrderCommand.execute()
      |
      v
OrderService.createOrder()
      |
      v
Order Created
```

Notice:

```text
Invoker knows Command
Command knows Receiver
Receiver knows Business Logic
```

---

# Important Components Explained

## Receiver = Does the Actual Work

```java
class OrderService {
    void createOrder() {
        System.out.println("Order created");
    }
}
```

Think:

```text
Receiver = Worker
```

## Command = Represents the Request

```java
class CreateOrderCommand implements Command {
    private OrderService service;

    public void execute() {
        service.createOrder();
    }
}
```

Think:

```text
Command = Request Object
```

## Invoker = Triggers the Request

```java
class CommandExecutor {
    void execute(Command command) {
        command.execute();
    }
}
```

Think:

```text
Invoker = Trigger
```

---

# Real-World Example: Job Queue
Command Pattern is useful when requests need to be executed later.

```text
Client
  |
  v
Create Commands
  |
  v
Command Queue
  |
  v
Worker
  |
  v
Execute Commands
```

Example:

```java
Queue<Command> queue = new LinkedList<>();

queue.add(new CreateOrderCommand(service));
queue.add(new CancelOrderCommand(service));
```

Later:

```java
while (!queue.isEmpty()) {
    Command command = queue.poll();
    command.execute();
}
```

This is possible because the request is now an **object**.

---

# Command with Undo
Command Pattern can support undo operations.

```java
interface Command {
    void execute();
    void undo();
}
```

Example:

```java
class Light {
    void on() {
        System.out.println("Light ON");
    }

    void off() {
        System.out.println("Light OFF");
    }
}
```

Command:

```java
class LightOnCommand implements Command {
    private Light light;

    LightOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }

    public void undo() {
        light.off();
    }
}
```

Flow:

```text
execute()
   |
   v
Light ON

undo()
   |
   v
Light OFF
```

---

# Command Queue
Because Command is an object, it can be stored.

```java
Queue<Command> queue = new LinkedList<>();
```

Add commands:

```java
queue.add(createCommand);
queue.add(cancelCommand);
```

Execute later:

```java
Command command = queue.poll();
command.execute();
```

This makes Command useful for:

```text
Background Jobs
Task Queues
Schedulers
Retry Systems
```

---

# Real-World Software Examples

## Background Job Processing

```text
API
 |
 v
Command
 |
 v
Queue
 |
 v
Worker
 |
 v
Service
```

Examples:

```text
SendEmailCommand
GenerateReportCommand
ProcessPaymentCommand
CreateInvoiceCommand
```

## Text Editor

```text
CopyCommand
PasteCommand
DeleteCommand
```

Commands can support:

```text
execute()
undo()
```

## Database Operations

```text
InsertCommand
UpdateCommand
DeleteCommand
```

## Remote Control

```text
Button
  |
  v
Command
  |
  v
Device
```

Examples:

```text
LightOnCommand
LightOffCommand
TVOnCommand
```

---

# Advantages

- Decouples sender from receiver.
- Converts requests into objects.
- Easy to queue requests.
- Easy to schedule commands.
- Supports undo/redo.
- Supports logging/history.
- Supports retry.
- Easy to add new commands.
- Supports Open/Closed Principle.

---

# Disadvantages

- Creates additional classes.
- One class may be needed for each command.
- Simple operations may become over-engineered.
- Undo logic can become complex.
- error handling becomes difficult

---

# When to Use
Use Command Pattern when:

- Requests need to be queued.
- Operations need to execute later.
- Undo/redo is required.
- Requests need logging/history.
- Failed operations need retry.
- Requests need scheduling.
- Sender should not directly depend on receiver.

Examples:

```text
Job Queue
Task Scheduler
Text Editor
Remote Control
Transaction Processing
Background Jobs
```

---

# When Not to Use
Avoid Command when:

- Operation is very simple.
- No queue/retry/undo/history is required.
- Sender directly calling the receiver is already clean.
- Creating separate command classes adds unnecessary complexity.

---

# Command vs Strategy

Both use interfaces, but their purpose is different.

```text
Command  → Represents WHAT action to perform
Strategy → Represents HOW an operation is performed
```

Example:

```text
Command:
CreateOrderCommand

Strategy:
CreditCardPaymentStrategy
UPIPaymentStrategy
```

Easy trick:

```text
Command  = Action
Strategy = Algorithm
```

---

# Command vs Chain of Responsibility

```text
Command → Converts request into an object
CoR     → Passes request through handlers
```

Example:

```text
Command:
CreateOrderCommand
      |
      v
OrderService
```

```text
CoR:
Request
  |
Auth
  |
Validation
  |
OrderHandler
```

They can also work together:

```text
Command
   |
   v
Handler Chain
```

---

# Command vs Observer

```text
Command  → Encapsulates an action/request
Observer → Notifies multiple interested objects
```

---

# Design Considerations
Keep business logic inside the **Receiver**.

Good:

```text
Command
   |
   v
Receiver
   |
   v
Business Logic
```

Avoid:

```text
Command
   |
All Business Logic
```

Command should mainly:

```text
Store Request Data
        +
Call Receiver
```

---

# Pitfalls

- Don't put all business logic inside Command.
- Avoid creating commands for trivial operations.
- Keep commands focused on one action.
- Store required request data inside the Command.
- Be careful when implementing undo for irreversible operations.
- Avoid very large Command classes.

---

# Interview Questions

## What is Command Pattern?
Command is a Behavioral Design Pattern that encapsulates a request as an object.

## Which category does Command belong to?
**Behavioral Design Pattern**

## What are the main components?
- Command
- Concrete Command
- Receiver
- Invoker
- Client

## What is Receiver?
Receiver performs the actual business operation.

```text
OrderService
```

## What is Invoker?
Invoker triggers the Command.

```text
CommandExecutor
```

## What is Concrete Command?
It represents a specific action and delegates it to the Receiver.

```text
CreateOrderCommand
CancelOrderCommand
```

## Why convert a request into an object?
Because an object can be:

```text
Stored
Queued
Logged
Retried
Scheduled
Undone
```

## Does Command contain business logic?
Normally, the main business logic should remain in the **Receiver**. Command coordinates the request.

## What are common Command Pattern examples?
```text
Job Queues
Text Editor Undo/Redo
Task Scheduler
Remote Control
Background Jobs
```

## Command vs Strategy?

```text
Command  → WHAT action
Strategy → HOW to perform something
```

## Command vs Chain of Responsibility?

```text
Command → Encapsulate request
CoR     → Pass request through handlers
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Encapsulates a request as an object.
- Command defines `execute()`.
- Concrete Command represents an action.
- Receiver performs actual business logic.
- Invoker executes the Command.
- Client creates/configures commands.
- Commands can be queued.
- Commands can be scheduled.
- Commands can support retry.
- Commands can support undo/redo.
- Useful for background processing.

---

# Easy Trick to Remember
Think about a **restaurant**.

```text
Customer
   |
   v
Order
   |
   v
Waiter
   |
   v
Chef
```

Mapping:

```text
Customer → Client
Order    → Command
Waiter   → Invoker
Chef     → Receiver
```

The waiter doesn't cook the food.

The waiter simply passes the order to the chef.

Similarly:

```text
Invoker
   |
   v
Command
   |
   v
Receiver
```

> **Command = Convert an action/request into an object.**

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Encapsulate request as an object |
| Command | Common command interface |
| Concrete Command | Represents specific action |
| Receiver | Performs actual work |
| Invoker | Executes command |
| Client | Creates/configures command |
| Main Benefit | Queue, retry, undo, schedule |
| Common Example | Background Job Queue |
| Easy Trick | Command = Request as Object |