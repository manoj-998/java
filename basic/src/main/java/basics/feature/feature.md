# Java 8 Notes: Lambda, Functional Interface, Predicate, Function, Default Methods, Streams
---

# 1. Lambda Expression
## Definition
- A **Lambda Expression** is a concise way to implement a **Functional Interface** by providing the implementation of its single abstract method.
- Introduced in **Java 8**, it eliminates the need for anonymous classes, making the code more readable, maintainable, and suitable for functional programming.

```java
interface Calculator {
    int add(int a, int b);
}

public class LambdaDemo {
    public static void main(String[] args) {
        Calculator c = (a, b) -> a + b;

        System.out.println(c.add(10, 20));
    }
}
```
**Use when:** You want to write short implementation instead of anonymous class.
---

# 2. Functional Interface

## Definition
A **functional interface** is an interface that has exactly **one abstract method**.
- A **Functional Interface** is an interface that contains **exactly one abstract method (SAM - Single Abstract Method)** and can have multiple default and static methods.
- It serves as the target type for **Lambda Expressions** and **Method References**, enabling functional programming in Java.

```java
@FunctionalInterface
interface Greeting {
    void sayHello();
}

public class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        Greeting g = () -> System.out.println("Hello Manoj");
        g.sayHello();
    }
}
```
Examples:

```text
Runnable
Callable
Predicate
Function
Consumer
Supplier
Comparator
```

---

# 3. Predicate

## Definition
`Predicate<T>` is a functional interface used to test a condition. It returns `true` or `false`.
- `Predicate<T>` is a built-in functional interface that accepts an object of type **T** and returns a **boolean** value.
- It is primarily used to test conditions, filter data, and validate objects in the Stream API and other functional programming scenarios.


Method:

```java
boolean test(T t)
```

```java
import java.util.function.Predicate;

public class PredicateDemo {
    public static void main(String[] args) {
        Predicate<Integer> isEven = n -> n % 2 == 0;

        System.out.println(isEven.test(10)); // true
        System.out.println(isEven.test(7));  // false
    }
}
```
**Use when:** Filtering or validating data.
---

# 4. Predicate Joins

## Definition
Predicate joins are used to combine multiple conditions using:
- Predicate joins allow multiple predicates to be combined into a single condition using methods like `and()`, `or()`, and `negate()`.
- They help build complex filtering logic while keeping the code modular, readable, and reusable.


```text
and()
or()
negate()
```

```java
import java.util.function.Predicate;

public class PredicateJoinDemo {
    public static void main(String[] args) {
        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isGreaterThan10 = n -> n > 10;

        System.out.println(isEven.and(isGreaterThan10).test(12)); // true
        System.out.println(isEven.or(isGreaterThan10).test(9));   // false
        System.out.println(isEven.negate().test(7));              // true
    }
}
```

---

# 5. Function

## Definition
`Function<T, R>` is a functional interface that takes one input and returns one output.
- `Function<T, R>` is a built-in functional interface that accepts an input of type **T** and returns an output of type **R**.
- It is commonly used to transform, map, or convert one object into another in functional programming and Stream operations.

Method:
```java
R apply(T t)
```

```java
import java.util.function.Function;

public class FunctionDemo {
    public static void main(String[] args) {
        Function<String, Integer> length = str -> str.length();
        System.out.println(length.apply("Java")); // 4
    }
}
```
**Use when:** You want to transform one value into another.

Example:
```text
String -> Integer
Employee -> EmployeeDTO
User -> UserResponse
```

---


# 6. Default Method in Interface

## Definition
A **default method** is a method with implementation inside an interface.
- A **default method** is a method with an implementation inside an interface, introduced in Java 8.
- It allows developers to add new functionality to interfaces without breaking the existing classes that implement them.


```java
interface Vehicle {
    default void start() {
        System.out.println("Vehicle started");
    }
}

public class Car implements Vehicle {
    public static void main(String[] args) {
        Car car = new Car();
        car.start();
    }
}
```

**Use when:** You want to add new behavior to an interface without breaking existing classes.
---


# 7. Class Implementing Two Interfaces with Same Default Method

## Definition
If a class implements two interfaces with the same default method, the class must override that method.
- When a class implements two interfaces containing the same default method, Java cannot decide which implementation to inherit.
- To resolve this ambiguity, the implementing class must override the method and may explicitly invoke a specific interface's default implementation using `InterfaceName.super.method()`.


```java
interface A {
    default void show() {System.out.println("A show");}
}
interface B {
    default void show() { System.out.println("B show"   );
    }
}

public class DefaultMethodConflictDemo implements A, B {

    @Override
    public void show() {
        System.out.println("Class show");
        // Optional: call specific interface method
        A.super.show();
        B.super.show();
    }

    public static void main(String[] args) {
        DefaultMethodConflictDemo obj = new DefaultMethodConflictDemo();
        obj.show();
    }
}
```

Output:
```text
Class show
A show
B show
```

---


# 8. Stream API

## Definition
A **Stream** is used to process collections in a functional style.
- The **Stream API** is a Java 8 feature used to process collections of data in a declarative and functional style.
- It supports operations such as filtering, mapping, sorting, grouping, and collecting data efficiently without modifying the original collection.

