# Interpreter Design Pattern

## Definition
The **Interpreter Design Pattern** is a **Behavioral Design Pattern** that defines a way to **interpret and evaluate sentences or expressions of a language**.

In simple words, Interpreter breaks an expression into smaller parts, creates objects for those parts, and evaluates them according to defined rules.

> **Interpreter = Define grammar rules and evaluate an expression using those rules.**

---

# Why Interpreter?
Suppose our application needs to evaluate simple expressions:

```text
10 + 20
10 + 20 - 5
```

We can define rules:

```text
Number     → 10, 20, 5
Addition   → +
Subtraction→ -
```

Then build objects representing the expression:

```text
        +
       / \
     10   20
```

Calling:

```java
interpret();
```

returns:

```text
30
```

Interpreter is useful when an application has a **small language or set of rules that needs to be evaluated repeatedly**.

---

# Problem Without Interpreter

```java
String expression = "10 + 20";

String[] values = expression.split(" ");

if (values[1].equals("+")) {
    int result =
        Integer.parseInt(values[0]) +
        Integer.parseInt(values[2]);

    System.out.println(result);
}
```

As more operations are added:

```text
+
-
*
/
AND
OR
```

the code can become:

```java
if (...) {
} else if (...) {
} else if (...) {
} else if (...) {
}
```

Problems:

- Parsing logic becomes complex.
- Many `if/else` conditions.
- Grammar rules are mixed together.
- Difficult to extend.
- Difficult to represent complex expressions.

---

# Solution
Create an object for each grammar rule.

```text
Expression
├── NumberExpression
├── AddExpression
└── SubtractExpression
```

Every expression supports:

```java
interpret();
```

Example:

```text
10 + 20

       Add
      /   \
    10     20
```

The tree evaluates recursively.

---

# Main Components

## 1. Abstract Expression
Defines the common interpretation operation.

```java
interface Expression {
    int interpret();
}
```

## 2. Terminal Expression
Represents the smallest value that cannot be broken down further.

Example:

```text
10
20
5
```

```java
class NumberExpression implements Expression {
    private int number;

    NumberExpression(int number) {
        this.number = number;
    }

    public int interpret() {
        return number;
    }
}
```

## 3. Non-Terminal Expression
Represents a rule that contains other expressions.

Examples:

```text
Addition
Subtraction
```

```java
class AddExpression implements Expression {
    private Expression left;
    private Expression right;

    AddExpression(Expression left, Expression right) {
        this.left = left;
        this.right = right;
    }

    public int interpret() {
        return left.interpret() + right.interpret();
    }
}
```

## 4. Context
Contains information needed while interpreting an expression.

Depending on the problem, it may contain:

```text
Variables
Values
Configuration
Environment
```

## 5. Client
Creates the expression tree and calls:

```java
interpret();
```

---

# Simple Java Example

```java
public class InterpreterExample {

    /**
     * ABSTRACT EXPRESSION
     * Common contract for all expressions.
     */
    interface Expression {
        int interpret();
    }

    /**
     * TERMINAL EXPRESSION
     * Represents a number.
     */
    static class NumberExpression implements Expression {
        private final int number;

        NumberExpression(int number) {
            this.number = number;
        }

        @Override
        public int interpret() {
            return number;
        }
    }

    /**
     * NON-TERMINAL EXPRESSION
     * Represents addition.
     */
    static class AddExpression implements Expression {
        private final Expression left;
        private final Expression right;

        AddExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int interpret() {
            return left.interpret() + right.interpret();
        }
    }

    /**
     * NON-TERMINAL EXPRESSION
     * Represents subtraction.
     */
    static class SubtractExpression implements Expression {
        private final Expression left;
        private final Expression right;

        SubtractExpression(Expression left, Expression right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public int interpret() {
            return left.interpret() - right.interpret();
        }
    }

    /**
     * CLIENT
     * Creates and evaluates the expression:
     *
     * (10 + 20) - 5
     */
    public static void main(String[] args) {
        Expression ten = new NumberExpression(10);
        Expression twenty = new NumberExpression(20);
        Expression five = new NumberExpression(5);

        Expression add = new AddExpression(ten, twenty);
        Expression result = new SubtractExpression(add, five);

        System.out.println(result.interpret());
    }
}
```

