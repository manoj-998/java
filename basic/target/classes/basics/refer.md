# Serialization and Deserialization in Java

## Definition

### Serialization

**Serialization** is the process of converting a Java object into a **byte stream** so that it can be:

- Stored in a file
- Sent over a network
- Saved in a database
- Cached

---

### Deserialization

**Deserialization** is the process of converting the **byte stream back into a Java object**.

---

# Why Do We Need Serialization?

Serialization is used when an object needs to be transferred or stored.

Common use cases:

- Network communication
- File storage
- Distributed systems
- Caching (Redis, Hazelcast)
- Messaging (Kafka, RabbitMQ)
- Session replication

---

# How Serialization Works

```
Java Object
      │
      ▼
Serialization
(ObjectOutputStream)
      │
      ▼
Byte Stream
      │
      ▼
File / Network / Database
```

---

# How Deserialization Works

```
Byte Stream
      │
      ▼
Deserialization
(ObjectInputStream)
      │
      ▼
Java Object
```

---

# Example

## Step 1 : Serializable Class

```java
import java.io.Serializable;

class Employee implements Serializable {

    private int id;
    private String name;

    public Employee(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " " + name;
    }
}
```

---

## Step 2 : Serialization

```java
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {

        Employee emp = new Employee(101, "Manoj");

        FileOutputStream fos =
                new FileOutputStream("employee.ser");

        ObjectOutputStream out =
                new ObjectOutputStream(fos);

        out.writeObject(emp);

        out.close();

        System.out.println("Object Serialized");
    }
}
```

Output

```
Object Serialized
```

A file

```
employee.ser
```

is created.

---

## Step 3 : Deserialization

```java
import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {

        FileInputStream fis =
                new FileInputStream("employee.ser");

        ObjectInputStream in =
                new ObjectInputStream(fis);

        Employee emp = (Employee) in.readObject();

        in.close();

        System.out.println(emp);
    }
}
```

Output

```
101 Manoj
```

---

# What is Serializable?

```java
class Employee implements Serializable
```

`Serializable` is a **marker interface**.

It contains **no methods**.

Its purpose is to tell the JVM:

> This object can be serialized.

---

# serialVersionUID

```java
private static final long serialVersionUID = 1L;
```

It is a unique version identifier.

Used during deserialization to verify that the sender and receiver have compatible class versions.

---

# transient Keyword

Suppose

```java
class Employee implements Serializable {

    int id;

    transient String password;
}
```

When serialized,

```
id
```

is saved.

```
password
```

is **not** saved.

After deserialization

```java
password = null;
```

---

# Internal Flow

Serialization

```
Employee Object

      │

ObjectOutputStream

      │

Byte Stream

      │

employee.ser
```

Deserialization

```
employee.ser

      │

ObjectInputStream

      │

Employee Object
```

---

# Real-World Example

Suppose a user logs into an application.

The User object is serialized and stored in:

- Redis
- HTTP Session
- Distributed Cache

Later,

the object is deserialized to restore the user's session.

---

# Advantages

- Easy object storage
- Easy network communication
- Used in distributed systems
- Supports caching
- Supports session replication

---

# Disadvantages

- Slower than custom serialization formats.
- Large serialized objects consume more memory.
- Version mismatch may cause errors.
- Java Serialization has security concerns.

---

# When to Use

Use Serialization when:

- Sending objects over the network.
- Saving objects to files.
- Caching objects.
- Session replication.
- Distributed systems.

---

# Implementation Considerations

- Implement `Serializable`.
- Define `serialVersionUID`.
- Mark sensitive fields as `transient`.
- Close streams properly (or use try-with-resources).
- Prefer JSON or Protocol Buffers for external APIs.

---

# Design Considerations

- Serialize only the required fields.
- Avoid serializing sensitive information like passwords.
- Keep serialized objects backward compatible.
- Consider performance for large object graphs.

---

# Pitfalls

- Forgetting to implement `Serializable`.
- Missing `serialVersionUID` may cause `InvalidClassException`.
- `transient` fields are not restored after deserialization.
- Java native serialization is not recommended for public APIs due to security and compatibility concerns.

---

# Serialization vs Deserialization

| Serialization | Deserialization |
|--------------|-----------------|
| Object → Byte Stream | Byte Stream → Object |
| Uses `ObjectOutputStream` | Uses `ObjectInputStream` |
| Saves or sends object | Restores object |

---

# Interview Questions

## What is Serialization?

Serialization is the process of converting a Java object into a byte stream so it can be stored or transmitted.

---

## What is Deserialization?

Deserialization is the process of reconstructing a Java object from a byte stream.

---

## What is Serializable?

`Serializable` is a marker interface that indicates an object can be serialized.

---

## What is transient?

The `transient` keyword prevents a field from being serialized.

---

## What is serialVersionUID?

It is a unique version identifier used during deserialization to verify class compatibility.

---

# Key Points

- Serialization = Object → Byte Stream
- Deserialization = Byte Stream → Object
- `Serializable` is a marker interface.
- `transient` fields are not serialized.
- `serialVersionUID` ensures version compatibility.
- Use JSON/Protobuf for communication between different systems.

---

# Easy Trick to Remember

**Serialization**

> "Object → File"

**Deserialization**

> "File → Object"

---

# One-Line Interview Answer

> **Serialization converts a Java object into a byte stream for storage or transmission, while deserialization reconstructs the object from the byte stream.**