# Liskov Substitution Principle (LSP)

## Definition

The **Liskov Substitution Principle (LSP)** is the **third principle** of SOLID.

> **"Objects of a superclass should be replaceable with objects of its subclasses without affecting the correctness of the program."**
>
> — Barbara Liskov

Simply put:

> **A subclass should be able to replace its parent class without changing the expected behavior of the application.**

---

# Why LSP?

When a class extends another class, it promises to behave like its parent.

If the child changes that expected behavior, it violates LSP.

---

# Bad Example (Violates LSP)

## Bird Example

```java
class Bird {

    public void fly() {
        System.out.println("Flying...");
    }
}
```

```java
class Sparrow extends Bird {
}
```

```java
class Penguin extends Bird {

    @Override
    public void fly() {
        throw new UnsupportedOperationException("Penguins can't fly");
    }
}
```

Client code:

```java
public class Main {

    public static void makeBirdFly(Bird bird) {
        bird.fly();
    }

    public static void main(String[] args) {

        makeBirdFly(new Sparrow());

        makeBirdFly(new Penguin()); // Exception
    }
}
```

### Problem

`Penguin` is a `Bird`, but it cannot perform the behavior (`fly()`) expected from a `Bird`.

This violates the **Liskov Substitution Principle**.

---

# Good Example (Follows LSP)

Separate flying behavior from birds.

```java
class Bird {
}
```

```java
interface Flyable {
    void fly();
}
```

```java
class Sparrow extends Bird implements Flyable {

    @Override
    public void fly() {
        System.out.println("Sparrow is flying");
    }
}
```

```java
class Penguin extends Bird {

    public void swim() {
        System.out.println("Penguin is swimming");
    }
}
```

Client:

```java
public class Main {

    public static void makeBirdFly(Flyable bird) {
        bird.fly();
    }

    public static void main(String[] args) {

        makeBirdFly(new Sparrow());

        // Penguin cannot be passed here
    }
}
```

Now every object passed to `makeBirdFly()` can actually fly.

---

# Another Example

## Bad

```java
class Rectangle {

    protected int width;
    protected int height;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int area() {
        return width * height;
    }
}
```

```java
class Square extends Rectangle {

    @Override
    public void setWidth(int width) {
        this.width = width;
        this.height = width;
    }

    @Override
    public void setHeight(int height) {
        this.width = height;
        this.height = height;
    }
}
```

Client:

```java
Rectangle rectangle = new Square();

rectangle.setWidth(5);
rectangle.setHeight(10);

System.out.println(rectangle.area());
```

Expected:

```
50
```

Actual:

```
100
```

The subclass changes the expected behavior of the parent.

This violates LSP.

---

# Spring Boot Example

Suppose we have different payment methods.

```java
public interface PaymentService {
    void pay(double amount);
}
```

Credit Card:

```java
@Service
class CardPaymentService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("Card Payment");
    }
}
```

UPI:

```java
@Service
class UpiPaymentService implements PaymentService {

    @Override
    public void pay(double amount) {
        System.out.println("UPI Payment");
    }
}
```

Both implementations behave as expected.

Client:

```java
@Service
class CheckoutService {

    private final PaymentService paymentService;

    public CheckoutService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void checkout(double amount) {
        paymentService.pay(amount);
    }
}
```

Any implementation of `PaymentService` can replace another without breaking the application.

This follows LSP.

---

# Benefits

- Proper use of inheritance
- More reliable polymorphism
- Easier maintenance
- Better code reusability
- Fewer runtime errors

---

# Signs That LSP is Violated

- Child class throws `UnsupportedOperationException`
- Child overrides methods with completely different behavior
- Client code checks object type using `instanceof`
- Subclass cannot perform all operations expected by the parent

---

# Interview Answer

## What is the Liskov Substitution Principle?

The Liskov Substitution Principle states that **a subclass should be able to replace its superclass without changing the correctness or expected behavior of the program**. If replacing a parent object with its child breaks the application, then the inheritance hierarchy is incorrect.

---

# Key Points

- Child class should behave like the parent.
- Do not override methods with incompatible behavior.
- Prefer interfaces when different behaviors exist.
- Avoid inheritance when the relationship is not truly **"is-a"**.

---

# Summary

| Bad Design | Good Design |
|------------|-------------|
| Child changes parent behavior | Child preserves parent behavior |
| Throws `UnsupportedOperationException` | Supports all expected operations |
| Incorrect inheritance | Proper inheritance or interfaces |
| Breaks polymorphism | Supports polymorphism |