Stream does not modify the original collection.

```java
list.stream()
```

---

# 9. Common Stream Methods
| Method        | Purpose                             |
|---------------|-------------------------------------|
| `filter()`    | Selects elements based on condition |
| `map()`       | Converts/transforms elements        |
| `collect()`   | Collects result into List/Set/Map   |
| `forEach()`   | Iterates each element               |
| `sorted()`    | Sorts elements                      |
| `distinct()`  | Removes duplicates                  |
| `limit()`     | Limits number of elements           |
| `skip()`      | Skips first N elements              |
| `count()`     | Counts elements                     |
| `findFirst()` | Returns first element               |
| `anyMatch()`  | Checks if any element matches       |
| `allMatch()`  | Checks if all elements match        |
| `noneMatch()` | Checks if no elements match         |

---

# 10. Stream filter()

## Definition
`filter()` is used to select elements that match a condition.
- `filter()` is an intermediate Stream operation used to select elements that satisfy a given condition represented by a `Predicate`.
- It returns a new stream containing only the elements that match the specified condition.


```java
import java.util.*;
import java.util.stream.Collectors;

public class StreamFilterDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(5, 10, 15, 20, 25);

        List<Integer> result = numbers.stream()
                .filter(n -> n > 10)
                .collect(Collectors.toList());

        System.out.println(result);
    }
}
```

Output:

```text
[15, 20, 25]
```

---

# 11. Stream map()

## Definition
`map()` is used to transform each element into another value.
- `map()` is an intermediate Stream operation used to transform each element of a stream into another object or value.
- It applies a `Function` to every element and returns a new stream containing the transformed results.


```java
import java.util.*;
import java.util.stream.Collectors;

public class StreamMapDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("java", "spring", "boot");

        List<String> upperCaseNames = names.stream()
                .map(name -> name.toUpperCase())
                .collect(Collectors.toList());

        System.out.println(upperCaseNames);
    }
}
```

Output:

```text
[JAVA, SPRING, BOOT]
```

---

# 12. Stream filter() + map() Together

```java
import java.util.*;
import java.util.stream.Collectors;

public class StreamFilterMapDemo {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Manoj", "Java", "Spring", "Boot");

        List<String> result = names.stream()
                .filter(name -> name.length() > 4)
                .map(name -> name.toUpperCase())
                .collect(Collectors.toList());

        System.out.println(result);
    }
}
```

Output:

```text
[MANOJ, SPRING]
```

---

# Complete Example with Employee

```java
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class Java8CompleteDemo {

    public static void main(String[] args) {
        List<Employee> employees = Arrays.asList(
                new Employee(1, "Manoj", 75000),
                new Employee(2, "Rahul", 45000),
                new Employee(3, "Sneha", 90000),
                new Employee(4, "Amit", 30000)
        );

        Predicate<Employee> highSalary = emp -> emp.salary > 50000;

        Function<Employee, String> employeeName = emp -> emp.name;

        List<String> names = employees.stream()
                .filter(highSalary)
                .map(employeeName)
                .collect(Collectors.toList());

        System.out.println(names);
    }

    static class Employee {
        int id;
        String name;
        double salary;

        Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }
    }
}
```

Output:

```text
[Manoj, Sneha]
```
---

# 11. Stream Methods

## Definition
- Stream methods are operations used to process, transform, and aggregate data within a stream pipeline.
- They are categorized into 
  - **Intermediate Operations** (such as `filter()`, `map()`, `sorted()`) that return another stream,
  - **Terminal Operations** (such as `collect()`, `forEach()`, `count()`) that produce the final result.

# Quick Revision

```text
Lambda → Short implementation of functional interface
Functional Interface → Interface with one abstract method
Predicate → Condition → Returns boolean
Predicate Join → and(), or(), negate()
Function → Converts input to output
Default Method → Method with body inside interface
Same Default Method Conflict → Class must override the method
Stream → Process collection data
filter() → Select data
map() → Transform data
```


# Java 9+ Enhancements

## 1. Private Methods in Interface

### Definition
* Java 9 introduced **private methods inside interfaces** to reuse common code between `default` and `static` methods.
* Private interface methods cannot be accessed or overridden by implementing classes.

```java
interface PaymentService {
    default void pay() {
        validate();
        System.out.println("Payment completed");
    }
    static void refund() {
        log();
        System.out.println("Refund completed");
    }
    private void validate() {
        System.out.println("Validating payment");
    }

    private static void log() {
        System.out.println("Logging transaction");
    }
}
```

### Key Points

* `private` method → Called by default methods.
* `private static` method → Called by static or default methods.
* Cannot be abstract.
* Cannot be called from implementing classes.

---

## 2. Immutable Collections

### Definition

* Immutable collections cannot be modified after creation.
* Java 9 introduced factory methods such as `List.of()`, `Set.of()`, and `Map.of()`.

```java
List<String> names = List.of("Manoj", "Rahul", "Sneha");

Set<Integer> numbers = Set.of(10, 20, 30);

Map<Integer, String> employees =
        Map.of(1, "Manoj", 2, "Rahul");
```

Modification is not allowed:

```java
names.add("Amit"); // UnsupportedOperationException
```

### Key Points

