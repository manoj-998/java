# Single Responsibility Principle (SRP)

## Definition

The **Single Responsibility Principle (SRP)** is the **first principle** of the SOLID principles.

> **"A class should have only one reason to change."**
>
> — Robert C. Martin (Uncle Bob)

A class should have **one responsibility** and therefore **only one reason to change**.

---

# What is a Responsibility?

A responsibility is a **single job or concern** that a class is responsible for.

For example:

- User authentication
- Email notification
- Payment processing
- Report generation
- Database operations

Each of these should ideally be handled by a separate class.

---

# Bad Example (Violates SRP)

```java
class Employee {

    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    // Responsibility 1 - Business Logic
    public double calculateSalary() {
        return salary;
    }

    // Responsibility 2 - Database
    public void saveEmployee() {
        System.out.println("Saving employee...");
    }

    // Responsibility 3 - Reporting
    public void generateReport() {
        System.out.println("Generating report...");
    }
}
```

## Problems

This class has multiple responsibilities:

- Employee data
- Salary calculation
- Database operations
- Report generation

Reasons to change:

- Salary calculation changes
- Database changes
- Report format changes

This violates SRP.

---

# Good Example (Follows SRP)

## Employee

```java
class Employee {

    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public double getSalary() {
        return salary;
    }
}
```

---

## Salary Service

```java
class SalaryService {

    public double calculateSalary(Employee employee) {
        return employee.getSalary();
    }
}
```

---

## Repository

```java
class EmployeeRepository {

    public void save(Employee employee) {
        System.out.println("Saving employee...");
    }
}
```

---

## Report Service

```java
class ReportService {

    public void generate(Employee employee) {
        System.out.println("Generating report...");
    }
}
```

Each class now has **one responsibility**.

---

# Real World Example

Imagine a restaurant.

## Chef

Responsibility:

- Cook food

---

## Cashier

Responsibility:

- Handle payments

---

## Waiter

Responsibility:

- Serve customers

If the chef also handled billing, cleaning, and customer service, the system would become difficult to manage.

Software prototype follows the same principle.

---

# Spring Boot Example

## Bad

```java
@Service
public class UserService {

    public void register(User user) {
        saveUser(user);
        sendEmail(user);
        generateReport(user);
    }

    private void saveUser(User user) {}

    private void sendEmail(User user) {}

    private void generateReport(User user) {}
}
```

This service performs:

- User persistence
- Email sending
- Report generation

Multiple responsibilities.

---

## Good

### UserService

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public void register(User user) {
        repository.save(user);
    }
}
```

---

### EmailService

```java
@Service
public class EmailService {

    public void sendWelcomeEmail(User user) {
        System.out.println("Sending email...");
    }
}
```

---

### ReportService

```java
@Service
public class ReportService {

    public void generate(User user) {
        System.out.println("Generating report...");
    }
}
```

Each service now has a single responsibility.

---

# Benefits of SRP

- Easier to understand
- Easier to maintain
- Easier to test
- Lower coupling
- Higher cohesion
- Better code reuse
- Easier debugging
- Smaller classes
- Easier code reviews

---

# Signs That SRP is Violated

A class:

- Has hundreds of lines of code
- Has unrelated methods
- Frequently changes for different reasons
- Depends on many unrelated classes
- Has multiple responsibilities

---

# Interview Question

## What is the Single Responsibility Principle?

**Answer:**

The Single Responsibility Principle states that **a class should have only one reason to change**, meaning it should have only one responsibility or concern. Each class should focus on a single task, making the code easier to maintain, test, and extend.

---

# Key Points to Remember

- SRP = One class → One responsibility
- One responsibility = One reason to change
- High cohesion, low coupling
- Improves maintainability and readability
- First principle of SOLID
- Frequently used in Spring Boot service and repository prototype

---

# Summary

| Bad Design | Good Design |
|------------|-------------|
| One class does everything | One class has one job |
| Multiple reasons to change | Single reason to change |
| Hard to test | Easy to test |
| Hard to maintain | Easy to maintain |
| Tightly coupled | Loosely coupled |
| Low cohesion | High cohesion |