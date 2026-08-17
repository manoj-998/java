# Composite Design Pattern

## Definition
The **Composite Design Pattern** is a **Structural Design Pattern** that allows us to treat **individual objects and groups of objects in the same way**.

In simple words, when objects are arranged in a **tree structure**, Composite gives both the single object and the group a common interface. For example, a `File` is a single object and a `Folder` is a group, but both can support the same `show()` operation.

> **Composite = Treat ONE object and a GROUP of objects in the same way.**

---

# Why Composite?
Consider a File System:

```text
Root
├── file1.txt
├── file2.txt
└── Documents
    ├── resume.pdf
    └── notes.txt
```

Here:

```text
File   = Individual Object
Folder = Group of Objects
```

We want to perform the same operation on both:

```java
show();
```

Instead of writing separate client logic for `File` and `Folder`, Composite provides a common interface.

---

# Problem Without Composite
Without Composite, the client may need different logic:

```java
if (item instanceof File) {
    // handle file
} else if (item instanceof Folder) {
    // handle folder
}
```

Problems:

- Too many `if/else` checks
- Client needs to know object types
- Difficult to handle nested folders
- Client code becomes complicated
- Tree structures become harder to manage

---

# Solution
Create a common interface for both File and Folder.

```java
interface FileSystemItem {
    void show();
}
```

Both implement the same interface:

```text
           FileSystemItem
            /         \
           /           \
        File          Folder
        Leaf         Composite
                       |
                 List<FileSystemItem>
```

Now the client simply calls:

```java
item.show();
```

It doesn't care whether `item` is a File or Folder.

---

# Main Components

## Component
The common interface for Leaf and Composite.

```java
interface FileSystemItem {
    void show();
}
```

## Leaf
A **Leaf** is a single object that cannot contain children.

Example:

```text
File
```

```java
class File implements FileSystemItem {
    public void show() {
        System.out.println("File");
    }
}
```

## Composite
A **Composite** can contain Leaf objects or other Composite objects.

Example:

```text
Folder
```

```java
class Folder implements FileSystemItem {
    List<FileSystemItem> items = new ArrayList<>();

    void add(FileSystemItem item) {
        items.add(item);
    }

    public void show() {
        for (FileSystemItem item : items) {
            item.show();
        }
    }
}
```

---

# Simple Java Example

```java
import java.util.*;

interface FileSystemItem {
    void show();
}

class File implements FileSystemItem {
    private String name;

    File(String name) {
        this.name = name;
    }

    public void show() {
        System.out.println("File: " + name);
    }
}

class Folder implements FileSystemItem {
    private String name;
    private List<FileSystemItem> items = new ArrayList<>();

    Folder(String name) {
        this.name = name;
    }

    void add(FileSystemItem item) {
        items.add(item);
    }

    public void show() {
        System.out.println("Folder: " + name);
        for (FileSystemItem item : items) {
            item.show();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        File file1 = new File("resume.pdf");
        File file2 = new File("photo.jpg");

        Folder documents = new Folder("Documents");
        documents.add(file1);

        Folder root = new Folder("Root");
        root.add(documents);
        root.add(file2);

        root.show();
    }
}
```

Output:

```text
Folder: Root
Folder: Documents
File: resume.pdf
File: photo.jpg
```

---

# Internal Structure

```text
Root
├── Documents
│   └── resume.pdf
└── photo.jpg
```

Here:

```text
File   → Leaf
Folder → Composite
```

A Folder can contain:

```text
Folder → File
Folder → Folder
```

This creates a tree structure.

---

# Internal Flow
When we call:

```java
root.show();
```

the call flows recursively:

```text
Root.show()
   |
   ├── Documents.show()
   |       |
   |       └── resume.pdf.show()
   |
   └── photo.jpg.show()
```

The Composite calls the same operation on all its children.

---

# Real-World Software Examples

## File System

```text
Folder
├── File
└── Folder
```

## Organization Hierarchy

```text
Manager
├── Employee
├── Employee
└── Manager
    └── Employee
```

## UI Components

```text
Page
├── Button
├── TextBox
└── Panel
    ├── Button
    └── Label
```

## Menu System