* Cannot add, remove, or update elements.
* `null` values are not allowed.
* Duplicate elements are not allowed in `Set.of()`.
* Duplicate keys are not allowed in `Map.of()`.

---

## 3. Stream API Updates

Java 9 added useful methods to the Stream API.

### `takeWhile()`

Takes elements while the condition remains `true`.

```java
List<Integer> numbers = List.of(2, 4, 6, 7, 8, 10);

numbers.stream()
       .takeWhile(n -> n % 2 == 0)
       .forEach(System.out::println);
```

Output:

```text
2
4
6
```

---

### `dropWhile()`

Skips elements while the condition remains `true`, then returns the remaining elements.

```java
numbers.stream()
       .dropWhile(n -> n % 2 == 0)
       .forEach(System.out::println);
```

Output:

```text
7
8
10
```

---

### `Stream.ofNullable()`

Creates an empty stream if the value is `null`; otherwise creates a stream with one element.

```java
String name = null;

Stream.ofNullable(name)
      .forEach(System.out::println);
```

This avoids manual null checking.

---

### `Stream.iterate()` with Condition

Java 9 added a termination condition to `iterate()`.

```java
Stream.iterate(1, n -> n <= 5, n -> n + 1)
      .forEach(System.out::println);
```

Output:

```text
1
2
3
4
5
```

---

## 4. Try-With-Resources Enhancement

### Definition

* Try-with-resources automatically closes resources after execution.
* From Java 9, an already declared `final` or effectively final resource can be used directly inside `try()`.

### Before Java 9

```java
BufferedReader reader =
        new BufferedReader(new FileReader("data.txt"));

try (BufferedReader br = reader) {
    System.out.println(br.readLine());
}
```

### Java 9+

```java
BufferedReader reader =
        new BufferedReader(new FileReader("data.txt"));

try (reader) {
    System.out.println(reader.readLine());
}
```

### Key Points

* Resource must be `final` or effectively final.
* Resources are closed automatically.
* Reduces duplicate variable declarations.
* Commonly used with files, streams, sockets, and database connections.

---

# Quick Revision

```text
Private Interface Method
→ Reuse code inside interface
→ Introduced in Java 9

Immutable Collection
→ List.of(), Set.of(), Map.of()
→ Cannot modify after creation

Stream Updates
→ takeWhile()
→ dropWhile()
→ ofNullable()
→ iterate() with condition

Try-With-Resources Enhancement
→ Reuse an already declared resource directly
→ Resource must be final or effectively final
```


# Java 10 features
---

# 1. `var` (Local Variable Type Inference)
## Definition
* `var` allows the Java compiler to **automatically infer the type of a local variable** from its initializer.
* It improves code readability by reducing verbosity while maintaining **static typing**.
> **Note:** `var` was introduced in **Java 10** and is fully available in Java 11.

### Example
```java
var name = "Manoj";          // String
var age = 25;                // int
var employees = List.of("A", "B");
```
Equivalent to:
```java
String name = "Manoj";
int age = 25
List<String> employees = List.of("A", "B");
```

### Rules
```java
var number = 10;     // ✔
var name = "Java";   // ✔
var obj = new Employee(); // ✔
var x;               // ❌ No initializer
var value = null;    // ❌ Type cannot be inferred
```

### Use When
* Local variables
* Loop variables
* Try-with-resources

### Cannot Use
* Method parameters
* Return types
* Instance variables
* Static variables
---

# Unmodifiable Collections (Java)

## Definition
- An **unmodifiable collection** is a collection whose elements **cannot be added, removed, or updated** after creation.
- Any attempt to modify it throws an **`UnsupportedOperationException`**.
---
# Java 9 Factory Methods
```java
Map<Integer, String> map = Stream.of("Java", "Spring")
        .collect(Collectors.toUnmodifiableMap(
                String::length,
                s -> s
        ));
```
---

# Modification

```java
list.add("Docker");      // ❌ UnsupportedOperationException
list.remove("Java");     // ❌ UnsupportedOperationException
list.set(0, "Python");   // ❌ UnsupportedOperationException
```
---

# CopyOf() (Java 10)

Creates an **unmodifiable copy** of an existing collection.

```java
List<String> list = new ArrayList<>();

list.add("Java");
list.add("Spring");

List<String> immutable = List.copyOf(list);
```
Modifying the original list does **not** affect the copied collection.
---

# Characteristics

- Cannot add elements.
- Cannot remove elements.
- Cannot update elements.
- Thread-safe for read-only access (because contents never change).
- `List.of()`, `Set.of()`, and `Map.of()` do **not** allow `null` elements (or `null` keys/values for maps).

---

# Mutable vs Unmodifiable

| Mutable Collection    | Unmodifiable Collection   |
|-----------------------|---------------------------|
| Can add/remove/update | Cannot modify             |
| `ArrayList`           | `List.of()`               |
| `HashSet`             | `Set.of()`                |
| `HashMap`             | `Map.of()`                |

---

# Interview Questions

### What is an unmodifiable collection?
An unmodifiable collection is a collection whose contents cannot be changed after creation.

### Difference between immutable and unmodifiable?
- **Unmodifiable:** You cannot modify it through that reference.
- **Immutable:** The object's state can never change after creation.
---

