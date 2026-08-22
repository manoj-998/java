# Iterator Design Pattern

## Definition
The **Iterator Design Pattern** is a **Behavioral Design Pattern** that provides a way to **access elements of a collection one by one without exposing its internal structure**.

In simple words, Iterator lets us loop through a collection without knowing whether the data is stored in an `Array`, `List`, `Set`, tree, or any other structure.

> **Iterator = Traverse a collection without knowing how it stores the data.**

---

# Why Iterator?
Suppose we have a custom collection:

```text
EmployeeCollection
├── John
├── David
└── Alex
```

The client wants to access every employee.

Without Iterator, the client may need to know:

```text
Is it an Array?
Is it a List?
How is data stored?
What is the current index?
```

With Iterator:

```java
Iterator<Employee> iterator = collection.iterator();

while (iterator.hasNext()) {
    Employee employee = iterator.next();
}
```

The client only needs:

```text
hasNext()
next()
```

It does not need to know the internal collection structure.

---

# Problem Without Iterator

```java
class EmployeeCollection {
    Employee[] employees = new Employee[10];
}
```

Client directly accesses:

```java
for (int i = 0; i < employees.length; i++) {
    System.out.println(employees[i]);
}
```

Now the client knows:

```text
Internal Storage = Array
```

If later we change:

```text
Array → List
```

the client code may also need modification.

Problems:

- Client knows internal data structure.
- Tight coupling with collection implementation.
- Traversal logic is duplicated.
- Difficult to provide different traversal methods.

---

# Solution
Move traversal responsibility into a separate **Iterator**.

```text
Client
  |
  v
Iterator
  |
  ├── hasNext()
  └── next()
       |
       v
Collection
```

The collection creates the Iterator.

The Iterator knows how to traverse the collection.

The client only uses the Iterator.

---

# Main Components

## 1. Iterator
Defines traversal operations.

```java
interface Iterator<T> {
    boolean hasNext();
    T next();
}
```

## 2. Concrete Iterator
Contains the actual traversal logic.

Example:

```text
EmployeeIterator
```

It usually maintains:

```text
Current Position / Index
```

## 3. Aggregate / Collection
Defines a method for creating an Iterator.

```java
interface Collection<T> {
    Iterator<T> iterator();
}
```

## 4. Concrete Aggregate
Stores the actual objects and creates the Iterator.

Example:

```text
EmployeeCollection
```

## 5. Client
Gets an Iterator and traverses the collection.

---

# Structure

```text
Client
  |
  v
EmployeeCollection
  |
  | iterator()
  v
EmployeeIterator
  |
  ├── hasNext()
  └── next()
```

---

# Simple Java Example

```java
public class IteratorExample {

    /**
     * ITERATOR
     * Defines traversal operations.
     */
    interface Iterator<T> {
        boolean hasNext();
        T next();
    }

    /**
     * Employee object stored inside the collection.
     */
    static class Employee {
        String name;

        Employee(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    /**
     * CONCRETE AGGREGATE
     * Stores employees internally in an array.
     */
    static class EmployeeCollection {
        private final Employee[] employees;

        EmployeeCollection(Employee[] employees) {
            this.employees = employees;
        }

        /**
         * Creates an Iterator for this collection.
         */
        Iterator<Employee> iterator() {
            return new EmployeeIterator(employees);
        }
    }

    /**
     * CONCRETE ITERATOR
     * Knows how to traverse the Employee array.
     */
    static class EmployeeIterator implements Iterator<Employee> {
        private final Employee[] employees;
        private int position;

        EmployeeIterator(Employee[] employees) {
            this.employees = employees;
        }

        /**
         * Checks whether another employee exists.
         */
        public boolean hasNext() {
            return position < employees.length;
        }

        /**
         * Returns current employee and moves
         * to the next position.
         */
        public Employee next() {
            return employees[position++];
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        Employee[] employees = {
            new Employee("John"),
            new Employee("David"),
            new Employee("Alex")
        };

        EmployeeCollection collection =
                new EmployeeCollection(employees);

        Iterator<Employee> iterator = collection.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```

Output:

```text
John
David
Alex
```

---

# Internal Flow
When we call:

```java
Iterator<Employee> iterator = collection.iterator();
```

the collection creates:

```text
EmployeeIterator

position = 0
```

Then:

```java
iterator.hasNext();
```

checks:

```text
position < size
```

And:

```java
iterator.next();
```

does:

```text
Return Current Element
        +
Move Position Forward
```

Flow:

```text
position = 0
     |
     v
John
     |
     v
position = 1
     |
     v
David
     |
     v
position = 2
     |
     v
Alex
     |
     v
position = 3
     |
     v
hasNext() = false
```

---

# Java Built-In Iterator
Java already provides:

```java
java.util.Iterator
```

Example:

```java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>();

        names.add("John");
        names.add("David");
        names.add("Alex");

        Iterator<String> iterator = names.iterator();

        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
```

The important methods are:

```java
hasNext()
next()
remove()
```

---

# Enhanced For Loop
When we write:

```java
for (String name : names) {
    System.out.println(name);
}
```

Java uses the `Iterable`/`Iterator` mechanism for iterable collections.

Conceptually:

```text
for-each
   |
   v
iterator()
   |
   v
hasNext()
   |
   v
next()
```

---

# Iterable vs Iterator
This is an important Java interview topic.

## Iterable
Represents something that **can provide an Iterator**.

```java
public interface Iterable<T> {
    Iterator<T> iterator();
}
```

Think:

```text
Iterable = Collection can be iterated
```

## Iterator
Performs the actual traversal.

```text
hasNext()
next()
```

Think:

```text
Iterator = Object doing the traversal
```

Easy difference:

```text
Iterable → Creates Iterator
Iterator → Traverses Elements
```

