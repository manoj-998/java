# Flyweight Design Pattern

## Definition
The **Flyweight Design Pattern** is a **Structural Design Pattern** that reduces memory usage by **sharing common objects instead of creating many duplicate objects**.

In simple words, when thousands or millions of objects contain the same data, Flyweight stores the **common data once and shares it**. Only the unique data is kept separately.

> **Flyweight = Share common data to save memory.**

---

# Why Flyweight?
Suppose we are building a game with **1,00,000 trees**.

Every tree has:

```text
Tree Type
Color
Texture
X Position
Y Position
```

Many trees may have the same:

```text
Type = Mango
Color = Green
Texture = mango.png
```

But different positions:

```text
Tree 1 → x=10, y=20
Tree 2 → x=50, y=80
Tree 3 → x=100, y=200
```

Creating the same type, color and texture for every tree wastes memory.

Flyweight shares the common information.

---

# Problem Without Flyweight

```java
class Tree {
    String type;
    String color;
    String texture;
    int x;
    int y;

    Tree(String type, String color, String texture, int x, int y) {
        this.type = type;
        this.color = color;
        this.texture = texture;
        this.x = x;
        this.y = y;
    }
}
```

For every tree:

```java
new Tree("Mango", "Green", "mango.png", 10, 20);
new Tree("Mango", "Green", "mango.png", 50, 80);
new Tree("Mango", "Green", "mango.png", 100, 200);
```

Duplicate data:

```text
"Mango"
"Green"
"mango.png"
```

is stored again and again.

Problems:

- High memory usage
- Duplicate objects/data
- Poor performance when huge numbers of objects are created
- Expensive objects may be created repeatedly

---

# Solution
Separate object data into:

```text
Intrinsic State → Shared data
Extrinsic State → Unique data
```

For our Tree example:

```text
Intrinsic State
├── Type
├── Color
└── Texture

Extrinsic State
├── X
└── Y
```

The common data is stored inside the Flyweight object.

```text
               TreeType
          type, color, texture
                  ▲
                  |
            Shared Object
                  |
        ┌─────────┼─────────┐
        |         |         |
      Tree      Tree      Tree
     x=10      x=50      x=100
     y=20      y=80      y=200
```

---

# Main Components

## 1. Flyweight
Contains the common/shared data.

Example:

```text
TreeType
```

## 2. Concrete Flyweight
The actual shared object.

Example:

```text
TreeType("Mango", "Green")
```

## 3. Flyweight Factory
Creates and reuses Flyweight objects.

Example:

```text
TreeFactory
```

## 4. Context
Contains unique data and references the shared Flyweight.

Example:

```text
Tree
```

## 5. Client
Requests objects through the Flyweight Factory.

---

# Simple Java Example

```java
import java.util.HashMap;
import java.util.Map;

public class FlyweightExample {

    /**
     * FLYWEIGHT
     * Contains common/shared data.
     */
    static class TreeType {
        String name;
        String color;

        TreeType(String name, String color) {
            this.name = name;
            this.color = color;
        }

        void draw(int x, int y) {
            System.out.println(name + " " + color + " at " + x + "," + y);
        }
    }

    /**
     * FLYWEIGHT FACTORY
     * Creates TreeType only once and reuses it.
     */
    static class TreeFactory {
        private static final Map<String, TreeType> cache = new HashMap<>();

        static TreeType getTreeType(String name, String color) {
            String key = name + color;

            if (!cache.containsKey(key)) {
                cache.put(key, new TreeType(name, color));
            }

            return cache.get(key);
        }
    }

    /**
     * CONTEXT
     * Contains unique data.
     */
    static class Tree {
        int x;
        int y;
        TreeType type;

        Tree(int x, int y, TreeType type) {
            this.x = x;
            this.y = y;
            this.type = type;
        }

        void draw() {
            type.draw(x, y);
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        TreeType mango = TreeFactory.getTreeType("Mango", "Green");

        Tree tree1 = new Tree(10, 20, mango);
        Tree tree2 = new Tree(50, 80, mango);
        Tree tree3 = new Tree(100, 200, mango);

        tree1.draw();
        tree2.draw();
        tree3.draw();
    }
}
```

Output:

```text
Mango Green at 10,20
Mango Green at 50,80
Mango Green at 100,200
```

All three trees share the same:

```text
TreeType
```

but have different:

```text
x
y
```

---

# Intrinsic vs Extrinsic State

This is the **most important concept** in Flyweight.

## Intrinsic State
Data that is **common and shared** between many objects.

Example:

```text
Tree Type
Color
Texture
```

Stored inside:

```text
Flyweight Object
```

## Extrinsic State
Data that is **different for each object**.

Example:

```text
X Position
Y Position
```

Usually stored outside the Flyweight or passed to it when needed.

Easy trick:

```text
Intrinsic = Shared
Extrinsic = Unique
```

---

# Internal Flow

```text
Client
  |
  v
TreeFactory
  |
  | Check Cache
  |
  ├── Exists? → Return existing TreeType
  |
  └── Not Exists? → Create TreeType
                         |
                         v
                      Cache It
```

Future requests:

```text
getTreeType("Mango", "Green")
              |
              v
         Same Object
```

---

# Verify Object Sharing

```java
TreeType t1 = TreeFactory.getTreeType("Mango", "Green");
TreeType t2 = TreeFactory.getTreeType("Mango", "Green");

System.out.println(t1 == t2);
```

Output:

```text
true
```

Both variables reference the same shared object.

---

# Real-World Software Examples

## Java String Pool

```java
String s1 = "hello";
String s2 = "hello";

System.out.println(s1 == s2);
```