# Quick Revision

```text
Unmodifiable Collection

✔ Read Only
✔ Cannot Add
✔ Cannot Remove
✔ Cannot Update

Methods

List.of()
Set.of()
Map.of()
List.copyOf()
Set.copyOf()
Map.copyOf()
```


# Java 11 Updates

---

# 1. String API Updates
Java 11 introduced several useful methods in the `String` class.
---

## `isBlank()`
### Definition
- Returns `true` if the string is empty or contains only whitespace characters.
- Unlike `isEmpty()`, it ignores spaces, tabs, and new lines.

```java
String s1 = "";
String s2 = "   ";

System.out.println(s1.isBlank()); // true
System.out.println(s2.isBlank()); // true
```

---

## `lines()`
- Splits a string into a **Stream of lines**.
- Useful for processing multi-line text.

```java
String text = "Java\nSpring\nKafka";
text.lines()
    .forEach(System.out::println);
```
---

## `strip()`
- Removes leading and trailing **Unicode whitespace**.
- Better than `trim()` because it supports Unicode characters.
```java
String str = "   Java   ";
System.out.println(str.strip());
```
Output
```text
Java
```
---

## `stripLeading()`
- Removes whitespace only from the beginning of the string.

```java
String str = "   Java";
System.out.println(str.stripLeading());
```
---

## `stripTrailing()`
- Removes whitespace only from the end of the string.
```java
String str = "Java   ";
System.out.println(str.stripTrailing());
```
---

## `repeat()`

### Definition

- Returns a new string repeated the specified number of times.

```java
System.out.println("Java ".repeat(3));
```

Output

```text
Java Java Java
```

---

# 2. File API Updates

Java 11 introduced convenient methods to read and write files.

---
## `Files.readString()`
- Reads the entire file content into a `String`.
```java
String content = Files.readString(Path.of("data.txt"));
System.out.println(content);
```
---

## `Files.writeString()`
- Writes a `String` directly to a file.
```java
Files.writeString(
        Path.of("data.txt"),
        "Hello Java 11");
```
---

# 3. `isEmpty()` Method
- Returns `true` if the string length is **0**.
- It does **not** ignore whitespace.

```java
String s1 = "";
String s2 = "   ";

System.out.println(s1.isEmpty()); // true
System.out.println(s2.isEmpty()); // false
```
---

# `isEmpty()` vs `isBlank()`

| Method      | Empty String   | Spaces Only |
|-------------|----------------|-------------|
| `isEmpty()` | ✅ true         | ❌ false |
| `isBlank()` | ✅ true         | ✅ true |

Example

```java
String s1 = "";
String s2 = "   ";

System.out.println(s1.isEmpty()); // true
System.out.println(s2.isEmpty()); // false

System.out.println(s1.isBlank()); // true
System.out.println(s2.isBlank()); // true
```

---

# Quick Revision

```text
Java 11 String API

✔ isBlank()
✔ lines()
✔ strip()
✔ stripLeading()
✔ stripTrailing()
✔ repeat()

File API

✔ Files.readString()
✔ Files.writeString()

Difference
isEmpty() → Checks length == 0
isBlank() → Checks empty or whitespace
```


# Java 12 Updates

---
# 1. `String.indent()`
- `indent()` adds or removes leading spaces from every line of a string.
- A positive value adds spaces, while a negative value removes spaces.

### Example
```java
String text = "Java\nSpring";
System.out.println(text.indent(4));
```
Output
```text
    Java
    Spring
```

---

# 2. `String.transform()`
- `transform()` applies a function to a string and returns the transformed result.
- It improves readability by allowing method chaining with custom logic.

### Example
```java
String result = "java".transform(String::toUpperCase);
System.out.println(result);
```
Output
```text
JAVA
```
---

# 3. Compact Number Formatting
- Java 12 introduced `NumberFormat.getCompactNumberInstance()` to display large numbers in a compact, human-readable format.
- Useful for dashboards, reports, and financial applications.

### Example
```java
import java.text.NumberFormat;
import java.util.Locale;

NumberFormat format =
        NumberFormat.getCompactNumberInstance(
                Locale.US,
                NumberFormat.Style.SHORT);

System.out.println(format.format(1200));
System.out.println(format.format(2500000));
```
Output
```text
1K
2M
```
---

# 4. More Unicode Support
- Java 12 includes support for newer versions of the Unicode standard.
- This improves handling of newer languages, symbols, and emoji characters.

### Example
```java
String emoji = "😊";

System.out.println(emoji);
```
---

# 5. Collection API Update - `Collectors.teeing()`
- `Collectors.teeing()` allows collecting a stream using **two collectors simultaneously** and combining their results into one.
- Introduced to avoid traversing the stream multiple times.

### Example
```java
import java.util.List;
import java.util.stream.Collectors;

public class TeeingDemo {

    public static void main(String[] args) {

        List<Integer> numbers = List.of(10, 20, 30, 40);

        Double average = numbers.stream()
                .collect(Collectors.teeing(
                        Collectors.summingInt(Integer::intValue),
                        Collectors.counting(),
                        (sum, count) -> (double) sum / count
                ));
        System.out.println("Average : " + average);
    }
}
```
Output
```text
Average : 25.0
```
---

