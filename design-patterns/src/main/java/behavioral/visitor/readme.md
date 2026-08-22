# Visitor Design Pattern

## Definition
The **Visitor Design Pattern** is a **Behavioral Design Pattern** that allows us to **add new operations to existing classes without modifying those classes**.

In simple words, instead of adding every new operation inside the existing object, we move that operation into a separate **Visitor class**. The object simply accepts the Visitor and lets it perform the operation.

> **Visitor = Add new operations without changing existing object classes.**

---

# Why Visitor Pattern?
Suppose an e-commerce application has different order items:

```text
Book
Electronics
Grocery
```

Initially they only contain product information.

Later we need different operations:

```text
Calculate Discount
Calculate Tax
Generate Report
Export Data
```

Without Visitor, we may keep adding methods:

```java
class Book {
    void calculateTax() {}
    void calculateDiscount() {}
    void generateReport() {}
    void export() {}
}
```

The product classes keep changing whenever a new operation is introduced.

Visitor separates these operations:

```text
Book ──────────┐
Electronics ───┼──> TaxVisitor
Grocery ───────┘

Book ──────────┐
Electronics ───┼──> DiscountVisitor
Grocery ───────┘
```

---

# Problem Without Visitor

```java
class Book {
    double price;

    double calculateTax() {
        return price * 0.05;
    }

    double calculateDiscount() {
        return price * 0.10;
    }

    void generateReport() {
        // report logic
    }
}
```

Later another requirement comes:

```text
Export to CSV
```

We modify `Book` again.

Then:

```text
Export to PDF
```

Modify it again.

Problems:

- Existing classes change frequently.
- Too many unrelated operations inside domain classes.
- Classes become large.
- Adding operations affects many classes.
- Violates Single Responsibility Principle.

---

# Solution
Keep domain objects simple:

```text
Book
Electronics
Grocery
```

Move operations into Visitors:

```text
TaxVisitor
DiscountVisitor
ReportVisitor
```

Structure:

```text
                 Visitor
                /       \
        TaxVisitor   DiscountVisitor
              |
              | visits
              v
          OrderItem
         /    |     \
      Book Electronics Grocery
```

---

# Main Components

## 1. Element
Common interface for objects that can be visited.

```java
interface Item {
    void accept(Visitor visitor);
}
```

## 2. Concrete Element
Actual objects that accept Visitors.

Examples:

```text
Book
Electronics
```

## 3. Visitor
Defines operations for each Element type.

```java
interface Visitor {
    void visit(Book book);
    void visit(Electronics electronics);
}
```

## 4. Concrete Visitor
Implements a particular operation.

Examples:

```text
TaxVisitor
DiscountVisitor
```

## 5. Client
Creates Elements and Visitors and starts the visit.

---

# Simple Java Example

```java
public class VisitorExample {

    /**
     * ELEMENT
     */
    interface Item {
        void accept(Visitor visitor);
    }

    /**
     * CONCRETE ELEMENT
     */
    static class Book implements Item {
        double price;

        Book(double price) {
            this.price = price;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * CONCRETE ELEMENT
     */
    static class Electronics implements Item {
        double price;

        Electronics(double price) {
            this.price = price;
        }

        @Override
        public void accept(Visitor visitor) {
            visitor.visit(this);
        }
    }

    /**
     * VISITOR
     */
    interface Visitor {
        void visit(Book book);
        void visit(Electronics electronics);
    }

    /**
     * CONCRETE VISITOR
     * Contains tax calculation logic.
     */
    static class TaxVisitor implements Visitor {

        @Override
        public void visit(Book book) {
            System.out.println("Book Tax: " + book.price * 0.05);
        }

        @Override
        public void visit(Electronics electronics) {
            System.out.println(
                "Electronics Tax: " + electronics.price * 0.18
            );
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        Item book = new Book(1000);
        Item laptop = new Electronics(50000);

        Visitor taxVisitor = new TaxVisitor();

        book.accept(taxVisitor);
        laptop.accept(taxVisitor);
    }
}
```

Output:

```text
Book Tax: 50.0
Electronics Tax: 9000.0
```