Java can reuse the same pooled String object.

Conceptually:

```text
"hello"
   ▲
   |
 ┌─┴─┐
s1   s2
```

## Integer Cache
Java caches some commonly used `Integer` objects.

```java
Integer a = 100;
Integer b = 100;

System.out.println(a == b);
```

Often:

```text
true
```

This is similar to the Flyweight idea: reuse common objects instead of repeatedly creating them.

## Game Development

```text
Bullet Type + Position
Tree Type + Position
Enemy Type + Position
```

Shared:

```text
Image
Texture
Model
Color
```

Unique:

```text
Position
Health
Direction
```

## Text Editor
A document may contain millions of characters.

Shared:

```text
Character
Font
Style
```

Unique:

```text
Position
```

---

# Advantages

- Reduces memory usage.
- Avoids duplicate objects.
- Reuses common data.
- Useful when creating huge numbers of similar objects.
- Can improve performance when object creation is expensive.

---

# Disadvantages

- Makes code slightly more complex.
- Intrinsic and extrinsic state must be identified correctly.
- Shared objects should generally be immutable.
- Managing the Flyweight cache adds complexity.
- Not useful when objects don't share much data.

---

# When to Use
Use Flyweight when:

- A huge number of similar objects are created.
- Many objects contain duplicate data.
- Memory usage is important.
- Common state can be shared.
- Unique state can be separated.

Examples:

```text
Game Trees
Game Bullets
Text Characters
Icons
Map Markers
```

---

# When Not to Use
Avoid Flyweight when:

- Only a small number of objects exist.
- Objects don't share common data.
- Memory usage is not a problem.
- Separating shared and unique state makes the design unnecessarily complex.

---

# Design Considerations

Identify:

```text
What is COMMON?
        ↓
Intrinsic State
        ↓
Store in Flyweight

What is UNIQUE?
        ↓
Extrinsic State
        ↓
Store outside Flyweight
```

Shared Flyweight objects should preferably be **immutable**, because multiple objects may use the same instance.

---

# Flyweight vs Singleton
Both can reuse objects, but their purpose is different.

```text
Singleton → Ensure only ONE instance exists
Flyweight → Share MANY reusable objects
```

Example:

```text
Singleton
    ↓
One ConfigurationManager

Flyweight
    ↓
One MangoTreeType
One AppleTreeType
One CoconutTreeType
```

A Flyweight system can therefore contain multiple shared objects.

---

# Flyweight vs Object Pool

```text
Flyweight   → Objects are shared simultaneously
Object Pool → Objects are borrowed and returned
```

Example:

```text
Flyweight → Shared TreeType
Object Pool → Database Connections
```

---

# Flyweight vs Cache
A cache is a general technique for storing data for faster reuse.

Flyweight specifically focuses on:

> **Sharing common object state to reduce memory usage.**

A Flyweight Factory commonly uses a cache internally.

---

# Pitfalls

- Don't use Flyweight when the number of objects is small.
- Don't store unique state inside shared Flyweight objects.
- Be careful when modifying shared objects.
- Prefer immutable Flyweight objects.
- Don't add caching complexity unless memory/object creation is actually a problem.

---

# Interview Questions

## What is Flyweight Pattern?
Flyweight is a Structural Design Pattern that reduces memory usage by sharing common objects instead of creating duplicate objects.

## Which category does Flyweight belong to?
**Structural Design Pattern**

## What problem does Flyweight solve?
It reduces memory usage when a large number of similar objects contain duplicate data.

## What are the main components?
- Flyweight
- Concrete Flyweight
- Flyweight Factory
- Context
- Client

## What is Intrinsic State?
Intrinsic State is the **common/shared data** stored inside the Flyweight.

Example:

```text
Tree Type
Color
Texture
```

## What is Extrinsic State?
Extrinsic State is the **unique data** for each object.

Example:

```text
X Position
Y Position
```

## Why should Flyweight objects be immutable?
Because the same Flyweight object can be shared by many clients. Changing it could unexpectedly affect all objects using it.

## What is the role of Flyweight Factory?
It checks whether a Flyweight already exists.

```text
Exists     → Reuse
Not Exists → Create + Store
```

## Flyweight vs Singleton?

```text
Singleton → One instance
Flyweight → Multiple shared instances
```

## Give Java examples related to Flyweight.
Common examples include:

```text
String Pool
Integer Cache
```

---

# Key Points

- Category: **Structural Design Pattern**
- Main goal is memory optimization.
- Shares common objects.
- Avoids duplicate data.
- Intrinsic State = Shared.
- Extrinsic State = Unique.
- Factory manages shared objects.
- Factory usually maintains a cache.
- Shared Flyweights should preferably be immutable.
- Useful when there are huge numbers of similar objects.

---

# Easy Trick to Remember

Suppose 10,000 trees have the same image.

Bad:

```text
Tree1 → mango.png
Tree2 → mango.png
Tree3 → mango.png
...
```

Flyweight:

```text
        mango.png
           ▲
           |
      Shared Once
      /    |    \
   Tree1 Tree2 Tree3
```

Remember:

> **Flyweight = Don't copy common heavy data; share it.**

And:

```text
Intrinsic = Shared
Extrinsic = Unique
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Structural |
| Purpose | Reduce memory usage |
| Main Idea | Share common objects |
| Intrinsic State | Shared/Common data |
| Extrinsic State | Unique data |
| Factory | Creates and caches Flyweights |
| Context | Stores unique state |
| Common Example | Trees in a game |
| Java Examples | String Pool, Integer Cache |
| Easy Trick | Share common data |