# Quick Revision

```text
Java 12

String API
✔ indent()
✔ transform()

Number API
✔ Compact Number Formatting

Unicode
✔ Latest Unicode support

Collection API
✔ Collectors.teeing()
→ Two collectors
→ One stream traversal
→ Combined result
```




# Java 13 & 14 Updates
---

# 1. Switch Expression (Java 14)
- A **Switch Expression** allows `switch` to return a value, making the code more concise and readable.
- It supports the **`->` syntax** and eliminates the need for multiple `break` statements.

### Example

```java
public class SwitchExpressionDemo {
    public static void main(String[] args) {
        String day = "MONDAY";
        String type = switch (day) {
            case "SATURDAY", "SUNDAY" -> "Weekend";
            case "MONDAY", "TUESDAY",
                    "WEDNESDAY",
                    "THURSDAY",
                    "FRIDAY" -> "Weekday";
            default -> "Invalid";
        };
        System.out.println(type);
    }
}
```

Output
```text
Weekday
```
---

# 2. Helpful NullPointerException (Java 14)
- Java 14 provides **detailed NullPointerException messages**, making debugging much easier.
- The exception clearly identifies **which object reference is null**.

### Example
```java
class Employee {
    Address address;
}

class Address {
    String city;
}

public class Demo {
    public static void main(String[] args) {
        Employee emp = new Employee();
        System.out.println(emp.address.city);
    }
}
```
Old Message

```text
NullPointerException
```
Java 14 Message
```text
Cannot read field "city"
because "emp.address" is null
```
---

# 3. Records (Java 14 - Preview)
- A **Record** is a special class used to represent **immutable data objects**.
- The compiler automatically generates constructors, getters, `equals()`, `hashCode()`, and `toString()`.

### Example

```java
record Employee(
        int id,
        String name,
        double salary) {
}

public class RecordDemo {

    public static void main(String[] args) {

        Employee emp =
                new Employee(101, "Manoj", 80000);

        System.out.println(emp.id());
        System.out.println(emp.name());
        System.out.println(emp);
    }
}
```

Output

```text
101
Manoj
Employee[id=101, name=Manoj, salary=80000.0]
```

---

# 4. Pattern Matching for `instanceof`
> **Note:** This feature became available as a preview in **Java 14** and was finalized in **Java 16**.
- Pattern matching combines the **type check** and **type cast** into a single operation.
- It removes the need for explicit casting after an `instanceof` check.

### Before
```java
Object obj = "Java";
if (obj instanceof String) {
    String str = (String) obj;
    System.out.println(str.length());
}
```

### After
```java
Object obj = "Java";
if (obj instanceof String str) {
    System.out.println(str.length());
}
```
Output
```text
4
```

---

# Quick Revision

```text
Java 13/14

✔ Switch Expression
→ Returns value
→ Uses ->

✔ Helpful NullPointerException
→ Better error messages
→ Easier debugging

✔ Records
→ Immutable data class
→ Auto constructor, getters,
  equals(), hashCode(), toString()

✔ Pattern Matching (instanceof)
→ instanceof + cast together
→ Less boilerplate
```



# Java 15 Updates
---

# 1. Sealed Classes (Preview)
- A **Sealed Class** restricts which classes or interfaces are allowed to extend or implement it.
- It provides better control over inheritance by explicitly declaring the permitted subclasses.

### Example
```java
public sealed class Vehicle
        permits Car, Bike {
}

final class Car extends Vehicle {
}

final class Bike extends Vehicle {
}
```

### Benefits

- Restricts inheritance.
- Improves code security and maintainability.
- Enables exhaustive pattern matching in future Java versions.
---

# 2. Records (Second Preview)

> **Note:** Records were introduced as a preview in Java 14, 
> received a second preview in Java 15, and became a standard feature in Java 16.
- A **Record** is a special class designed to hold immutable data.
- The compiler automatically generates the constructor, accessor methods, `equals()`, `hashCode()`, and `toString()`.

### Example
```java
record Employee(
        int id,
        String name,
        double salary) {
}

public class RecordDemo {

    public static void main(String[] args) {
        Employee emp = new Employee(101, "Manoj", 85000);
        System.out.println(emp.id());
        System.out.println(emp.name());
        System.out.println(emp.salary());
        System.out.println(emp);
    }
}
```

### Output

```text
101
Manoj
85000.0
Employee[id=101, name=Manoj, salary=85000.0]
```

### Benefits

- Less boilerplate code.
- Immutable by default.
- Ideal for DTOs, request/response objects, and value objects.

---

# Quick Revision

```text
Java 15

✔ Sealed Classes (Preview)
→ Restrict inheritance
→ permits keyword

✔ Records (Second Preview)
→ Immutable data carrier
→ Auto constructor
→ Auto getters
→ Auto equals()
→ Auto hashCode()
→ Auto toString()
```


# Java 16 Features
---

# 1. Records (Standard Feature)
- **Records** became a standard feature in Java 16 for creating **immutable data classes**.
- The compiler automatically generates the constructor, accessors, `equals()`, `hashCode()`, and `toString()`.
```java
record Employee(
        int id,
        String name,
        double salary) {
}

public class RecordDemo {
    public static void main(String[] args) {
        Employee emp = new Employee(101, "Manoj", 80000);
        System.out.println(emp.id());
        System.out.println(emp.name());
        System.out.println(emp.salary());
        System.out.println(emp);
    }
}
```
---

