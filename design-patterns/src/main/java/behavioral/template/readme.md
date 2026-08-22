# Template Method Design Pattern

## Definition
The **Template Method Design Pattern** is a **Behavioral Design Pattern** that defines the **overall steps of an algorithm in a parent class**, while allowing child classes to customize some of those steps.

In simple words, the parent class defines **what steps must happen and in what order**, while child classes provide their own implementation for specific steps.

> **Template Method = Fixed workflow + Customizable steps.**

---

# Why Template Method?
Suppose we process different types of files:

```text
CSV File
JSON File
XML File
```

The overall processing flow is always:

```text
1. Read File
2. Parse Data
3. Validate Data
4. Save Data
```

But parsing is different:

```text
CSV  → parseCSV()
JSON → parseJSON()
XML  → parseXML()
```

Instead of duplicating the complete workflow in every class, define the common workflow once in the parent class.

---

# Problem Without Template Method

```java
class CSVProcessor {
    void process() {
        System.out.println("Read file");
        System.out.println("Parse CSV");
        System.out.println("Validate");
        System.out.println("Save");
    }
}

class JSONProcessor {
    void process() {
        System.out.println("Read file");
        System.out.println("Parse JSON");
        System.out.println("Validate");
        System.out.println("Save");
    }
}
```

Duplicate code:

```text
Read
Validate
Save
```

Only this changes:

```text
Parse CSV
Parse JSON
```

Problems:

- Duplicate code
- Same workflow repeated
- Difficult to maintain
- Child classes may execute steps in different order
- Common changes must be made in multiple places

---

# Solution
Create an abstract parent class.

```text
FileProcessor
     |
     | process()
     |
     ├── read()
     ├── parse()      ← Child implements
     ├── validate()
     └── save()
```

Child classes only implement the changing part:

```text
FileProcessor
   /       \
  /         \
CSVProcessor JSONProcessor
     |           |
 parseCSV     parseJSON
```

---

# Main Components

## 1. Abstract Class
Defines the template method and common operations.

Example:

```text
FileProcessor
```

## 2. Template Method
Defines the fixed sequence of steps.

```java
final void process() {
    read();
    parse();
    validate();
    save();
}
```

## 3. Abstract Operations
Steps that child classes must implement.

```java
abstract void parse();
```

## 4. Concrete Class
Implements the customizable steps.

Examples:

```text
CSVProcessor
JSONProcessor
```

## 5. Hook
An optional method that child classes may override.

```java
boolean shouldValidate() {
    return true;
}
```

---

# Simple Java Example

```java
public class TemplateMethodExample {

    /**
     * ABSTRACT CLASS
     * Defines the common file-processing workflow.
     */
    abstract static class FileProcessor {

        /**
         * TEMPLATE METHOD
         *
         * Defines the fixed execution order.
         * final prevents child classes from changing the workflow.
         */
        final void process() {
            read();
            parse();
            validate();
            save();
        }

        /**
         * Common implementation.
         */
        void read() {
            System.out.println("Reading file");
        }

        /**
         * Custom step.
         * Every child must implement this.
         */
        abstract void parse();

        /**
         * Common implementation.
         */
        void validate() {
            System.out.println("Validating data");
        }

        /**
         * Common implementation.
         */
        void save() {
            System.out.println("Saving data");
        }
    }

    /**
     * CONCRETE CLASS
     */
    static class CSVProcessor extends FileProcessor {
        @Override
        void parse() {
            System.out.println("Parsing CSV");
        }
    }

    /**
     * CONCRETE CLASS
     */
    static class JSONProcessor extends FileProcessor {
        @Override
        void parse() {
            System.out.println("Parsing JSON");
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        FileProcessor csv = new CSVProcessor();
        csv.process();

        System.out.println("-----");

        FileProcessor json = new JSONProcessor();
        json.process();
    }
}
```

Output:

```text
Reading file
Parsing CSV
Validating data
Saving data
-----
Reading file
Parsing JSON
Validating data
Saving data
```

---

# Internal Flow
When:

```java
FileProcessor processor = new CSVProcessor();
processor.process();
```

is called:

```text
FileProcessor.process()
       |
       ├── read()
       |
       ├── parse()
       |      |
       |      v
       | CSVProcessor.parse()
       |
       ├── validate()
       |
       └── save()
```

Important:

```text
Parent decides WHEN parse() is called.
Child decides HOW parse() works.
```

---

# Why is Template Method Usually `final`?

```java
final void process() {
    read();
    parse();
    validate();
    save();
}
```

`final` prevents a child class from overriding the complete workflow.

Without `final`, a child could do:

```java
@Override
void process() {
    save();
    read();
}
```

and break the required sequence.

Therefore:

> **Parent controls the algorithm structure.**

---

# Abstract Method
An abstract method has no implementation in the parent:

```java
abstract void parse();
```

Concrete child classes **must implement it**:

```java
class CSVProcessor extends FileProcessor {
    @Override
    void parse() {
        System.out.println("Parsing CSV");
    }
}
```

Remember:

```text
Abstract Step → Child MUST implement
```

---

# Hook Method
A **Hook** is an optional method that provides default behavior.

The child **can override it but doesn't have to**.

Example:

```java
boolean shouldValidate() {
    return true;
}
```

Template:

```java
final void process() {
    read();
    parse();

    if (shouldValidate()) {
        validate();
    }

    save();
}
```

Child can customize:

```java
class CSVProcessor extends FileProcessor {
    void parse() {
        System.out.println("Parsing CSV");
    }

    @Override
    boolean shouldValidate() {
        return false;
    }
}
```

Remember:

```text
Abstract Method → MUST override
Hook Method     → CAN override
```

---

# Real-World Software Example: Data Migration
Suppose different migration jobs follow:

```text
Extract
  ↓
Transform
  ↓
Validate
  ↓
Load
```

Common workflow:

```java
abstract class MigrationJob {

    final void migrate() {
        extract();
        transform();
        validate();
        load();
    }

    void extract() {
        System.out.println("Extracting data");
    }

    abstract void transform();

    void validate() {
        System.out.println("Validating data");
    }

    void load() {
        System.out.println("Loading data");
    }
}
```

Different migrations customize:

```text
MemberMigration
TransactionMigration
AccountMigration
```

but the overall migration workflow remains the same.

---

# Real-World Software Examples

## ETL Processing

```text
Extract
Transform
Validate
Load
```

## File Processing

```text
Read
Parse
Validate
Save
```

## Payment Processing

```text
Validate
Process
Update
Notify
```

## Report Generation

```text
Fetch Data
Process Data
Generate Report
Export
```

## Test Framework

```text
Setup
Execute Test
Cleanup
```

---

# Advantages

- Avoids duplicate code.
- Common workflow is defined once.
- Child classes customize only required steps.
- Ensures execution order.
- Easy to maintain common behavior.
- Supports code reuse.
- Supports Open/Closed Principle.

---

# Disadvantages

- Uses inheritance.
- Child classes are coupled to the parent.
- Too many abstract methods can make subclasses complex.
- Changing the template can affect all child classes.
- Less flexible than composition-based patterns.

---

# When to Use
Use Template Method when:

- Multiple classes follow the same workflow.
- Only some steps are different.
- Execution order must remain fixed.
- Common code is duplicated across subclasses.
- Parent should control the algorithm structure.

Examples:

```text
File Processing
ETL Pipeline
Data Migration
Report Generation
Payment Processing
Test Execution
```

---

# When Not to Use
Avoid Template Method when:

- Algorithms are completely different.
- Workflow needs to change dynamically at runtime.
- You want to avoid inheritance.
- There is very little common behavior.

---

# Design Considerations
Keep common steps in the parent:

```text
read()
validate()
save()
```