---

# What Happens Internally?

When:

```java
book.accept(taxVisitor);
```

is called:

```text
Book.accept(TaxVisitor)
        |
        v
visitor.visit(this)
        |
        v
TaxVisitor.visit(Book)
        |
        v
Calculate Book Tax
```

For Electronics:

```java
laptop.accept(taxVisitor);
```

flow:

```text
Electronics.accept(TaxVisitor)
        |
        v
visitor.visit(this)
        |
        v
TaxVisitor.visit(Electronics)
        |
        v
Calculate Electronics Tax
```

---

# Why `visitor.visit(this)`?
This is the most important part:

```java
public void accept(Visitor visitor) {
    visitor.visit(this);
}
```

Inside `Book`:

```text
this = Book
```

So:

```java
visitor.visit(this);
```

becomes:

```java
visitor.visit(Book);
```

Inside `Electronics`:

```text
this = Electronics
```

so Java calls:

```java
visitor.visit(Electronics);
```

This helps Java choose the correct `visit()` method.

---

# Double Dispatch
Visitor Pattern uses a concept called **Double Dispatch**.

Normally Java chooses a method mainly based on the runtime object receiving the call.

Visitor uses two steps:

```text
Step 1:
Element.accept(visitor)

Step 2:
Visitor.visit(element)
```

Example:

```text
book.accept(visitor)
        |
        v
Book.accept()
        |
        v
visitor.visit(Book)
        |
        v
TaxVisitor.visit(Book)
```

Two objects participate in deciding the final operation:

```text
Element Type + Visitor Type
```

Hence:

> **Visitor commonly uses Double Dispatch.**

---

# Adding Another Visitor
Suppose we now need discounts.

We don't modify:

```text
Book
Electronics
```

Create another Visitor:

```java
static class DiscountVisitor implements Visitor {

    @Override
    public void visit(Book book) {
        System.out.println(
            "Book Discount: " + book.price * 0.10
        );
    }

    @Override
    public void visit(Electronics electronics) {
        System.out.println(
            "Electronics Discount: " + electronics.price * 0.05
        );
    }
}
```

Usage:

```java
Visitor discount = new DiscountVisitor();

book.accept(discount);
laptop.accept(discount);
```

Now:

```text
Elements
├── Book
└── Electronics

Visitors
├── TaxVisitor
└── DiscountVisitor
```

No changes are required in the existing Element classes.

---

# Internal Structure

```text
                       Visitor
                      /       \
              TaxVisitor   DiscountVisitor
                  |              |
                  | visit()      | visit()
                  v              v
             +-----------------------+
             |                       |
            Book                Electronics
             |                       |
             +------ accept() -------+
```

---

# Real-World Software Examples

## Tax Calculation

```text
Book
Electronics
Grocery
      |
      v
TaxVisitor
```

## Report Generation

```text
Order
Payment
Refund
      |
      v
ReportVisitor
```

## Export

```text
Element
   |
   ├── PDFExportVisitor
   ├── CSVExportVisitor
   └── JSONExportVisitor
```

## Compiler
A compiler may have nodes such as:

```text
VariableNode
MethodNode
ClassNode
```

Visitors can perform:

```text
Validation
Code Generation
Optimization
Static Analysis
```

---

# Advantages

- Adds new operations without modifying Element classes.
- Keeps domain classes clean.
- Separates unrelated operations.
- Easy to add new Visitors.
- Useful when object structure is stable.
- Supports Single Responsibility Principle.
- Centralizes related operation logic.

---

# Disadvantages

- Adding a new Element type is difficult.
- Every Visitor may need modification when a new Element is added.
- Can expose Element internals to Visitors.
- Double Dispatch can be difficult to understand.
- Creates additional classes.

---

# When to Use
Use Visitor when:

- Object classes rarely change.
- New operations are added frequently.
- Many operations need to work on different object types.
- You don't want operation logic inside domain classes.

Examples:

```text
Tax Calculation
Reporting
Exporting
Validation
Compiler AST Processing
Static Code Analysis
```

---

# When Not to Use
Avoid Visitor when:

- New Element types are added frequently.
- Operations rarely change.
- Object structure changes often.
- Visitor would need too much access to private internals.

Important rule:

```text
Stable Objects + Changing Operations
            ↓
         Visitor
```

---

# Visitor vs Strategy

```text
Visitor  → Add operations across different object types
Strategy → Replace one algorithm/behavior
```

Example:

```text
Visitor:
Book + Electronics → TaxVisitor

Strategy:
PaymentService → UPI / Card Strategy
```

---

# Visitor vs Iterator

```text
Iterator → HOW to traverse objects
Visitor  → WHAT to do with objects
```

They can work together:

```text
Iterator
   |
   | Traverse
   v
Elements
   |
   | accept()
   v
Visitor
```

Easy trick:

```text
Iterator = Move through objects
Visitor  = Perform operation on objects
```

---

# Visitor vs Command

```text
Visitor → Performs operation across object types
Command → Encapsulates a request/action
```

---

# Design Considerations
Visitor works best when:

```text
Element Types = Stable
Operations    = Frequently Changing
```

Example:

```text
Stable:
Book
Electronics
Grocery

Changing:
Tax
Discount
Report
Export
```

Then Visitor is a good fit.

If you frequently add:

```text
Furniture
Clothing
Medicine
Food
...
```

every Visitor may need another:

```java
visit(NewType item);
```

This is the main weakness of Visitor.

---

# Pitfalls

- Don't use Visitor when Element types change frequently.
- Keep each Visitor focused on one operation.
- Avoid exposing unnecessary internal state.
- Understand `accept()` and `visit()` clearly.
- Remember that adding a new Element can require changing all Visitors.

---

# Interview Questions

## What is Visitor Pattern?
Visitor is a Behavioral Design Pattern that allows new operations to be added to existing object structures without modifying their classes.

## Which category does Visitor belong to?
**Behavioral Design Pattern**

## What are the main components?
- Visitor
- Concrete Visitor
- Element
- Concrete Element
- Client

## What does `accept()` do?
It accepts a Visitor and calls:

```java
visitor.visit(this);
```

## What does `visit()` do?
It contains the actual operation for a specific Element type.

## What is Double Dispatch?
The final operation depends on:

```text
Visitor Type
     +
Element Type
```

and is resolved through:

```text
element.accept(visitor)
        ↓
visitor.visit(element)
```

## What is the biggest advantage?
Adding a **new operation** is easy.

Create:

```text
NewVisitor
```

without modifying existing Elements.

## What is the biggest disadvantage?
Adding a **new Element type** is difficult because all Visitors may need modification.

## Visitor vs Iterator?

```text
Iterator → HOW to traverse
Visitor  → WHAT operation to perform
```

## When is Visitor a good choice?

```text
Object Structure = Stable
Operations       = Frequently Changing
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Adds operations without modifying Element classes.
- Element provides `accept()`.
- Visitor provides `visit()`.
- Concrete Visitor contains operation logic.
- Uses Double Dispatch.
- Easy to add new operations.
- Difficult to add new Element types.
- Best when object structure is stable.
- Common in compilers and AST processing.

---

# Easy Trick to Remember
Think about a **Doctor visiting patients**.

Patients stay the same:

```text
Child
Adult
Senior
```

Different visitors can come:

```text
General Doctor
Eye Doctor
Dentist
```

Each Visitor performs a different operation depending on the patient.

```text
Patient
   |
   | accept()
   v
Doctor
   |
   | visit()
   v
Perform Check
```

> **Visitor = Object accepts a visitor, and the visitor performs an operation on it.**

Easy memory:

```text
accept() → Receive Visitor
visit()  → Perform Operation
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Add operations without changing Elements |
| Element | Provides `accept()` |
| Concrete Element | Actual object |
| Visitor | Defines `visit()` |
| Concrete Visitor | Implements operation |
| Key Concept | Double Dispatch |
| Best When | Elements stable, operations change |
| Main Advantage | Easy to add operations |
| Main Disadvantage | Hard to add Element types |
| Easy Trick | Visitor = Visit + Perform Operation |