# 2. Pattern Matching for `instanceof`
- Pattern matching simplifies the `instanceof` operator by combining **type checking and type casting** into a single step.
- Eliminates explicit casting and improves code readability.
### Example
```java
Object obj = "Java";
if (obj instanceof String str) {
    System.out.println(str.toUpperCase());
}
```
---

# 3. Stream API - `toList()`

## Definition

- Java 16 introduced the `Stream.toList()` method as a simpler way to collect stream elements into an **unmodifiable list**.
- It replaces the common `collect(Collectors.toList())` syntax in many cases.

### Example

```java
List<String> names = List.of(
        "java",
        "spring",
        "kafka");

List<String> result = names.stream()
        .map(String::toUpperCase)
        .toList();

System.out.println(result);
```

---

# 4. Sealed Classes (Second Preview)
- **Sealed Classes** received a second preview in Java 16 before becoming a standard feature in Java 17.
- They restrict inheritance by specifying which classes are permitted to extend them.

### Example

```java
public sealed class Vehicle permits Car, Bike { }

final class Car extends Vehicle { }

final class Bike extends Vehicle { }
```
---

# Quick Revision

```text
Java 16

✔ Records (Standard)
→ Immutable Data Class
✔ Pattern Matching for instanceof
→ Type Check + Cast Together
✔ Stream.toList()
→ Unmodifiable List
✔ Sealed Classes (2nd Preview)
→ Restrict Inheritance
```



# Java 17 Features (LTS)
Java 17 is a **Long-Term Support (LTS)** release that introduced several language and API improvements focused on readability, safety, and performance.
---

# 1. Sealed Classes (Final)

## Definition
- **Sealed Classes** restrict which classes or interfaces can extend or implement them.
- They provide better control over inheritance using the `permits` keyword.

### Example
```java
public sealed class Vehicle permits Car, Bike {
}
```
---

# 2. Pattern Matching for `instanceof` (Final)

## Definition

- Combines **type checking** and **type casting** into a single statement.
- Eliminates explicit casting after `instanceof`.

### Example
```java
Object obj = "Java";
if (obj instanceof String str) {
    System.out.println(str.length());
}
```
---

# 3. Records (Final)

- A **Record** is a special class used to represent immutable data.
- The compiler automatically generates the constructor, getters, `equals()`, `hashCode()`, and `toString()`.

### Example
```java
record Employee(int id, String name) { }
```
---

# 4. Enhanced Random Number Generator
## Definition
- Java 17 introduces the **RandomGenerator** interface and new random number generator implementations.
- It provides better performance and flexibility than the traditional `Random` class.

### Example
```java
RandomGenerator random = RandomGenerator.getDefault();
System.out.println(random.nextInt(100));
```
---

# 5. Foreign Function & Memory API (Incubator)
## Definition
- Allows Java programs to interact with native code and native memory without using JNI.
- Simplifies integration with C/C++ libraries.
---


# 6. Strong Encapsulation of JDK Internals
## Definition
- Most internal JDK APIs are strongly encapsulated by default.
- Applications should use supported public APIs instead of internal JDK classes.
---

# 7. Deprecation of Security Manager

## Definition
- The **Security Manager** is deprecated for future removal.
- Modern containerization and OS-level security have largely replaced its use.
---

# 8. New macOS Rendering Pipeline
- Java 17 replaces the deprecated OpenGL rendering pipeline with the Apple Metal rendering API.
- Improves graphics performance on macOS.
---

# Interview Highlights ⭐
For Java backend interviews, these are the most important Java 17 features:
- ✔ Records
- ✔ Sealed Classes
- ✔ Pattern Matching for `instanceof`
---

# Quick Revision
```text
Java 17 (LTS)
✔ Records (Final)
✔ Sealed Classes (Final)
✔ Pattern Matching for instanceof (Final)
✔ RandomGenerator API
✔ Foreign Function & Memory API
✔ Strong Encapsulation
✔ Security Manager Deprecated
✔ Apple Metal Rendering

Interview Focus
Records
Sealed Classes
Pattern Matching
```


# Java 18 Features

Java 18 is a **non-LTS** release focused on improving developer productivity, performance, and simplifying native interoperability.
---

# 1. Simple Web Server

- Java 18 introduced a **lightweight HTTP file server** for quickly serving static files.
- It is intended for **development, testing, and learning**, not production use.

### Start Server
```bash
jwebserver
```

Default URL
```text
http://localhost:8000
```
---

# 2. UTF-8 by Default
- Java 18 uses **UTF-8 as the default character encoding** across all operating systems.
- This ensures consistent text handling and avoids platform-specific encoding issues.

### Example
```java
String text = "Hello Java";
```
No need to specify encoding explicitly in most cases.
---

# 3. Code Snippets in JavaDoc
- JavaDoc now supports the `@snippet` tag for embedding formatted and validated code examples directly in documentation.
- It improves readability and documentation quality.
### Example

```java
/**
 * Example:
 * {@snippet :
 * System.out.println("Hello Java");
 * }
 */
```
---