Output:

```text
25
```

---

# How the Expression is Built
We want:

```text
(10 + 20) - 5
```

First:

```java
Expression ten = new NumberExpression(10);
Expression twenty = new NumberExpression(20);

Expression add =
    new AddExpression(ten, twenty);
```

This represents:

```text
10 + 20
```

Then:

```java
Expression five = new NumberExpression(5);

Expression result =
    new SubtractExpression(add, five);
```

Now:

```text
(10 + 20) - 5
```

Expression tree:

```text
           -
          / \
         +   5
        / \
       10  20
```

---

# Internal Flow
When:

```java
result.interpret();
```

is called:

```text
Subtract.interpret()
        |
        ├── Add.interpret()
        |      |
        |      ├── 10.interpret() → 10
        |      └── 20.interpret() → 20
        |                |
        |                v
        |               30
        |
        └── 5.interpret() → 5
                 |
                 v
              30 - 5
                 |
                 v
                25
```

This recursive evaluation is an important part of Interpreter Pattern.

---

# Terminal vs Non-Terminal Expression

## Terminal Expression
Represents the smallest unit of the language.

Example:

```text
10
20
true
false
variable
```

Usually it doesn't contain another Expression.

```text
NumberExpression(10)
```

## Non-Terminal Expression
Combines other expressions.

Example:

```text
10 + 20
```

Here:

```text
AddExpression
├── NumberExpression(10)
└── NumberExpression(20)
```

Easy trick:

```text
Terminal     = Value / End
Non-Terminal = Rule / Combination
```

---

# Example With Context
Context becomes useful when expressions contain variables.

Suppose:

```text
x + y
```

and:

```text
x = 10
y = 20
```

Context:

```java
import java.util.HashMap;
import java.util.Map;

class Context {
    private final Map<String, Integer> values = new HashMap<>();

    void set(String name, int value) {
        values.put(name, value);
    }

    int get(String name) {
        return values.get(name);
    }
}
```

Variable expression:

```java
class VariableExpression implements Expression {
    private final String name;
    private final Context context;

    VariableExpression(String name, Context context) {
        this.name = name;
        this.context = context;
    }

    public int interpret() {
        return context.get(name);
    }
}
```

Now:

```text
Context
├── x = 10
└── y = 20

Expression
     |
     v
x + y
     |
     v
30
```

---

# Real-World Software Examples

Interpreter can be useful for small domain-specific languages such as:

```text
Search Rules
Validation Rules
Permission Expressions
Filtering Expressions
Mathematical Expressions
Configuration Rules
```

Example permission rule:

```text
ADMIN AND ACTIVE
```

Expression tree:

```text
        AND
       /   \
    ADMIN  ACTIVE
```

Another example:

```text
age > 18 AND country = "India"
```

The application can interpret the rule against some context.

---

# Advantages

- Grammar rules are separated into classes.
- Easy to add simple expressions.
- Expression trees are easy to represent.
- Supports recursive evaluation.
- Useful for small domain-specific languages.
- Removes large conditional blocks for grammar rules.

---

# Disadvantages

- Creates many expression classes.
- Complex grammar becomes difficult to maintain.
- Expression trees can become large.
- Performance may suffer for complex languages.
- Not suitable for building full programming-language parsers.

---

# When to Use
Use Interpreter when:

- You have a small language or grammar.
- Rules need to be evaluated repeatedly.
- Expressions can be represented as a tree.
- Grammar is simple and stable.

Examples:

```text
Math Expressions
Search Filters
Permission Rules
Validation Rules
Business Rules
Simple Query Languages
```

---

# When Not to Use
Avoid Interpreter when:

- Grammar is very complex.
- Performance is critical.
- You are building a full programming language.
- There are hundreds of grammar rules.
- A parser/compiler library already solves the problem better.

For complex grammar, consider proper parser tools instead of manually implementing Interpreter.

---

# Design Considerations
The core structure is:

```text
              Expression
              /        \
             /          \
       Terminal      Non-Terminal
       Expression     Expression
                         |
                    Expressions
```

For arithmetic:

```text
Expression
├── NumberExpression
├── AddExpression
└── SubtractExpression
```

Non-terminal expressions normally contain:

```text
HAS-A Expression
```

Example:

```java
private Expression left;
private Expression right;
```

---

# Interpreter vs Composite
Both often create tree structures.

```text
Composite   → Represents object hierarchy
Interpreter → Evaluates grammar/expression tree
```

Interpreter often uses a Composite-like tree internally.

Example:

```text
       +
      / \
     10  20
```

---

# Interpreter vs Strategy

```text
Interpreter → Evaluates language/grammar rules
Strategy    → Selects one algorithm/behavior
```

---

# Interpreter vs Chain of Responsibility

```text
Interpreter → Expression tree is evaluated
CoR         → Request passes through handlers
```

---

# Pitfalls

- Don't use Interpreter for very complex grammar.
- Keep each expression responsible for one grammar rule.
- Avoid putting all parsing logic into one expression.
- Separate parsing from interpretation when possible.
- Be careful with very deep recursive expression trees.

---

# Interview Questions

## What is Interpreter Pattern?
Interpreter is a Behavioral Design Pattern that defines grammar rules as objects and evaluates expressions based on those rules.

## Which category does Interpreter belong to?
**Behavioral Design Pattern**

## What are the main components?
- Abstract Expression
- Terminal Expression
- Non-Terminal Expression
- Context
- Client

## What is a Terminal Expression?
A Terminal Expression represents the smallest unit of the grammar.

Example:

```text
NumberExpression(10)
```

## What is a Non-Terminal Expression?
A Non-Terminal Expression combines other expressions according to a grammar rule.

Example:

```text
AddExpression
```

## What is Context?
Context contains external information needed while evaluating expressions.

Example:

```text
x = 10
y = 20
```

## Why does Interpreter often use recursion?
Because expressions are commonly represented as trees containing other expressions.

## Give a simple Interpreter example.
```text
(10 + 20) - 5
```

Expression tree:

```text
      -
     / \
    +   5
   / \
  10  20
```

## Interpreter vs Composite?

```text
Composite   → Build/manage object tree
Interpreter → Evaluate expression tree
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Defines grammar rules as objects.
- Evaluates expressions.
- `Expression` defines `interpret()`.
- Terminal Expression represents a basic value.
- Non-Terminal Expression combines expressions.
- Context provides external values.
- Expressions are often represented as trees.
- Recursive evaluation is common.
- Best suited for simple grammars.

---

# Easy Trick to Remember
Think about a calculator:

```text
10 + 20
```

The Interpreter understands:

```text
10 → Number
+  → Addition Rule
20 → Number
```

Then:

```text
        +
       / \
      10 20
        |
        v
       30
```

> **Interpreter = Understand rules + evaluate expression.**

Easy memory:

```text
Terminal     = Value
Non-Terminal = Rule
Interpret()  = Evaluate
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Interpret and evaluate expressions |
| Abstract Expression | Defines `interpret()` |
| Terminal Expression | Basic value |
| Non-Terminal Expression | Combines expressions |
| Context | Provides external information |
| Structure | Expression Tree |
| Common Example | Mathematical/Rule Expressions |
| Best For | Small grammar/DSL |
| Easy Trick | Interpreter = Rules + Evaluate |