---

# Why Keep Position Inside Iterator?
Suppose:

```text
EmployeeCollection
```

contains:

```text
John
David
Alex
```

Two clients can create separate iterators:

```java
Iterator<Employee> i1 = collection.iterator();
Iterator<Employee> i2 = collection.iterator();
```

Each has its own:

```text
position
```

So:

```text
Iterator 1 → position = 2
Iterator 2 → position = 0
```

Both can traverse the same collection independently.

---

# Different Traversal Strategies
Iterator can also provide different ways to traverse the same structure.

Example:

```text
Tree
├── A
├── B
└── C
```

Possible iterators:

```text
DepthFirstIterator
BreadthFirstIterator
ReverseIterator
```

The client still uses:

```text
hasNext()
next()
```

Only the traversal implementation changes.

---

# Real-World Software Examples

## Java Collections

```text
ArrayList
LinkedList
HashSet
```

provide iterators.

## Database Result Processing
Conceptually:

```text
Result Set
   |
   v
Next Record
   |
   v
Next Record
```

## File Processing

```text
File
 |
 v
Line Iterator
 |
 v
Process one line at a time
```

## Tree Traversal

```text
Tree
 |
 ├── DFS Iterator
 └── BFS Iterator
```

---

# Advantages

- Hides collection implementation.
- Simplifies traversal.
- Reduces client coupling.
- Supports multiple independent traversals.
- Different traversal algorithms can be created.
- Collection and traversal responsibilities remain separate.
- Client uses the same traversal interface.

---

# Disadvantages

- Adds extra Iterator classes.
- Can be unnecessary for very simple collections.
- Iterator must handle collection modification carefully.
- Complex structures may require complex traversal logic.

---

# When to Use
Use Iterator when:

- You want to hide internal collection structure.
- Collection traversal logic is complex.
- Multiple traversal styles are required.
- Clients should use a common traversal interface.
- The same collection needs independent traversal.

Examples:

```text
Custom Collections
Trees
Graphs
File Records
Database Results
Menus
```

---

# When Not to Use
Avoid creating a custom Iterator when:

- Java's built-in collection Iterator is enough.
- The collection is very simple.
- There is no need to hide traversal logic.
- A simple loop already solves the problem cleanly.

---

# Design Considerations
Keep responsibilities separate:

```text
Collection
    |
    | Stores Data
    v
Elements

Iterator
    |
    | Traverses Data
    v
Elements
```

Remember:

```text
Collection = Storage
Iterator   = Traversal
```

The Iterator should maintain its own traversal state:

```text
position
currentNode
stack
queue
```

depending on the data structure.

---

# Iterator vs Composite
Composite represents hierarchical data:

```text
Folder
├── File
└── Folder
```

Iterator traverses that structure:

```text
Iterator
   |
   v
Folder → File → Folder → File
```

Easy difference:

```text
Composite = Build Tree
Iterator  = Traverse Tree
```

They are often used together.

---

# Iterator vs Visitor

```text
Iterator → Controls HOW objects are traversed
Visitor  → Defines WHAT operation is performed on objects
```

They can also work together:

```text
Iterator
   |
Traverse Objects
   |
   v
Visitor
   |
Perform Operation
```

---

# Pitfalls

- Don't expose internal collection details unnecessarily.
- Don't keep traversal position inside the collection itself.
- Handle empty collections correctly.
- Handle `next()` when no elements remain.
- Be careful when the collection changes during iteration.
- Prefer Java's built-in Iterator when possible.

---

# Interview Questions

## What is Iterator Pattern?
Iterator is a Behavioral Design Pattern that provides sequential access to collection elements without exposing the collection's internal structure.

## Which category does Iterator belong to?
**Behavioral Design Pattern**

## What are the main components?
- Iterator
- Concrete Iterator
- Aggregate
- Concrete Aggregate
- Client

## What does `hasNext()` do?
Checks whether another element is available.

## What does `next()` do?
Returns the next element and moves the Iterator forward.

## What is the difference between Iterable and Iterator?

```text
Iterable → Provides iterator()
Iterator → Provides hasNext() and next()
```

## Why does Iterator maintain position?
It needs to remember where it currently is during traversal.

## Can multiple Iterators traverse the same collection?
Yes. Each Iterator can maintain its own traversal state.

## What is a real Java example?
Java Collections:

```java
Iterator<String> iterator = list.iterator();
```

## Does for-each use Iterator?
For objects implementing `Iterable`, the enhanced `for` loop uses the iterable traversal mechanism.

## Iterator vs Composite?

```text
Composite → Represents hierarchy
Iterator  → Traverses hierarchy/collection
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Used to traverse collections.
- Hides internal collection structure.
- `hasNext()` checks for more elements.
- `next()` returns the next element.
- Iterator stores traversal state.
- Multiple Iterators can traverse independently.
- `Iterable` creates an Iterator.
- Java Collections already support Iterator.
- Useful for custom and complex data structures.

---

# Easy Trick to Remember
Think about a **TV remote**.

You don't need to know how channels are internally stored.

You simply press:

```text
Next
Next
Next
```

Similarly:

```text
Collection
    |
    v
Iterator
    |
    ├── hasNext()
    └── next()
```

> **Iterator = Give me the next item without showing me how the collection is stored.**

Easy memory:

```text
Collection = Stores
Iterator   = Moves
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Traverse collection without exposing structure |
| Iterator | Defines traversal operations |
| Concrete Iterator | Performs traversal |
| Aggregate | Provides Iterator |
| Main Methods | `hasNext()`, `next()` |
| Traversal State | Stored inside Iterator |
| Java Example | `java.util.Iterator` |
| Main Benefit | Hides internal structure |
| Easy Trick | Iterator = Give me Next |