# 4. Internet Address Resolution SPI
- Introduced a **Service Provider Interface (SPI)** for hostname and IP address resolution.
- Allows developers to plug in custom DNS or address resolution mechanisms.
---

# 5. Foreign Function & Memory API (Second Incubator)
- Continued improvements to the Foreign Function & Memory API.
- Enables Java to interact with native libraries without using JNI.

---

# 6. Vector API (Third Incubator)
- Introduced further enhancements to the **Vector API** for performing vector computations.
- Improves performance for mathematical, AI, graphics, and scientific applications by utilizing modern CPU instructions.
---

# Interview Highlights ⭐

For backend interviews, the most commonly discussed Java 18 features are:

- ✔ Simple Web Server (`jwebserver`)
- ✔ UTF-8 as Default Charset
- ✔ JavaDoc `@snippet`

---

# Quick Revision

```text
Java 18

✔ Simple Web Server (jwebserver)
✔ UTF-8 Default Charset
✔ JavaDoc @snippet
✔ Internet Address Resolution SPI
✔ Foreign Function & Memory API
✔ Vector API

Interview Focus

• jwebserver
• UTF-8 Default
• JavaDoc Snippets
```



# Java 19 Features

Java 19 is a **non-LTS** release that introduced several preview and incubator features, with a major focus on **concurrency**, **performance**, and **structured programming**.

---

# 1. Virtual Threads (Preview)
- **Virtual Threads** are lightweight threads managed by the JVM instead of the operating system.
- They enable applications to create **millions of concurrent threads** with very low memory overhead.

### Example
```java
Thread.startVirtualThread(() -> {
    System.out.println("Hello Virtual Thread");
});
```

### Benefits

- Lightweight
- Better scalability
- Ideal for I/O-intensive applications

---

# 2. Structured Concurrency (Incubator)

## Definition

- **Structured Concurrency** treats multiple related threads as a single unit of work.
- It simplifies task management, cancellation, and error handling.

### Example

```java
try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {

    // Execute related tasks

}
```

---

# 3. Record Patterns (Preview)

## Definition

- **Record Patterns** allow direct extraction of values from record objects while performing pattern matching.
- Reduces boilerplate code when working with records.

### Example

```java
record Employee(int id, String name) {}

Object obj = new Employee(101, "Manoj");

if (obj instanceof Employee(int id, String name)) {
    System.out.println(name);
}
```

---

# 4. Pattern Matching for `switch` (Preview)
## Definition
- Extends the `switch` statement to support **type patterns** and more expressive matching.
- Reduces the need for multiple `instanceof` checks.

### Example

```java
Object obj = "Java";

switch (obj) {
        case String s ->
        System.out.println(s.toUpperCase());
        case Integer i ->
        System.out.println(i * 2);
default ->
        System.out.println("Unknown");
}
```
---

# 5. Foreign Function & Memory API (Preview)
- Further improvements to the API for calling native libraries and managing native memory.
- Provides a modern alternative to JNI.
---

# Interview Highlights ⭐
For backend interviews, the most important Java 19 features are:

- ✔ Virtual Threads
- ✔ Pattern Matching for `switch`
- ✔ Record Patterns
- ✔ Structured Concurrency
---

# Quick Revision
```text
Java 19
✔ Virtual Threads (Preview)
✔ Structured Concurrency (Incubator)
✔ Record Patterns (Preview)
✔ Pattern Matching for switch (Preview)
✔ Foreign Function & Memory API

Interview Focus

• Virtual Threads
• Pattern Matching
• Record Patterns
• Structured Concurrency
```



# Java 20 Features

> **Note:** Java 20 is a **non-LTS** release. Most features were **preview** or **incubator** features and became stable in later Java versions.

---

# 1. Virtual Threads (2nd Preview)

## Definition

- **Virtual Threads** are lightweight threads managed by the JVM instead of the operating system.
- They enable applications to handle millions of concurrent tasks with minimal memory overhead.

### Example

```java
Thread.startVirtualThread(() -> {
    System.out.println("Hello Virtual Thread");
});
```

---

# 2. Structured Concurrency (2nd Incubator)

## Definition

- **Structured Concurrency** treats multiple concurrent tasks as a single unit of work.
- It simplifies error handling, cancellation, and lifecycle management of concurrent tasks.

### Example

```java
try (var scope =
        new StructuredTaskScope.ShutdownOnFailure()) {

    var task1 = scope.fork(() -> "User");
    var task2 = scope.fork(() -> "Orders");

    scope.join();
    scope.throwIfFailed();

    System.out.println(task1.get());
    System.out.println(task2.get());
}
```

---

# 3. Scoped Values (Incubator)

## Definition

- **Scoped Values** provide a safe and efficient way to share immutable data between methods and threads.
- They are intended as a modern alternative to `ThreadLocal` for read-only contextual data.
```java
static final ScopedValue<String> USER = ScopedValue.newInstance();
```
---

# 4. Record Patterns (2nd Preview)

## Definition

- **Record Patterns** allow records to be destructured directly into their components.
- This reduces boilerplate when extracting values from records.

### Example

