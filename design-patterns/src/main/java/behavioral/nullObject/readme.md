# Null Object Design Pattern

## Definition
The **Null Object Pattern** is a **Behavioral Design Pattern** that uses a special object instead of `null` to represent the absence of an object.

In simple words, instead of returning `null` and repeatedly checking `if (object != null)`, we return a **Null Object** that implements the same interface but performs a safe/default operation.

> **Null Object = Replace `null` with an object that does nothing safely.**

---

# Why Null Object Pattern?
Suppose we have a notification service:

```java
Notification notification = getNotification();

if (notification != null) {
    notification.send();
}
```

Every client needs a null check:

```java
if (notification != null) {
    notification.send();
}
```

If someone forgets the check:

```java
notification.send();
```

we may get:

```text
NullPointerException
```

Null Object removes these repeated checks.

```java
Notification notification = getNotification();
notification.send();
```

Even when no real notification exists, a `NullNotification` safely handles the call.

---

# Problem Without Null Object

```java
interface Notification {
    void send(String message);
}

class EmailNotification implements Notification {
    public void send(String message) {
        System.out.println("Email: " + message);
    }
}
```

Client:

```java
Notification notification = getNotification();

if (notification != null) {
    notification.send("Order created");
}
```

Problems:

- Repeated null checks
- Risk of `NullPointerException`
- Client code becomes cluttered
- Client needs to handle missing objects
- Easy to forget null checks

---

# Solution
Create a Null Object implementing the same interface.

```java
class NullNotification implements Notification {
    public void send(String message) {
        // Do nothing
    }
}
```

Now:

```java
Notification notification = getNotification();
notification.send("Order created");
```

No null check is required.

---

# Main Components

## 1. Abstract Object
Common interface for real and null objects.

```java
interface Notification {
    void send(String message);
}
```

## 2. Real Object
Performs the actual operation.

```text
EmailNotification
```

## 3. Null Object
Provides safe/default behavior.

```text
NullNotification
```

## 4. Client
Uses the common interface without checking for `null`.

---

# Structure

```text
                 Notification
                 /          \
                /            \
EmailNotification        NullNotification
      |                        |
 Real Action               Do Nothing
```

Client only knows:

```text
Notification
```

---

# Simple Java Example

```java
public class NullObjectExample {

    /**
     * ABSTRACT OBJECT
     * Common contract for real and null objects.
     */
    interface Notification {
        void send(String message);
    }

    /**
     * REAL OBJECT
     * Sends an actual email.
     */
    static class EmailNotification implements Notification {
        @Override
        public void send(String message) {
            System.out.println("Email: " + message);
        }
    }

    /**
     * NULL OBJECT
     * Represents absence of notification.
     * Safely does nothing.
     */
    static class NullNotification implements Notification {
        @Override
        public void send(String message) {
            // Do nothing
        }
    }

    /**
     * Factory returns a real object or Null Object.
     * It never returns null.
     */
    static Notification getNotification(boolean enabled) {
        if (enabled) {
            return new EmailNotification();
        }

        return new NullNotification();
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        Notification notification1 = getNotification(true);
        notification1.send("Order created");

        Notification notification2 = getNotification(false);
        notification2.send("Order created");
    }
}
```

Output:

```text
Email: Order created
```

The second call safely does nothing.

---

# Internal Flow

When notification is enabled:

```text
getNotification(true)
        |
        v
EmailNotification
        |
        v
send()
        |
        v
Email Sent
```

When notification is disabled:

```text
getNotification(false)
        |
        v
NullNotification
        |
        v
send()
        |
        v
Do Nothing
```

In both cases:

```java
notification.send();
```

is safe.

---

# Without Null Object vs With Null Object

## Without Null Object

```java
Notification notification = getNotification();

if (notification != null) {
    notification.send("Hello");
}
```

Client must check:

```text
Is it null?
```

## With Null Object

```java
Notification notification = getNotification();

notification.send("Hello");
```

The returned object decides its behavior.

---

# Real-World Example: Logger
Suppose logging is optional.

Without Null Object:

```java
if (logger != null) {
    logger.log("Payment completed");
}
```

With Null Object:

```java
logger.log("Payment completed");
```

Real Logger:

```java
class ConsoleLogger implements Logger {
    public void log(String message) {
        System.out.println(message);
    }
}
```

Null Logger:

```java
class NullLogger implements Logger {
    public void log(String message) {
        // Do nothing
    }
}
```

Now:

```text
Logging Enabled  → ConsoleLogger
Logging Disabled → NullLogger
```

Client code remains unchanged.

---

# Another Software Example: User
Suppose we search for a user:

```java
User user = findUser(id);
```

Instead of returning:

```java
null
```

we could return:

```text
NullUser
```

Example:

```java
interface User {
    String getName();
}

class RealUser implements User {
    public String getName() {
        return "John";
    }
}

class NullUser implements User {
    public String getName() {
        return "Guest";
    }
}
```

Client:

```java
System.out.println(user.getName());
```

Possible results:

```text
John
```

or:

```text
Guest
```

without a null check.

---

# Advantages

- Reduces null checks.
- Prevents many `NullPointerException`s.
- Makes client code cleaner.
- Provides safe default behavior.
- Client treats real and null objects uniformly.
- Keeps missing-object behavior in one place.