```text
Menu
├── MenuItem
└── SubMenu
    ├── MenuItem
    └── MenuItem
```

---

# Advantages

- Treats individual and group objects uniformly.
- Makes tree structures easy to manage.
- Reduces `if/else` type checking.
- Supports nested objects naturally.
- Client code becomes simpler.
- Easy to add new Leaf or Composite types.
- Recursive operations become easy.

---

# Disadvantages

- Can make simple designs unnecessarily complex.
- Deep tree structures can be difficult to debug.
- Restricting which children can be added may be difficult.
- Not useful when objects don't have a tree structure.

---

# When to Use
Use Composite when:

- Objects form a tree structure.
- Objects can contain other objects.
- Individual and group objects need the same operations.
- Nested structures are required.

Common examples:

```text
File + Folder
Employee + Manager
MenuItem + Menu
UI Element + Container
Product + Category
```

---

# When Not to Use
Avoid Composite when:

- There is no tree structure.
- Objects don't have parent-child relationships.
- Single and group objects require completely different operations.
- The hierarchy is very simple.

---

# Design Considerations
The main relationship is:

```text
Composite HAS-A List<Component>
```

Example:

```text
Folder HAS-A List<FileSystemItem>
```

Since `FileSystemItem` can be either:

```text
File
```

or:

```text
Folder
```

we can create nested structures.

```text
Component
├── Leaf
└── Composite
    ├── Leaf
    └── Composite
```

---

# Composite vs Decorator

Both use composition, but their purpose is different.

```text
Composite → Manages a GROUP of objects
Decorator → Adds behavior to ONE wrapped object
```

Usually:

```text
Composite → List<Component>
Decorator → One Component
```

Easy trick:

```text
Composite = Group
Decorator = Wrap
```

---

# Composite vs Iterator

```text
Composite → Creates/manages a tree structure
Iterator  → Traverses a collection
```

Iterator can be used to traverse objects inside a Composite.

---

# Pitfalls

- Don't use Composite without a natural tree structure.
- Avoid putting unrelated methods in the Component interface.
- Be careful with deep recursive structures.
- Keep Leaf objects simple.
- Composite should mainly manage and delegate to children.
- Avoid unnecessary type checking using `instanceof`.

---

# Interview Questions

## What is Composite Pattern?
Composite is a Structural Design Pattern that allows individual objects and groups of objects to be treated uniformly.

## Which category does Composite belong to?
**Structural Design Pattern**

## What are the main components?
- Component
- Leaf
- Composite

## What is Component?
The common interface implemented by both Leaf and Composite.

## What is Leaf?
A Leaf is an individual object that does not contain children.

Example:

```text
File
```

## What is Composite?
A Composite is an object that contains other Components.

Example:

```text
Folder
```

## Can Composite contain another Composite?
Yes.

```text
Folder
└── Folder
    └── Folder
```

This allows recursive tree structures.

## What is the main benefit of Composite?
The client can treat a single object and a group of objects in the same way.

## Give a real-world example.
The most common example is:

```text
File + Folder
```

## Composite vs Decorator?

```text
Composite → Group/tree of objects
Decorator → Adds behavior to an object
```

---

# Key Points

- Category: **Structural Design Pattern**
- Used mainly for tree structures.
- Treats single and group objects uniformly.
- `Component` is the common interface.
- `Leaf` is an individual object.
- `Composite` contains Components.
- Composite can contain another Composite.
- Uses recursive operations.
- Reduces client-side `if/else` checks.
- File System is the easiest example.

---

# Easy Trick to Remember
Think about a File System:

```text
Folder
├── File
├── File
└── Folder
    └── File
```

A File is **ONE** object.

A Folder contains **MANY** objects.

But both are treated as:

```text
FileSystemItem
```

> **Composite = Treat ONE and MANY the SAME.**

Remember:

```text
Leaf      = Single Object
Composite = Group of Objects
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Treat single and group objects uniformly |
| Structure | Tree |
| Component | Common interface |
| Leaf | Individual object |
| Composite | Group/container |
| Relationship | Composite HAS-A List of Components |
| Common Example | File + Folder |
| Main Benefit | Simplifies hierarchical structures |
| Easy Trick | ONE and MANY are treated the SAME |