```java
record Employee(int id, String name) {}

Object obj = new Employee(101, "Manoj");

if (obj instanceof Employee(int id, String name)) {
    System.out.println(id);
    System.out.println(name);
}
```

---

# 5. Pattern Matching for switch (4th Preview)

## Definition

- Extends the `switch` statement to support type patterns and guarded cases.
- Eliminates long chains of `instanceof` and casting.

### Example

```java
Object obj = "Java";

switch (obj) {

    case String s ->
            System.out.println(s.toUpperCase());

    case Integer i ->
            System.out.println(i * 2);

    default ->
            System.out.println("Unknown");
}
```

---

# Quick Revision

```text
Java 20

✔ Virtual Threads (2nd Preview)
→ Lightweight Threads

✔ Structured Concurrency
→ Group concurrent tasks

✔ Scoped Values
→ Alternative to ThreadLocal

✔ Record Patterns
→ Destructure Records
✔ Pattern Matching for switch
→ Type-based switch
```



# Java 21 Features (LTS)
> **Java 21** is a **Long-Term Support (LTS)** release with several important features that improve concurrency, pattern matching, and language usability.
---

# 1. Virtual Threads
- **Virtual Threads** are lightweight threads managed by the JVM instead of the operating system.
- They enable applications to create **millions of concurrent threads** with minimal memory overhead.

### Example

```java
Thread.startVirtualThread(() -> {
    System.out.println("Hello Virtual Thread");
});
```

---

# 2. Structured Concurrency (Preview)
- **Structured Concurrency** treats multiple concurrent tasks as a single unit of work.
- If one task fails, the remaining tasks can be cancelled automatically, simplifying concurrent programming.

### Example

```java
try (var scope =
        new StructuredTaskScope.ShutdownOnFailure()) {

    var user = scope.fork(() -> "User");
    var order = scope.fork(() -> "Order");

    scope.join();
    scope.throwIfFailed();

    System.out.println(user.get());
    System.out.println(order.get());
}
```

---

# 3. Scoped Values (Preview)

## Definition

- **Scoped Values** provide a safe and efficient way to share immutable data between methods and threads.
- They are intended as a modern replacement for `ThreadLocal` when sharing read-only context.

### Example

```java
static final ScopedValue<String> USER = ScopedValue.newInstance();
```

---

# 4. Pattern Matching for `switch` ⭐

## Definition

- Allows `switch` statements to work directly with object types and patterns.
- Eliminates multiple `instanceof` checks and explicit casting.

### Example

```java
Object obj = "Java";

switch (obj) {

    case String s ->
            System.out.println(s.toUpperCase());

    case Integer i ->
            System.out.println(i * 2);

    default ->
            System.out.println("Unknown");
}
```

---

# 5. Record Patterns

## Definition

- **Record Patterns** allow direct extraction (destructuring) of record components.
- Reduces boilerplate code when working with records.

### Example

```java
record Employee(int id, String name) {}
Object obj = new Employee(101, "Manoj");
if (obj instanceof Employee(int id, String name)) {
    System.out.println(id);
    System.out.println(name);
}
```
---

# 6. Sequenced Collections

## Definition

- Java 21 introduces the **SequencedCollection**, **SequencedSet**, and **SequencedMap** interfaces.
- They provide a consistent way to access the **first**, **last**, and **reversed** elements of ordered collections.

### Example

```java
SequencedCollection<String> list = new ArrayList<>();
list.add("Java");
list.add("Spring");
System.out.println(list.getFirst());
System.out.println(list.getLast());
```
---

# 7. String Templates (Preview)

- **String Templates** simplify string interpolation by embedding expressions directly into strings.
- Intended to improve readability and reduce string concatenation.

### Example

```java
String name = "Manoj";
String message = STR."Hello \{name}";
```
Output :- Hello Manoj
---

# 8. Unnamed Patterns & Variables (Preview)
- Introduces `_` as a placeholder for variables or pattern components that are intentionally unused.
- Reduces unnecessary variable declarations and improves code readability.

```java
if (obj instanceof Employee(_, String name)) {
    System.out.println(name);
}
```
---

# 9. Foreign Function & Memory API (3rd Preview)

## Definition
- Provides a modern API to call native libraries (C/C++) and safely access off-heap memory.
- Intended to replace the older JNI (Java Native Interface).

### Example
```java
MemorySegment segment = Arena.ofAuto().allocate(100);
```
---

# Quick Revision
```text
Java 21 (LTS)

✔ Virtual Threads
→ Lightweight Threads

✔ Structured Concurrency
→ Manage concurrent tasks together

✔ Scoped Values
→ Better alternative to ThreadLocal

✔ Pattern Matching for switch
→ Type-based switch

✔ Record Patterns
→ Destructure records

✔ Sequenced Collections
→ getFirst(), getLast(), reversed()

✔ String Templates (Preview)
→ String interpolation

✔ Unnamed Patterns (Preview)
→ Ignore unused variables

✔ Foreign Function & Memory API
→ Replace JNI
```

---

# Most Important Java 21 Interview Topics ⭐⭐⭐⭐⭐

```text
✔ Virtual Threads
✔ Structured Concurrency
✔ Scoped Values
✔ Pattern Matching for switch
✔ Record Patterns
✔ Sequenced Collections
```