---

# Disadvantages

- Adds an additional class.
- Can hide unexpected missing data.
- Not every `null` should be replaced with a Null Object.
- Developers may not realize they received a Null Object.
- Incorrect default behavior can hide bugs.

---

# When to Use
Use Null Object when:

- `null` checks appear repeatedly.
- Missing object has a reasonable default behavior.
- Doing nothing is valid behavior.
- Client should not care whether the real object exists.
- You want to avoid repeated `NullPointerException` handling.

Examples:

```text
Optional Logger
Optional Notification
Guest User
No Discount
No Permission
Default Handler
```

---

# When Not to Use
Avoid Null Object when:

- Missing data is actually an error.
- The caller must know that the object doesn't exist.
- Returning a default object could hide a bug.
- Different missing cases require different error handling.

For example, if a payment transaction cannot be found:

```text
Transaction Not Found
```

may need to throw an exception rather than silently returning a Null Object.

---

# Null Object vs Null Check

Without:

```java
if (logger != null) {
    logger.log("Hello");
}
```

With Null Object:

```java
logger.log("Hello");
```

Think:

```text
Without Null Object
Client handles absence

With Null Object
Object handles absence
```

---

# Null Object vs Optional
Java provides:

```java
Optional<T>
```

The purpose is slightly different.

```text
Null Object → Provides default behavior
Optional    → Explicitly represents presence/absence
```

Example:

```java
Optional<User> user = findUser();
```

The caller knows the value may not exist.

With Null Object:

```java
User user = findUser();
user.getName();
```

The caller always receives an object.

Use:

```text
Null Object → When safe/default behavior exists
Optional    → When caller should explicitly handle absence
```

---

# Null Object vs Strategy
A Null Object can sometimes behave like a Strategy that does nothing.

Example:

```text
Notification
├── EmailNotification
└── NullNotification
```

But their intent differs:

```text
Strategy    → Select behavior/algorithm
Null Object → Represent absence safely
```

---

# Design Considerations
Real Object and Null Object should implement the same interface:

```text
             Notification
              /        \
             /          \
EmailNotification   NullNotification
```

The client depends only on:

```java
Notification
```

Prefer making the Null Object **stateless** when possible.

Since it usually contains no state, one shared instance can often be reused.

---

# Singleton Null Object
Because a Null Object usually has no state, we don't necessarily need to create it repeatedly.

```java
class NullNotification implements Notification {
    static final NullNotification INSTANCE =
            new NullNotification();

    private NullNotification() {}

    public void send(String message) {
        // Do nothing
    }
}
```

Usage:

```java
return NullNotification.INSTANCE;
```

This avoids creating unnecessary Null Object instances.

---

# Pitfalls

- Don't use Null Object to hide real errors.
- Don't replace every `null` blindly.
- Keep Null Object behavior predictable.
- Prefer stateless Null Objects.
- Don't put complex business logic inside Null Object.
- Use `Optional` when absence should be explicitly handled.

---

# Interview Questions

## What is Null Object Pattern?
Null Object is a Behavioral Design Pattern that replaces `null` with an object providing safe/default behavior.

## Why use Null Object?
To reduce repeated null checks and avoid unnecessary `NullPointerException`s.

## What are the main components?
- Abstract Object
- Real Object
- Null Object
- Client

## What does Null Object normally do?
It may:

```text
Do Nothing
Return Default Value
Provide Safe Behavior
```

## Does Null Object return null?
No.

The main idea is to provide an actual object instead of `null`.

## Null Object vs Optional?

```text
Null Object → Default behavior
Optional    → Explicit absence
```

## Can Null Object be Singleton?
Yes.

If it is stateless, one shared instance is usually enough.

## When should Null Object not be used?
When absence represents an actual error that the caller must handle.

---

# Key Points

- Category: **Behavioral Design Pattern**
- Replaces `null` with a safe object.
- Reduces repeated null checks.
- Real and Null objects implement the same interface.
- Null Object usually does nothing or returns a default value.
- Client doesn't need to know whether the object is real or null.
- Useful for optional behavior.
- Stateless Null Objects can be Singleton.
- Don't use it to hide actual errors.

---

# Easy Trick to Remember

Without Null Object:

```text
Get Object
   |
   v
Is Null?
 /    \
Yes    No
 |      |
Skip   Execute
```

With Null Object:

```text
Get Object
   |
   v
Execute
```

The object itself decides:

```text
Real Object → Do Work
Null Object → Do Nothing
```

> **Null Object = Instead of giving me nothing, give me something that safely does nothing.**

---

# Summary

| Aspect            | Description                           |
|-------------------|---------------------------------------|
| Pattern Type      | Behavioral                            |
| Purpose           | Replace null with safe/default object |
| Real Object       | Performs actual operation             |
| Null Object       | Does nothing / returns default        |
| Main Benefit      | Removes repeated null checks          |
| Prevents          | Many NullPointerExceptions            |
| Best For          | Optional behavior                     |
| Alternative       | `Optional`                            |
| Can be Singleton? | Yes, if stateless                     |
| Easy Trick        | Null Object = Safe Nothing            |