Keep variable steps abstract:

```text
parse()
```

Template:

```text
process()
  |
  ├── Common
  ├── Custom
  ├── Common
  └── Common
```

The Template Method should normally control the sequence.

---

# Template Method vs Strategy
This is an important interview question.

Both allow algorithms to vary, but:

```text
Template Method → Inheritance
Strategy        → Composition
```

Template Method:

```text
Parent
  |
  ├── Child A
  └── Child B
```

Strategy:

```text
Context
  |
  | HAS-A
  v
Strategy
```

Easy difference:

```text
Template → Customize steps of a fixed workflow
Strategy → Replace the complete algorithm
```

---

# Template Method vs Factory Method
Template Method defines:

```text
HOW an algorithm/workflow executes
```

Factory Method defines:

```text
HOW an object is created
```

They can also be used together.

---

# Template Method vs State

```text
Template → Fixed workflow with customizable steps
State    → Behavior changes based on current state
```

---

# Pitfalls

- Don't create too many abstract methods.
- Don't put unrelated workflows in one parent class.
- Use `final` for the template method when order must not change.
- Keep common logic in the parent.
- Keep child-specific logic in child classes.
- Avoid deep inheritance hierarchies.

---

# Interview Questions

## What is Template Method Pattern?
Template Method is a Behavioral Design Pattern that defines the structure of an algorithm in a parent class while allowing child classes to customize specific steps.

## Which category does Template Method belong to?
**Behavioral Design Pattern**

## What are the main components?
- Abstract Class
- Template Method
- Abstract Operations
- Concrete Classes
- Optional Hooks

## What is the Template Method?
The method that defines the fixed algorithm sequence.

Example:

```java
final void process() {
    read();
    parse();
    validate();
    save();
}
```

## Why is Template Method often final?
To prevent subclasses from changing the required execution order.

## What is an Abstract Operation?
A step without implementation that concrete subclasses must implement.

```java
abstract void parse();
```

## What is a Hook?
A method with default behavior that subclasses may optionally override.

```java
boolean shouldValidate() {
    return true;
}
```

## Abstract Method vs Hook?

```text
Abstract Method → MUST override
Hook            → MAY override
```

## Template Method vs Strategy?

```text
Template Method → Inheritance + fixed workflow
Strategy        → Composition + replaceable algorithm
```

## Give a real-world example.
ETL processing:

```text
Extract → Transform → Validate → Load
```

Different ETL jobs can customize the `Transform` step while keeping the overall workflow fixed.

---

# Key Points

- Category: **Behavioral Design Pattern**
- Parent defines the workflow.
- Child customizes specific steps.
- Uses inheritance.
- Template Method defines execution order.
- Template Method is commonly `final`.
- Abstract methods must be implemented.
- Hook methods are optional to override.
- Reduces duplicate code.
- Useful when multiple classes share the same workflow.

---

# Easy Trick to Remember
Think about a **recipe**:

```text
1. Prepare ingredients
2. Cook
3. Serve
```

The overall recipe structure stays the same, but different dishes implement the `cook()` step differently.

In software:

```text
Parent
  |
  | Defines Steps
  v
Template Method
  |
  └── Child customizes some steps
```

> **Template Method = Parent defines the recipe, child fills in some steps.**

Easy memory:

```text
Template = Fixed Steps
Child    = Custom Implementation
```

---

# Summary

| Aspect          | Description                                   |
|-----------------|-----------------------------------------------|
| Pattern Type    | Behavioral                                    |
| Purpose         | Define fixed workflow with customizable steps |
| Main Technique  | Inheritance                                   |
| Template Method | Defines execution order                       |
| Abstract Method | Child must implement                          |
| Hook            | Child may override                            |
| Parent          | Controls workflow                             |
| Child           | Customizes steps                              |
| Common Example  | ETL / File Processing                         |
| Easy Trick      | Fixed Workflow + Custom Steps                 |