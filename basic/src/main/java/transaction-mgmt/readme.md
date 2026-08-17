# Spring Transactions

## Definition

- A **Transaction** is a sequence of one or more database operations executed as a **single logical unit of work**.
- It ensures that **either all operations succeed (Commit) or all operations fail (Rollback)**, maintaining database consistency.
---

## Why Do We Need It?

Many business operations involve multiple database updates.

If one operation succeeds and another fails, the database becomes inconsistent.
A transaction ensures **everything succeeds or everything is undone**.

---

## What Problem Does It Solve?

Without Transaction

```text
Transfer ₹1000
↓
Debit Account A ✔
↓
Application Crash ❌
↓
Credit Account B ✘
```
Result
```text
Money Lost
```

With Transaction
```text
Debit
↓
Credit
↓
Commit
```
If any step fails
```text
Rollback
↓
Database returns to previous state.
```
---

## Real-World Example

### Bank Transfer

Before

```text
Account A = ₹5000
Account B = ₹3000
```
Transfer ₹1000

```text
Debit ₹1000 from A
↓
Credit ₹1000 to B
↓
Commit
```

After
```text
Account A = ₹4000
Account B = ₹4000
```
If Credit fails
```text
Rollback
↓
Account A = ₹5000
Account B = ₹3000
```
---

## Spring Boot Example

```java
@Service
public class PaymentService {

    @Transactional
    public void transferMoney() {
        debitAccount();
        creditAccount();
    }
}
```

Spring automatically
```text
Begin Transaction
↓
Execute SQL
↓
Commit
OR
Rollback
```
---

## Transaction Lifecycle

```text
Begin Transaction
    ↓
Execute Queries 
    ↓
Commit
  OR
Rollback
    ↓
End Transaction
```
---

## Commit

- Permanently saves all changes made during the transaction.
- Executed when every operation succeeds.

Example

```text
Create Order
    ↓
Update Inventory
    ↓
Payment Success
    ↓
Commit
```
---

## Rollback

- Cancels all operations performed during the transaction.
- Restores the database to its previous state.

Example
```text
Payment Failed
    ↓
Rollback
    ↓
Order Not Created
```

---

## Transaction Propagation
Propagation defines **how one transaction behaves when another transactional method is called.**
```text
Main transaction T1
|
+-- save payment
|
+-- T1 suspended
|
+-- Audit transaction T2
|      |
|      +-- COMMIT ✓
|
+-- T1 resumed
|
+-- ERROR
|
+-- ROLLBACK T1 ✗
```

### REQUIRED (Default)
- Joins the existing transaction.
- Creates a new one if none exists.

Use when
```text
Business Services
```

---

### REQUIRES_NEW

- Always creates a new transaction.
- Suspends the current transaction.

Use when
```text
Audit Logs
Notifications
Email
```
---

| Propagation     | What happens                                                 |
| --------------- | ------------------------------------------------------------ |
| `REQUIRED`      | Use existing transaction; otherwise create one               |
| `REQUIRES_NEW`  | Always create a new transaction and suspend the existing one |
| `SUPPORTS`      | Use transaction if one exists; otherwise run without one     |
| `MANDATORY`     | Must already have a transaction, otherwise exception         |
| `NOT_SUPPORTED` | Run without transaction; suspend existing transaction        |
| `NEVER`         | Must run without a transaction; exception if one exists      |
| `NESTED`        | Execute within a nested transaction/savepoint when supported |

## Rollback Rules
Default
```text
Rollback
    ↓
RuntimeException
Error
```

Custom
```java
@Transactional(
        rollbackFor = Exception.class
)
```
---

## Read Only Transaction
```java
@Transactional(readOnly = true)
```

Use when
```text
SELECT Queries
```

Benefits

✔ Better Performance
✔ Skips Dirty Checking
✔ Prevents Accidental Updates
---

## Best Practices

✔ Keep transactions short.
✔ Don't call external APIs inside transactions.
✔ Use `readOnly=true` for SELECT.
✔ Keep database locks for minimum time.
---

## Interview Questions

### What is a Transaction?
A transaction is a logical unit of work where all database operations either succeed together or fail together.
---

### What does `@Transactional` do?
It automatically manages transaction boundaries by beginning, committing, or rolling back transactions.

---

### Default Propagation?
```text
REQUIRED
```
---

### Default Rollback?
```text
RuntimeException

Error
```
---

## Quick Revision
```text
Transaction
    ↓
Commit
OR
Rollback
Spring
@Transactional
Default Propagation
REQUIRED
Default Rollback
RuntimeException
```
---

# ACID Properties
## Definition
- **ACID** is a set of four properties that ensure every database transaction is **reliable, consistent, and safe**.
- Every successful transaction should satisfy **Atomicity, Consistency, Isolation, and Durability**.

---

# A — Atomicity
## Definition
- Atomicity ensures **all operations in a transaction execute completely or none execute at all**.
- There are no partial updates.

---

## Why Do We Need It?
To prevent incomplete transactions.

---

## What Problem Does It Solve?
Without Atomicity

```text
Debit Done ✔

Credit Failed ✘
↓
Money Lost
```

---

## Real-World Example
Bank Transfer

```text
Debit
↓
Credit
↓
Commit
```

If Credit fails
```text
Rollback
↓
Nothing Changes
```

---

# C — Consistency
## Definition
- Consistency ensures the database always moves from **one valid state to another valid state**.
- Database rules and constraints are never violated.

---

## Why Do We Need It?
To prevent invalid or corrupted data.

---

## What Problem Does It Solve?

```text
Negative Balance
Duplicate Primary Key
Invalid Foreign Key
```
---

## Real-World Example

```text
Total Money

Before = ₹8000
↓
Transfer
↓
After = ₹8000
```

Money is transferred—not created or destroyed.

---

# I — Isolation

## Definition

- Isolation ensures multiple transactions execute **independently** without affecting each other.
- One transaction should not see another transaction's intermediate changes.

---

## Why Do We Need It?
Multiple users may update the same data simultaneously.

---

## What Problem Does It Solve?
Prevents
```text
Dirty Read
Non-repeatable Read
Phantom Read
Lost Update
```
---

## Real-World Example

Movie Ticket Booking

```text
Last Seat
↓
User A Books
↓
User B Books
↓
Without Isolation

Both Succeed ❌
```

With Isolation
```text
Only One Booking Succeeds ✔
```

---

# D — Durability
## Definition
- Durability ensures that once a transaction is committed, the data is permanently stored.
- Data remains safe even after power failure or server crash.

---

## Why Do We Need It?
To ensure committed transactions are never lost.

---

## What Problem Does It Solve?

```text
Payment Success
↓
Commit
↓
Server Crash
↓
Payment Still Exists
```

---

## Real-World Example
ATM Withdrawal

```text
Cash Dispensed
↓
Commit
↓
Power Failure
↓
Transaction Still Recorded
```
---

# ACID Summary
| Property    | Meaning                  | Problem Solved             |
|-------------|--------------------------|----------------------------|
| Atomicity   | All or Nothing           | Partial Updates            |
| Consistency | Valid Database State     | Invalid Data               |
| Isolation   | Independent Transactions | Concurrent Access Problems |
| Durability  | Permanent Data           | Data Loss After Commit     |

---

## Interview Questions

### What is ACID?
ACID is a set of four properties that ensure reliable and consistent database transactions.

---

## Quick Revision

```text
ACID

A → Atomicity
(All or Nothing)

C → Consistency
(Valid Database)

I → Isolation
(No Concurrent Issues)

D → Durability
(Permanent Data)

Memory Trick

A → All
C → Correct
I → Independent
D → Don't Lose Data
```
````
````
# Distributed Transactions

## Definition
- A **Distributed Transaction** is a transaction that spans **multiple services or databases**, ensuring all participating systems either **commit together** or **roll back together**.
- It is commonly used in **Microservices Architecture**, where each service owns its own database.
---

## Why Do We Need It?
In Microservices, a single business operation may involve multiple services.
Without distributed transactions, one service may succeed while another fails, resulting in inconsistent data.
---

## What Problem Does It Solve?

Without Distributed Transaction

```text
Order Service ✔
↓
Payment Service ✔
↓
Inventory Service ❌
↓
Customer Charged
Product Not Reserved
```

Result
```text
Inconsistent System
```

With Distributed Transaction

```text
Order
↓
Payment
↓
Inventory
↓
Shipping
↓
Commit
```
If any service fails

```text
Rollback / Compensation
↓
System returns to a consistent state.
```
---

# Real-World Example

### Amazon Order
Customer clicks
```text
Buy Now
```
The application performs

```text
Create Order
↓
Process Payment
↓
Reserve Inventory
↓
Create Shipment
```
If Inventory Service fails

```text
Refund Payment
↓
Cancel Order
↓
Notify Customer
```
Everything remains consistent.

---

# Architecture
```text
Client
    ↓   
Order Service
    ↓
Payment Service
    ↓
Inventory Service
    ↓
Shipping Service
```
Each service has its own database.

---

# Common Approaches

## 1. Two-Phase Commit (2PC)

### Definition
- **Two-Phase Commit (2PC)** is a distributed transaction protocol where a **Coordinator** asks every participating service whether it can commit.
- If all services agree, they commit. Otherwise, every service rolls back.
---

### Why Do We Need It?
To maintain **strong consistency** across multiple databases.

---

### What Problem Does It Solve?
Ensures that every service either commits or rolls back together.

---

### Flow

```text
Coordinator
    ↓
Prepare Phase
    ↓
Service A ✔
Service B ✔
Service C ✔
    ↓
Commit Phase
↓
Commit All
```

If one service fails

```text
Coordinator
    ↓
Rollback All
```

---

### Real-World Example

```text
Bank Transfer
    ↓
Debit Account
    ↓
Credit Account
    ↓
Commit
```
If Credit fails

```text
Rollback
```
---

### Advantages

✔ Strong Consistency

✔ Easy to Understand

---

### Disadvantages

✘ Blocking

✘ Slow

✘ Poor Scalability

---

## 2. Saga Pattern ⭐

### Definition

- **Saga Pattern** divides one large transaction into multiple **local transactions**.
- If one transaction fails, **Compensation Transactions** undo previously completed work.

---

### Why Do We Need It?
Traditional database transactions don't work efficiently across multiple microservices.

---

### What Problem Does It Solve?
Maintains consistency without locking multiple databases.

---

### Flow

```text
Create Order ✔
    ↓
Payment ✔
    ↓
Reserve Inventory ❌
    ↓
Refund Payment
    ↓
Cancel Order
```
---

### Real-World Example

```text
Book Flight ✔
    ↓
Book Hotel ✔
    ↓
Payment Failed ❌
    ↓
Cancel Hotel
    ↓
Cancel Flight
```
---

### Advantages

✔ High Performance

✔ Highly Scalable

✔ Non-blocking

✔ Best for Microservices

---

### Disadvantages

✘ Eventual Consistency

✘ Complex Implementation

---

## 3. Eventual Consistency
### Definition
- **Eventual Consistency** means all services become consistent **after some time**, rather than immediately.
- Temporary inconsistencies are acceptable until all services process the required events.

---

### Why Do We Need It?
To improve scalability and avoid blocking multiple services.

---

### What Problem Does It Solve?
Allows distributed systems to remain available without requiring immediate consistency.

---

### Real-World Example
```text
Payment Success
    ↓
Inventory Updating
    ↓
Inventory Updated
    ↓
System Consistent
```

---

## 4. Outbox Pattern

### Definition
- The **Outbox Pattern** stores events in an **Outbox Table** within the same database transaction.
- A background process later publishes those events to Kafka or RabbitMQ.
---

### Why Do We Need It?
To ensure database updates and event publishing happen reliably.

---

### What Problem Does It Solve?
Prevents event loss when an application crashes after saving data but before publishing the event.

---

### Flow

```text
Save Order
    ↓
Save Outbox Event
    ↓
Commit
    ↓
Background Worker
    ↓
Kafka
    ↓
Inventory Service
```

---

## 5. Idempotency

### Definition
- **Idempotency** ensures that executing the same request multiple times produces the **same result**.
- It prevents duplicate operations caused by retries.
---

### Why Do We Need It?
Network failures often cause clients to resend the same request.

---

### What Problem Does It Solve?
Prevents duplicate payments, duplicate orders, and duplicate shipments.

---

### Real-World Example

```text
Customer Clicks
Pay
↓
Network Timeout
↓
Retry
↓
Money Deducted Only Once ✔
```
---

## 6. Compensation Transaction

### Definition
- A **Compensation Transaction** reverses the work of a previously completed transaction.
- Used mainly in the **Saga Pattern**.
---

### Why Do We Need It?
To undo completed operations when a later transaction fails.

---

### Real-World Example

```text
Payment Success
    ↓
Inventory Failed
    ↓
Refund Payment
```

---

# 2PC vs Saga
| Feature     | 2PC     | Saga          |
|-------------|---------|---------------|
| Consistency | Strong  | Eventual      |
| Blocking    | Yes     | No            |
| Performance | Slow    | Fast          |
| Scalability | Low     | High          |
| Best For    | Banking | Microservices |
---

# Interview Questions

### What is a Distributed Transaction?
A distributed transaction is a transaction that spans multiple services or databases while maintaining consistency across all participants.

---

### Why is Saga preferred over 2PC?
Because Saga is non-blocking, highly scalable, and better suited for Microservices.

---

### What is Eventual Consistency?
Data may not be immediately consistent, but it becomes consistent after all services complete processing.

---

### What is the Outbox Pattern?
A pattern that guarantees reliable event publishing by storing events in an Outbox table before publishing them.

---

# Best Practices
✔ Prefer Saga Pattern for Microservices.

✔ Use Outbox Pattern with Kafka or RabbitMQ.

✔ Make APIs Idempotent.

✔ Keep Local Transactions Small.

✔ Avoid 2PC in High-Scale Systems.

---

# Quick Revision

```text
Distributed Transaction
    ↓
Multiple Services
    ↓
Maintain Consistency

Approaches
2PC
→ Strong Consistency
→ Blocking

Saga ⭐
→ Local Transactions
→ Compensation
→ Best for Microservices

Eventual Consistency
→ Consistent Later

Outbox Pattern
→ Reliable Event Publishing

Idempotency
→ Same Request
→ Same Result
```
````

````
# Transaction Isolation Levels

## Definition
- **Isolation Level** defines how one transaction is isolated from other concurrent transactions.
- It determines **how much data one transaction can see from another transaction**, preventing data inconsistency in multi-user environments.

---

## Why Do We Need Isolation Levels?
In real-world applications, multiple users access and update the same data simultaneously.
Without proper isolation, transactions may read incorrect or inconsistent data.

---

## What Problems Does It Solve?
Isolation Levels prevent common concurrency problems:
- Dirty Read
- Non-Repeatable Read
- Phantom Read
- Lost Update

---

# Concurrency Problems

# 1. Dirty Read
A **Dirty Read** occurs when one transaction reads data that has **not yet been committed** by another transaction.

## Why Is It Bad?
The transaction that modified the data may roll back later.
The reading transaction has already read incorrect data.

## Real-World Example

```text
Transaction A
Update Salary
50000 → 60000
(Not Committed)
    ↓
Transaction B
Reads Salary
60000
↓
Transaction A
Rollback
↓
Actual Salary = 50000
Transaction B Read Wrong Data
```

---

# 2. Non-Repeatable Read
A **Non-Repeatable Read** occurs when the same row returns different values during the same transaction because another transaction updated it.

## Why Is It Bad?
The transaction cannot rely on previously read data.

## Real-World Example

```text
Transaction A
Read Balance
₹5000
↓
Transaction B
Update Balance
₹4000
↓
Transaction A
Read Again
₹4000
```
Same row but different values

---

# 3. Phantom Read
A **Phantom Read** occurs when a transaction executes the same query twice and gets a different number of rows because another transaction inserted or deleted rows.


## Why Is It Bad?

The result set changes unexpectedly.

---

## Real-World Example

```text
Transaction A

SELECT Employees

100 Rows

↓

Transaction B

Insert Employee

↓

Transaction A

SELECT Employees

101 Rows
```

New row appears.

---

# 4. Lost Update

## Definition

A **Lost Update** occurs when two transactions update the same row, and one update overwrites the other.

---

## Why Is It Bad?

One user's changes are lost.

---

## Real-World Example

```text
Salary = ₹50000

↓

User A

Changes to ₹55000

↓

User B

Changes to ₹60000

↓

Last Update Wins

↓

₹55000 Lost
```

---

# Isolation Levels
---

# 1. Read Uncommitted
- Lowest isolation level.
- Allows transactions to read **uncommitted data** from other transactions.

---

## Why Use It?
Maximum performance.

Minimum locking.

---

## Problems

```text
Dirty Read ✔

Non-Repeatable Read ✔

Phantom Read ✔
```
---

## Real-World Example
Reporting systems where temporary incorrect data is acceptable.

---

# 2. Read Committed ⭐
- A transaction can only read **committed data**.
- Prevents Dirty Reads.

---

## Why Use It?
Good balance between performance and consistency.
Default in many databases (e.g., Oracle, PostgreSQL).

---

## Problems

```text
Dirty Read ✘

Non-Repeatable Read ✔

Phantom Read ✔
```

---

## Real-World Example
Online Shopping

Customer sees only confirmed orders.

---

# 3. Repeatable Read
- Once a row is read, it cannot change during the transaction.
- Prevents Dirty Reads and Non-Repeatable Reads.

---

## Why Use It?
Ensures consistent reads for the same record.

---

## Problems

```text
Dirty Read ✘
Non-Repeatable Read ✘
Phantom Read ✔ (Database Dependent)
```

---

## Real-World Example

Bank Account Statement

Balance should remain consistent throughout the transaction.

---

# 4. Serializable

## Definition

- Highest isolation level.
- Transactions execute as if they are running one after another.

---

## Why Use It?

Maximum consistency.

No concurrency anomalies.

---

## Problems

```text
Dirty Read ✘

Non-Repeatable Read ✘

Phantom Read ✘
```

---

## Real-World Example

Movie Ticket Booking

```text
Last Seat
↓
User A Books
↓
User B Waits
↓
Only One Booking Succeeds
```

---

# Isolation Level Comparison

| Isolation Level   | Dirty Read   | Non-Repeatable Read   | Phantom Read |
|-------------------|--------------|-----------------------|--------------|
| Read Uncommitted  | ✔ Allowed   | ✔ Allowed            | ✔ Allowed |
| Read Committed    | ✘ Prevented | ✔ Possible           | ✔ Possible |
| Repeatable Read   | ✘ Prevented | ✘ Prevented          | ✔ Possible |
| Serializable      | ✘ Prevented | ✘ Prevented          | ✘ Prevented |

---

# Which One Should I Use?

| Scenario                     | Isolation Level   |
|------------------------------|-------------------|
| Reporting                    | Read Uncommitted  |
| Normal Business Applications | Read Committed ⭐ |
| Banking                      | Repeatable Read   |
| Financial Systems            | Serializable      |

---

# Spring Boot Example

```java
@Transactional(
    isolation = Isolation.READ_COMMITTED
)
public void transferMoney() {

}
```

Available Isolation Levels

```java
Isolation.DEFAULT

Isolation.READ_UNCOMMITTED

Isolation.READ_COMMITTED

Isolation.REPEATABLE_READ

Isolation.SERIALIZABLE
```

---

# Interview Questions

### What is an Isolation Level?

An Isolation Level defines how transactions are isolated from each other and controls the visibility of data changes between concurrent transactions.

---

### Which Isolation Level is the safest?

```text
Serializable
```

---

### Which Isolation Level is the fastest?

```text
Read Uncommitted
```

---

### Which Isolation Level is commonly used?

```text
Read Committed
```

---

### Which Isolation Level prevents Dirty Reads?

```text
Read Committed

Repeatable Read

Serializable
```

---

# Best Practices

✔ Use **Read Committed** for most applications.

✔ Use **Repeatable Read** for banking systems.

✔ Use **Serializable** only when absolute consistency is required.

✔ Avoid Read Uncommitted unless temporary inconsistent data is acceptable.

---

# Quick Revision

```text
Isolation Level
↓
Controls Data Visibility
Problems
Dirty Read
↓
Read Uncommitted Data
----------------------
Non-Repeatable Read
↓
Same Row Changes
----------------------
Phantom Read
↓
New Rows Appear
----------------------
Lost Update
↓
One Update Overwrites Another
Isolation Levels
Read Uncommitted
↓
Read Committed ⭐
↓
Repeatable Read
↓
Serializable
Memory Trick
Read Uncommitted
→ Fastest
Read Committed
→ Most Common
Repeatable Read
→ Banking
Serializable
→ Safest
```

# Database Locking

## Definition

- **Locking** is a concurrency-control mechanism used by databases to control simultaneous access to the same data by multiple transactions.
- It prevents problems such as **Lost Updates, inconsistent data, and conflicting modifications** when multiple users access the same records.


## Why Do We Need Locking?
Suppose two users try to update the same bank account.

```text
Balance = ₹10,000

User A reads → ₹10,000
User B reads → ₹10,000

User A withdraws ₹1,000
User B withdraws ₹2,000
```

Without proper concurrency control, one update may overwrite the other.

Locks help control how transactions access shared data.

---

# 1. Optimistic Locking
## Definition
- **Optimistic Locking** assumes that multiple transactions rarely modify the same record simultaneously.
- It does not lock the database row while reading; instead, it uses a **version number** to detect whether another transaction modified the record.

---

## Why Do We Need It?
It prevents **Lost Updates** without keeping database rows locked for long periods.

---

## Real-World Example
Two users open the same employee record.

```text
Employee

Salary = ₹50,000
Version = 1
```

Both users read:
```text
User A → Version 1
User B → Version 1
```

User A updates:
```text
Salary = ₹60,000
Version
1 → 2
```

User B tries to update:
```text
Expected Version = 1
Database Version = 2
↓
Version Mismatch
↓
OptimisticLockException
```
User B must refresh and retry.

---

## JPA Example

```java
@Entity
public class Employee {

    @Id
    private Long id;
    private String name;
    private double salary;
    @Version
    private Long version;
}
```

Hibernate generates an update similar to:

```sql
UPDATE employee
SET salary = ?, version = version + 1
WHERE id = ?
AND version = ?;
```

If no row is updated because the version changed, Hibernate detects the conflict.

---

## Use When

```text
✔ Reads are frequent
✔ Updates are less frequent
✔ Conflicts are rare
✔ High concurrency is required
```

Examples:

```text
Employee Profile
Customer Profile
Product Details
Configuration Data
```

---

# 2. Pessimistic Locking

## Definition
- **Pessimistic Locking** assumes that conflicts are likely, so the database record is locked before modification.
- Other transactions must wait until the transaction holding the lock commits or rolls back.

---

## Why Do We Need It?

Used when multiple users frequently modify the same data and allowing simultaneous updates would be dangerous.

---

## Real-World Example

### Last Ticket Booking

```text
Available Tickets = 1
```

User A

```text
Lock Ticket Row
↓
Book Ticket
```

User B
```text
Try to access same row
↓
WAIT
```

User A commits.
```text
Available Tickets = 0
```
User B then checks again and cannot book it.

---

## JPA Example

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Product p WHERE p.id = :id")
Optional<Product> findByIdForUpdate(@Param("id") Long id);
```
Conceptually, the database may use something similar to:

```sql
SELECT * FROM product WHERE id = ? FOR UPDATE;
```
---

## Use When
```text
✔ Conflicts are frequent
✔ Data is highly sensitive
✔ Immediate consistency is required
```

Examples:
```text
Ticket Booking
Inventory Reservation
Wallet Balance
Critical Financial Operations
```

---

# Optimistic vs Pessimistic Locking

| Optimistic                      | Pessimistic                      |
|---------------------------------|----------------------------------|
| Doesn't lock while reading      | Locks database data              |
| Uses version checking           | Uses database locks              |
| Better concurrency              | More blocking                    |
| Conflict detected during update | Conflict prevented before update |
| Good when conflicts are rare    | Good when conflicts are frequent |
| `@Version`                      | `@Lock(PESSIMISTIC_WRITE)`       |

---

# 3. Row-Level Lock

## Definition
- A **Row Lock** locks only the specific database row being modified.
- Other transactions can continue working with different rows in the same table.

---

## Why Do We Need It?
It provides concurrency without locking the entire table.

---

## Real-World Example

```text
Account Table
Account 101 → LOCKED
Account 102 → Available
Account 103 → Available
```

Transaction A updates Account 101.
Transaction B can still update Account 102.
---

## Example

```sql
SELECT * FROM account WHERE account_id = 101 FOR UPDATE;
```
---

## Use When

```text
✔ Updating individual records
✔ High concurrency is required
✔ Large tables
```

---

# 4. Table-Level Lock

## Definition
- A **Table Lock** locks an entire table instead of individual rows.
- Depending on the lock mode/database, other transactions may be prevented from performing conflicting operations on the table.

---

## Why Do We Need It?
Useful when a transaction modifies a large portion of a table and fine-grained row locking would be inefficient.

---

## Real-World Example

```text
Monthly Payroll Processing
↓
Lock Employee Salary Table
↓
Perform Bulk Update
↓
Commit
↓
Release Lock
```

---

## Disadvantage

```text
Table Locked
↓
Many Transactions Wait
↓
Concurrency Decreases
```

---

# Row Lock vs Table Lock

| Row Lock                      | Table Lock                                     |
|-------------------------------|------------------------------------------------|
| Locks specific rows           | Locks entire table                             |
| High concurrency              | Low concurrency                                |
| More lock-management overhead | Less lock-management overhead                  |
| Good for normal transactions  | Useful for some bulk/administrative operations |

---

# 5. Shared Lock

## Definition

- A **Shared Lock (S Lock)** allows multiple transactions to read the same data simultaneously.
- A conflicting exclusive modification generally cannot proceed until the shared locks are released.

---

## Real-World Example

```text
Account Balance

Reader A → Read ✔

Reader B → Read ✔

Reader C → Read ✔
```

Multiple users can read.

A conflicting writer may need to wait.

```text
Writer

↓

WAIT
```

---

## Easy Memory Trick

```text
Shared Lock

READ + READ

✔ Allowed

READ + Conflicting WRITE

✘ Blocked
```

---

# 6. Exclusive Lock

## Definition

- An **Exclusive Lock (X Lock)** is used when a transaction modifies data.
- Conflicting reads/writes that require incompatible locks must wait until the exclusive lock is released.

---

## Real-World Example

```text
Account Balance = ₹10,000

Transaction A

↓

Exclusive Lock

↓

Update Balance
```

Transaction B attempting a conflicting operation:

```text
WAIT
```

After Transaction A commits:

```text
Lock Released

↓

Transaction B Continues
```

---

## Easy Memory Trick

```text
Shared

→ Multiple compatible readers

Exclusive

→ Exclusive conflicting access
```

---

# Shared vs Exclusive Lock

| Shared Lock | Exclusive Lock |
|-------------|----------------|
| Mainly for reading | Mainly for writing |
| Multiple compatible shared locks possible | Conflicting locks are blocked |
| Allows concurrent readers | Protects modifications |
| S Lock | X Lock |

---

# 7. Deadlock

## Definition

- A **Deadlock** occurs when two or more transactions wait indefinitely for locks held by each other.
- Since every transaction is waiting for another one, none can continue.

---

## Real-World Example

Transaction A

```text
Locks Account A

↓

Needs Account B
```

Transaction B

```text
Locks Account B

↓

Needs Account A
```

Result:

```text
Transaction A

Account A 🔒
     ↓
Waiting for Account B


Transaction B

Account B 🔒
     ↓
Waiting for Account A


        DEADLOCK
```

---

## Banking Example

Transaction 1:

```sql
UPDATE account
SET balance = balance - 100
WHERE id = 1;

UPDATE account
SET balance = balance + 100
WHERE id = 2;
```

Transaction 2:

```sql
UPDATE account
SET balance = balance - 200
WHERE id = 2;

UPDATE account
SET balance = balance + 200
WHERE id = 1;
```

They acquire locks in opposite order.

```text
T1 locks Account 1

T2 locks Account 2

T1 waits for Account 2

T2 waits for Account 1

↓

Deadlock
```

---

# How Database Handles Deadlock

Most databases detect the deadlock.

```text
Deadlock Detected

↓

Choose One Transaction

↓

Rollback That Transaction

↓

Release Locks

↓

Other Transaction Continues
```

The rolled-back transaction may need to retry.

---

# How to Prevent Deadlocks

## 1. Lock Records in Same Order

Bad:

```text
Transaction A

Account 1 → Account 2


Transaction B

Account 2 → Account 1
```

Better:

```text
Transaction A

Account 1 → Account 2


Transaction B

Account 1 → Account 2
```

---

## 2. Keep Transactions Short

Avoid:

```text
Begin Transaction

↓

Database Update

↓

External API Call

↓

Wait 10 Seconds

↓

Commit
```

Prefer:

```text
Begin Transaction

↓

Database Operations

↓

Commit
```

---

## 3. Avoid Unnecessary Locks

Lock only the records required for the operation.

---

## 4. Use Proper Indexes

Without an appropriate index, the database may scan or lock more data than necessary, depending on the database and execution plan.

---

## 5. Handle Retry

Deadlocks can still happen in well-designed systems.

Applications should be able to retry appropriate failed transactions safely.

---

# Lock Granularity

## Definition

Lock granularity means **how much data is protected by a lock**.

```text
Database
   ↓
Table
   ↓
Page
   ↓
Row
```

Smaller lock

```text
Row Lock

↓

Higher Concurrency
```

Larger lock

```text
Table Lock

↓

Lower Concurrency
```

---

# Optimistic vs Pessimistic — Real-World Decision

## Employee Profile

```text
1000 employees

Few simultaneous updates
```

Use:

```text
Optimistic Locking
```

Reason:

```text
Conflict Probability = Low
```

---

## Last Concert Ticket

```text
1 Ticket

100 Users
```

Use:

```text
Pessimistic Locking
```

Reason:

```text
Conflict Probability = High
```

---

# JPA Lock Modes

Common JPA lock modes include:

```java
LockModeType.OPTIMISTIC

LockModeType.OPTIMISTIC_FORCE_INCREMENT

LockModeType.PESSIMISTIC_READ

LockModeType.PESSIMISTIC_WRITE

LockModeType.PESSIMISTIC_FORCE_INCREMENT
```

---

# PESSIMISTIC_READ

```text
Main Purpose

↓

Protect data while reading
```

Other compatible reads may be allowed depending on the database.

Conflicting writes are blocked.

---

# PESSIMISTIC_WRITE

```text
Main Purpose

↓

Read + Intend to Update
```

Conflicting transactions must wait.

---

# Spring Data JPA Example

```java
public interface ProductRepository
        extends JpaRepository<Product, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
           SELECT p
           FROM Product p
           WHERE p.id = :id
           """)
    Optional<Product> findForUpdate(
            @Param("id") Long id);
}
```

Service:

```java
@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(
            ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void purchase(Long productId) {

        Product product = repository
                .findForUpdate(productId)
                .orElseThrow();

        if (product.getStock() <= 0) {
            throw new IllegalStateException(
                    "Out of stock"
            );
        }

        product.setStock(
                product.getStock() - 1
        );
    }
}
```

Flow:

```text
Transaction Begins

↓

Product Row Locked

↓

Check Stock

↓

Decrease Stock

↓

Commit

↓

Lock Released
```

---

# Locking Problems

Locks improve consistency but can introduce:

```text
Blocking

↓

Long Wait Times

↓

Reduced Throughput

↓

Deadlocks
```

Therefore:

```text
Lock Only When Required

+

Keep Transaction Short
```

---

# Best Practices

✔ Prefer **Optimistic Locking** when conflicts are rare.

✔ Use **Pessimistic Locking** for highly contended critical records.

✔ Keep transactions short.

✔ Lock only required rows.

✔ Access records in a consistent order.

✔ Create appropriate indexes.

✔ Avoid external API calls while holding database locks.

✔ Implement safe retry logic for deadlock/optimistic-lock failures where appropriate.

✔ Monitor lock waits and deadlocks in production.

---

# Interview Questions

## What is Locking?

Locking is a database concurrency-control mechanism that controls simultaneous access to shared data and prevents conflicting operations.

---

## What is Optimistic Locking?

Optimistic locking doesn't hold a database lock while reading. It detects concurrent modifications using a version value.

```java
@Version
```

---

## What is Pessimistic Locking?

Pessimistic locking locks database data because it assumes concurrent conflicts are likely.

---

## Optimistic vs Pessimistic?

```text
Optimistic

→ Don't Lock While Reading
→ Detect Conflict Later


Pessimistic

→ Lock First
→ Prevent Conflict
```

---

## What is a Row Lock?

Locks only specific rows.

---

## What is a Table Lock?

Locks the entire table for the relevant conflicting operations.

---

## What is a Shared Lock?

Allows multiple compatible readers while preventing conflicting modifications.

---

## What is an Exclusive Lock?

Protects data being modified by preventing incompatible concurrent access.

---

## What is Deadlock?

Two or more transactions hold locks and wait for each other's locks, preventing all of them from progressing.

---

# Quick Revision

```text
DATABASE LOCKING

Optimistic Lock

→ No Lock While Reading
→ @Version
→ Conflict Detected Later
→ Best When Conflicts Are Rare


Pessimistic Lock

→ Lock Data First
→ Other Transaction Waits
→ Best When Conflicts Are Frequent


Row Lock

→ Lock Specific Row
→ High Concurrency


Table Lock

→ Lock Entire Table
→ Lower Concurrency


Shared Lock

→ Mainly Read
→ Multiple Compatible Readers


Exclusive Lock

→ Mainly Write
→ Blocks Conflicting Access


Deadlock

Transaction A

Lock A
↓

Wait B


Transaction B

Lock B
↓

Wait A

↓

DEADLOCK


BEST PRACTICES

✔ Short Transactions
✔ Consistent Lock Order
✔ Proper Indexes
✔ Lock Minimum Data
✔ Retry Appropriate Failures
```


# SQL Query Optimization

## Definition

- **SQL Query Optimization** is the process of improving SQL queries so the database can retrieve or modify data using **less CPU, memory, disk I/O, and execution time**.
- It involves writing efficient queries, creating proper indexes, analyzing execution plans, reducing unnecessary data retrieval, and choosing efficient joins and filters.

---

## Why Do We Need It?

A query may work perfectly with:

```text
1,000 Rows
```

but become very slow when the table grows to:

```text
1 Million Rows
10 Million Rows
100 Million Rows
```

Query optimization ensures the application remains fast as data grows.

---

## What Problem Does It Solve?

Poor queries can cause:

```text
Slow API Response

High CPU Usage

High Memory Usage

Full Table Scans

Database Locks

Connection Pool Exhaustion

Application Timeouts
```

---

# Real-World Example

Suppose the `employee` table contains:

```text
10 Million Employees
```

You execute:

```sql
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Without an index:

```text
Scan Row 1
Scan Row 2
Scan Row 3
...
Scan Row 10,000,000

↓

Find Employee
```

This is a **Full Table Scan**.

With an index:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

Now:

```text
Index

↓

Find Matching Location

↓

Read Employee

↓

Fast Result
```

---

# 1. Avoid `SELECT *`

## Bad

```sql
SELECT *
FROM employee;
```

This retrieves every column.

---

## Better

```sql
SELECT id, name, email
FROM employee;
```

---

## Why?

Reduces:

```text
Network Traffic

Memory Usage

Disk I/O

Object Mapping Cost
```

---

## Use When

Retrieve only the columns required by the application.

---

# 2. Use WHERE Clause Properly

## Definition

`WHERE` filters rows **before they are returned or processed by later query stages**.

---

## Bad

```sql
SELECT *
FROM employee;
```

Application:

```java
employees.stream()
        .filter(e -> e.getSalary() > 50000);
```

Database returns every employee.

---

## Better

```sql
SELECT id, name, salary
FROM employee
WHERE salary > 50000;
```

Filtering happens in the database.

---

## Best Practice

```text
Filter at Database

Not Application
```

whenever the database can efficiently perform the filtering.

---

# 3. Index Columns Used Frequently in WHERE

Suppose queries frequently use:

```sql
SELECT id, name
FROM employee
WHERE email = ?;
```

Create:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

---

## Good Candidates

Columns frequently used in:

```text
WHERE

JOIN

ORDER BY

GROUP BY
```

may benefit from indexes.

Whether an index helps depends on selectivity, table size, query pattern, and database optimizer.

---

# 4. Avoid Functions on Indexed Columns

Suppose:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

Potentially inefficient:

```sql
SELECT *
FROM employee
WHERE LOWER(email) = 'user@example.com';
```

Applying a function can prevent a normal index on `email` from being used efficiently, depending on the database.

---

## Better

Store normalized data when appropriate:

```sql
WHERE email = 'user@example.com';
```

Or use a database-supported functional/expression index when the function is required.

---

# 5. Avoid Calculations on Indexed Columns

Potentially inefficient:

```sql
WHERE salary + 1000 > 50000;
```

Better when logically equivalent:

```sql
WHERE salary > 49000;
```

This gives the optimizer a better chance to use an index on `salary`.

---

# 6. LIKE Query Optimization

## Usually Index-Friendly

```sql
WHERE name LIKE 'Man%'
```

Because the search starts with a known prefix.

---

## Often Expensive

```sql
WHERE name LIKE '%Man%'
```

The leading wildcard commonly prevents efficient use of a normal B-tree index.

---

## Use When

For substring/full-text search requirements, consider database-specific:

```text
Full-Text Search

Specialized Text Index

Elasticsearch / OpenSearch
```

when appropriate.

---

# 7. WHERE vs HAVING

## WHERE

Filters rows **before grouping**.

```sql
SELECT department, AVG(salary)
FROM employee
WHERE active = true
GROUP BY department;
```

---

## HAVING

Filters groups **after GROUP BY**.

```sql
SELECT department, AVG(salary)
FROM employee
GROUP BY department
HAVING AVG(salary) > 50000;
```

---

## Best Practice

If a condition doesn't depend on an aggregate, normally put it in `WHERE`.

Bad:

```sql
SELECT department, COUNT(*)
FROM employee
GROUP BY department
HAVING department = 'IT';
```

Prefer:

```sql
SELECT department, COUNT(*)
FROM employee
WHERE department = 'IT'
GROUP BY department;
```

---

# WHERE vs HAVING

| WHERE | HAVING |
|-------|--------|
| Filters rows | Filters groups |
| Before grouping | After grouping |
| Cannot directly filter aggregate results | Used with aggregate conditions |
| Usually reduces rows earlier | Applied after grouping |

---

# 8. IN

## Definition

`IN` checks whether a value matches one of several values.

---

## Example

```sql
SELECT *
FROM employee
WHERE department_id IN (10, 20, 30);
```

Equivalent conceptually to:

```sql
department_id = 10
OR department_id = 20
OR department_id = 30
```

---

## Use When

Checking against a relatively small set of known values.

---

# 9. EXISTS

## Definition

`EXISTS` checks whether a subquery returns **at least one matching row**.

---

## Example

Find customers who have orders:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

The database only needs to determine whether a match exists.

---

# EXISTS vs IN

Example using `IN`:

```sql
SELECT *
FROM customer
WHERE id IN (
    SELECT customer_id
    FROM orders
);
```

Using `EXISTS`:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

---

## General Guideline

```text
Small Fixed Value List
→ IN

Existence Check Against Related Table
→ EXISTS
```

Modern optimizers can often transform these queries, so **check the execution plan rather than assuming one is always faster**.

Also be careful with:

```sql
NOT IN (...)
```

when the subquery can return `NULL`; `NOT EXISTS` is often safer semantically.

---

# 10. JOIN Optimization

## Definition

A `JOIN` combines related records from multiple tables.

Poorly designed joins can become expensive when tables contain large amounts of data.

---

## Example

```sql
SELECT e.name,
       d.name
FROM employee e
JOIN department d
    ON e.department_id = d.id;
```

---

## Important Index

For this relationship:

```text
employee.department_id

↓

department.id
```

an index on the foreign-key/join column can often help:

```sql
CREATE INDEX idx_employee_department
ON employee(department_id);
```

The primary key `department.id` is normally already indexed.

---

# JOIN Types

## INNER JOIN

Returns matching rows.

```sql
SELECT *
FROM employee e
INNER JOIN department d
ON e.department_id = d.id;
```

---

## LEFT JOIN

Returns every row from the left table plus matching rows from the right.

```sql
SELECT *
FROM employee e
LEFT JOIN department d
ON e.department_id = d.id;
```

---

## Best Practice

Don't use:

```text
LEFT JOIN
```

when:

```text
INNER JOIN
```

correctly expresses the requirement.

Returning unnecessary unmatched rows can increase work.

---

# 11. Avoid Unnecessary JOINs

Bad:

```sql
SELECT e.name
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

If nothing from `department` is needed and the join isn't required for filtering or correctness, remove it:

```sql
SELECT e.name
FROM employee e;
```

---

# 12. JOIN vs Subquery

Sometimes:

```sql
SELECT *
FROM employee
WHERE department_id IN (
    SELECT id
    FROM department
    WHERE location = 'Mumbai'
);
```

can be expressed as:

```sql
SELECT e.*
FROM employee e
JOIN department d
    ON e.department_id = d.id
WHERE d.location = 'Mumbai';
```

Neither form is universally faster.

---

## Best Practice

```text
Write Clear SQL

↓

Check EXPLAIN Plan

↓

Measure Actual Performance
```

---

# 13. Composite Index

## Definition

A **Composite Index** contains multiple columns.

---

## Example

Frequently executed query:

```sql
SELECT *
FROM employee
WHERE department_id = ?
AND status = ?;
```

Possible index:

```sql
CREATE INDEX idx_employee_dept_status
ON employee(department_id, status);
```

---

## Important

Column order matters.

```text
(department_id, status)

is not equivalent to

(status, department_id)
```

The best order depends on query patterns, predicates, sorting, selectivity, and database behavior.

---

# 14. Covering Index

## Definition

A **Covering Index** contains all the data required to answer a query, allowing the database in some cases to avoid reading the base table.

---

## Example

Query:

```sql
SELECT name
FROM employee
WHERE email = ?;
```

An index designed to cover the query might include both:

```text
email
name
```

Exact syntax differs by database.

---

# 15. Avoid Too Many Indexes

Indexes improve reads but have a cost.

Every:

```text
INSERT

UPDATE

DELETE
```

may require index maintenance.

---

## Problem

```text
More Indexes

↓

Faster Some Reads

BUT

↓

Slower Writes

More Storage
```

---

## Best Practice

Create indexes based on actual query patterns, not on every column.

---

# 16. Execution Plan

## Definition

An **Execution Plan** shows how the database intends to execute a SQL query.

It helps identify expensive operations.

---

## Example

```sql
EXPLAIN
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Depending on the database, inspect for operations such as:

```text
Table / Sequential Scan

Index Scan

Index Seek

Nested Loop

Hash Join

Merge Join

Sort
```

---

# Full Table Scan

```text
Employee Table

↓

Read Many / All Rows

↓

Find Match
```

Can be expensive for large tables, although a full scan can still be the correct plan when a query needs a large percentage of the table.

---

# Index Access

```text
Index

↓

Locate Matching Rows

↓

Read Required Data
```

Often better for selective queries.

---

# 17. N+1 Query Problem

## Definition

The **N+1 problem** occurs when ORM executes:

```text
1 Query

+

N Additional Queries
```

to retrieve related data.

---

## Example

Load 100 departments:

```sql
SELECT *
FROM department;
```

Then Hibernate executes:

```text
SELECT * FROM employee WHERE department_id = 1;

SELECT * FROM employee WHERE department_id = 2;

...

SELECT * FROM employee WHERE department_id = 100;
```

Total:

```text
101 Queries
```

---

## Problem

```text
More DB Calls

↓

More Network Round Trips

↓

Slow Application
```

---

# Solution — Fetch Join

```java
@Query("""
       SELECT DISTINCT d
       FROM Department d
       LEFT JOIN FETCH d.employees
       """)
List<Department> findAllWithEmployees();
```

Conceptually:

```text
Department + Employees

↓

Fetched Together
```

---

# Other N+1 Solutions

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

Choose based on the use case rather than making every relationship eager.

---

# 18. Pagination

## Definition

Pagination retrieves a small portion of records instead of loading the entire dataset.

---

## Bad

```sql
SELECT *
FROM orders;
```

Imagine:

```text
10 Million Orders
```

Loading everything is expensive.

---

## Better

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 0;
```

---

## Spring Data JPA

```java
Pageable pageable =
        PageRequest.of(0, 20);

Page<Order> orders =
        repository.findAll(pageable);
```

---

# OFFSET Pagination Problem

For deep pages:

```sql
LIMIT 20 OFFSET 1000000;
```

the database may still need to walk/skip a large number of rows.

---

# Keyset Pagination

Instead of:

```sql
OFFSET 1000000
```

use the last seen key:

```sql
SELECT *
FROM orders
WHERE id > 1000000
ORDER BY id
LIMIT 20;
```

---

## Use When

```text
Large Data Sets

Infinite Scroll

High-Traffic APIs
```

---

# 19. ORDER BY Optimization

Query:

```sql
SELECT *
FROM orders
WHERE customer_id = ?
ORDER BY created_at DESC;
```

A useful index may be:

```sql
CREATE INDEX idx_order_customer_created
ON orders(customer_id, created_at);
```

This may allow filtering and ordering to be handled efficiently.

Always verify with the execution plan.

---

# 20. GROUP BY Optimization

Avoid grouping unnecessary data.

Bad:

```sql
SELECT department,
       COUNT(*)
FROM employee
GROUP BY department;
```

if you only need active employees.

Better:

```sql
SELECT department,
       COUNT(*)
FROM employee
WHERE active = true
GROUP BY department;
```

Filter first.

Then group.

---

# 21. Avoid Unnecessary DISTINCT

Bad:

```sql
SELECT DISTINCT name
FROM employee;
```

if duplicates don't matter or are impossible.

`DISTINCT` may require:

```text
Sort

OR

Hashing
```

Use only when required.

---

# 22. UNION vs UNION ALL

## UNION

```sql
SELECT email FROM customer

UNION

SELECT email FROM employee;
```

Removes duplicates.

This requires additional work.

---

## UNION ALL

```sql
SELECT email FROM customer

UNION ALL

SELECT email FROM employee;
```

Keeps duplicates.

Usually faster because duplicate removal is unnecessary.

---

## Best Practice

If duplicates are acceptable:

```text
Prefer UNION ALL
```

---

# 23. Avoid Large Result Sets

Bad API:

```text
GET /employees
```

returns:

```text
1,000,000 Records
```

Better:

```text
GET /employees?page=0&size=50
```

---

# 24. Batch Operations

Instead of:

```text
INSERT
INSERT
INSERT
INSERT
INSERT
```

use batching when supported:

```text
Batch

↓

100 / 500 / 1000 Operations

↓

Database
```

This reduces network round trips.

---

# 25. Query Only What You Need

Suppose API needs:

```text
Employee Name

Employee Email
```

Don't load a large entity graph unnecessarily.

Use DTO projection:

```java
public interface EmployeeSummary {

    String getName();

    String getEmail();
}
```

Repository:

```java
List<EmployeeSummary>
findByDepartmentId(Long departmentId);
```

---

# 26. Fetch Strategies

## LAZY

```text
Load Relationship

Only When Needed
```

Example:

```java
@OneToMany(fetch = FetchType.LAZY)
```

Usually preferred for collections.

---

## EAGER

```text
Load Relationship

Immediately
```

Can accidentally load large object graphs.

---

## Best Practice

```text
Prefer LAZY by default

↓

Fetch exactly what the use case needs
```

using:

```text
JOIN FETCH

EntityGraph

DTO Projection
```

---

# 27. Database Connection Pool

Opening a database connection for every request is expensive.

Spring Boot commonly uses:

```text
HikariCP
```

Flow:

```text
Application

↓

Connection Pool

↓

Existing Connection

↓

Database
```

---

## Best Practice

Monitor:

```text
Active Connections

Idle Connections

Waiting Threads

Connection Timeout

Query Duration
```

Don't blindly increase pool size; size it based on workload and database capacity.

---

# 28. Caching

Frequently read and rarely changed data may be cached.

```text
Application

↓

Cache

↓

Database
```

Possible options:

```text
Spring Cache

Redis

Hibernate Second-Level Cache
```

---

## Good Cache Candidates

```text
Country List

Configuration

Product Categories

Reference Data
```

Avoid caching highly volatile data without a clear invalidation strategy.

---

# 29. Prepared Statements

Use parameters:

```sql
SELECT *
FROM employee
WHERE email = ?;
```

instead of building SQL through string concatenation.

Benefits:

```text
SQL Injection Protection

Cleaner Query Handling

Potential Plan Reuse
```

---

# 30. Query Optimization Workflow

When an API is slow:

```text
1. Identify Slow Query

↓

2. Measure Execution Time

↓

3. Run EXPLAIN / EXPLAIN ANALYZE

↓

4. Check Scans

↓

5. Check Indexes

↓

6. Check JOINs

↓

7. Check Returned Rows

↓

8. Check N+1

↓

9. Optimize Query / Index

↓

10. Measure Again
```

---

# Query Best Practices

✔ Retrieve only required columns.

✔ Filter data in the database.

✔ Use indexes based on real query patterns.

✔ Index important join/filter columns where beneficial.

✔ Avoid functions on indexed columns unless using an appropriate functional index.

✔ Avoid leading-wildcard searches with normal B-tree indexes when performance matters.

✔ Use `EXISTS` naturally for existence checks.

✔ Use `WHERE` before grouping when possible.

✔ Avoid unnecessary joins.

✔ Avoid unnecessary `DISTINCT`.

✔ Prefer `UNION ALL` when duplicate removal isn't needed.

✔ Use pagination.

✔ Consider keyset pagination for deep/high-volume pagination.

✔ Detect and fix N+1 queries.

✔ Prefer targeted fetching over globally making associations EAGER.

✔ Use DTO projections when only a few columns are required.

✔ Use batch operations for large writes.

✔ Analyze execution plans.

✔ Measure before and after optimization.

---

# Interview Questions

## What is Query Optimization?

Query optimization is the process of improving SQL execution to reduce response time and database resource usage.

---

## WHERE vs HAVING?

```text
WHERE

→ Filters Rows
→ Before GROUP BY


HAVING

→ Filters Groups
→ After GROUP BY
```

---

## IN vs EXISTS?

```text
IN

→ Natural for matching against a list/set


EXISTS

→ Natural for checking whether a related row exists
```

Performance depends on the database optimizer and data.

---

## What is N+1?

```text
1 Parent Query

+

N Child Queries
```

Common solutions:

```text
JOIN FETCH

EntityGraph

Batch Fetching

DTO Projection
```

---

## What is an Execution Plan?

It shows how the database plans to execute a query, including scans, indexes, joins, sorts, and estimated costs.

---

## Why avoid SELECT *?

Because it can retrieve unnecessary columns, increasing I/O, memory, network traffic, and mapping work.

---

## Why can too many indexes be bad?

Because indexes require additional:

```text
Storage

INSERT Work

UPDATE Work

DELETE Work
```

---

# Quick Revision

```text
SQL QUERY OPTIMIZATION

SELECT

→ Select only required columns


WHERE

→ Filter Early


INDEX

→ Faster selective lookup


JOIN

→ Index appropriate join columns
→ Remove unnecessary joins


WHERE vs HAVING

WHERE
→ Rows

HAVING
→ Groups


IN

→ Value Set


EXISTS

→ Existence Check


N+1

→ 1 + N Queries

Solve With

JOIN FETCH
EntityGraph
Batch Fetching
DTO


Pagination

Small Dataset
→ OFFSET can be fine

Large / Deep Pagination
→ Keyset Pagination


Execution Plan

EXPLAIN
↓

Check Scan
↓

Check Index
↓

Check Join
↓

Optimize
↓

Measure Again
Golden Rule

Don't Guess
↓
Measure
↓
EXPLAIN
↓
Optimize
↓
Measure Again
```



# Database Indexing

## Definition

- An **Index** is a database data structure that helps the database **find rows faster without scanning the entire table**.
- It improves read/query performance but requires additional **storage** and adds overhead to `INSERT`, `UPDATE`, and `DELETE` operations.

---


## Why Do We Need Indexing?

Suppose an `employee` table contains:

```text
10 Million Rows
```

You execute:

```sql
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Without an index:

```text
Row 1
↓
Row 2
↓
Row 3
↓
...
Row 10,000,000

↓

Find Employee
```

The database may perform a **Full Table Scan**.

With an index:

```text
Index

↓

Find Matching Key

↓

Locate Row

↓

Return Employee
```

---

## What Problem Does It Solve?

Indexes help reduce:

```text
Full Table Scans

Slow Searches

Slow JOINs

Slow Sorting

Slow Filtering

High Disk I/O
```

---

# Real-World Example

Think about a book.

Without an index:

```text
Find "Transaction"

↓

Read Page 1
Read Page 2
Read Page 3
...
Read Page 1000
```

With an index:

```text
Index

Transaction → Page 450

↓

Go Directly to Page 450
```

A database index works on a similar idea.

---

# Basic Index Example

Table:

```sql
CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(200),
    department_id BIGINT,
    salary DECIMAL(10,2)
);
```

Query:

```sql
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Create index:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

Now the database may use:

```text
idx_employee_email

↓

Find Email

↓

Locate Employee
```

---

# How Index Works

Most traditional relational database indexes use structures such as:

```text
B-Tree / B+ Tree
```

Conceptually:

```text
                 50
               /    \
             25      75
            /  \    /  \
          10   30  60   90
```

Instead of scanning every value, the database navigates the tree.

Typical lookup complexity is approximately:

```text
O(log n)
```

rather than a linear scan:

```text
O(n)
```

Actual database behavior depends on the engine, query, and data distribution.

---

# Types of Indexes

---

# 1. Primary Key Index

## Definition

- A primary key uniquely identifies each row in a table.
- Databases normally create an index automatically for the primary key.

Example:

```sql
CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100)
);
```

Query:

```sql
SELECT *
FROM employee
WHERE id = 101;
```

Usually very fast because `id` is indexed.

---

## Use When

```text
Finding Record by ID

Updating Record by ID

Deleting Record by ID
```

---

# 2. Unique Index

## Definition

- A **Unique Index** prevents duplicate values while also providing indexed access.
- Commonly used for fields that must remain unique.

Example:

```sql
CREATE UNIQUE INDEX idx_employee_email
ON employee(email);
```

Now:

```text
user@example.com
user@example.com
```

cannot normally be inserted twice.

---

## Real-World Example

```text
Email

Username

Employee Number

Account Number
```

---

# 3. Single-Column Index

## Definition

An index created on one column.

Example:

```sql
CREATE INDEX idx_employee_salary
ON employee(salary);
```

Useful query:

```sql
SELECT *
FROM employee
WHERE salary > 50000;
```

Whether the optimizer uses it depends on how selective the query is.

---

# 4. Composite Index

## Definition

- A **Composite Index** contains multiple columns.
- It is useful when queries frequently filter, join, or sort using the same combination of columns.

Example:

```sql
CREATE INDEX idx_employee_dept_status
ON employee(department_id, status);
```

Useful query:

```sql
SELECT *
FROM employee
WHERE department_id = 10
AND status = 'ACTIVE';
```

---

# Column Order Matters ⭐

Index:

```text
(department_id, status)
```

is different from:

```text
(status, department_id)
```

For a typical B-tree composite index:

```text
(department_id, status)
```

can naturally support queries starting with the leftmost column.

Example:

```sql
WHERE department_id = 10
```

can often use it.

And:

```sql
WHERE department_id = 10
AND status = 'ACTIVE'
```

can often use it well.

But:

```sql
WHERE status = 'ACTIVE'
```

may not be able to use the index as effectively.

---

# Leftmost Prefix Rule

For index:

```text
(A, B, C)
```

Typical useful prefixes are:

```text
A

A + B

A + B + C
```

A query only on:

```text
B

C

B + C
```

usually cannot exploit the index in the same way.

Database-specific optimizer features can vary.

---

# Real-World Example

Index:

```sql
CREATE INDEX idx_order_customer_status
ON orders(customer_id, status);
```

Good:

```sql
WHERE customer_id = 101
```

Good:

```sql
WHERE customer_id = 101
AND status = 'PAID'
```

Less ideal for this index alone:

```sql
WHERE status = 'PAID'
```

---

# 5. Covering Index

## Definition

- A **Covering Index** contains all columns required to satisfy a query.
- The database may answer the query directly from the index without reading the underlying table rows.

---

## Example

Query:

```sql
SELECT name, email
FROM employee
WHERE department_id = 10;
```

A covering index might contain:

```text
department_id
name
email
```

Conceptually:

```text
Index

↓

department_id

name

email

↓

Result
```

No additional table lookup may be required.

---

## Benefits

```text
Less Disk I/O

Faster Reads

Fewer Table Lookups
```

---

# Clustered Index vs Non-Clustered Index

## Clustered Index

### Definition
A **Clustered Index** determines how the actual table data is organized based on the indexed column. A table generally has only **one clustered organization**.

### Real-World Example
Think of a **dictionary**:

```text
Apple
Ball
Cat
Dog
```

The actual words are stored in alphabetical order.

Similarly:

```text
Employee ID

101 → Manoj
102 → Rahul
103 → Amit
104 → Sneha
```

The data is organized by `id`.

---

## Non-Clustered Index

### Definition
A **Non-Clustered Index** is a separate structure that stores indexed values and a reference to the actual table row. A table can have **multiple non-clustered indexes**.

### Real-World Example
Think of the **index page of a book**:

```text
Java        → Page 10
Spring      → Page 50
Hibernate   → Page 80
```

The index doesn't contain the actual content; it tells you **where to find it**.

Similarly:

```text
Email Index

amit@gmail.com  → Employee 103
manoj@gmail.com → Employee 101
rahul@gmail.com → Employee 102
```

---

# Simple Difference

| Clustered | Non-Clustered |
|---|---|
| Organizes actual table data | Separate index structure |
| Generally one per table | Multiple possible |
| Like a dictionary | Like a book index |
| Good for range queries | Good for searching different columns |

## Memory Trick

```text
Clustered
→ Actual data is organized

Non-Clustered
→ Index points to actual data
```

# 8. Foreign Key Index

## Definition

A foreign key does **not universally guarantee** that an index is automatically created on the referencing column.

For frequently joined or parent-delete/update relationships, indexing foreign-key columns is often beneficial.

Example:

```text
Employee

department_id

↓

Department

id
```

Create:

```sql
CREATE INDEX idx_employee_department
ON employee(department_id);
```

Useful for:

```sql
SELECT e.*
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

---

# 9. Index for JOIN

Suppose:

```sql
SELECT e.name,
       d.name
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

Useful indexes commonly include:

```text
department.id
→ Primary Key Index

employee.department_id
→ Foreign Key Index
```

---

# 10. Index for ORDER BY

Query:

```sql
SELECT *
FROM orders
WHERE customer_id = 101
ORDER BY created_at DESC;
```

Potential index:

```sql
CREATE INDEX idx_order_customer_created
ON orders(customer_id, created_at);
```

The database may use the index for:

```text
Filtering

+

Ordering
```

reducing explicit sorting.

Verify with the execution plan.

---

# 11. Index for GROUP BY

Query:

```sql
SELECT department_id,
       COUNT(*)
FROM employee
GROUP BY department_id;
```

An index on:

```sql
CREATE INDEX idx_employee_department
ON employee(department_id);
```

may help some database engines process the grouping more efficiently.

Always verify with `EXPLAIN`.

---

# 12. Index Selectivity

## Definition

**Selectivity** describes how well a column distinguishes rows.

High selectivity:

```text
Email

Account Number

Employee ID
```

Many unique values.

Usually good index candidates.

---

Low selectivity:

```text
Gender

Boolean Status

Active = true/false
```

Very few unique values.

A standalone index may provide limited benefit when a query returns a large portion of the table.

---

# Example

10 million employees.

Column:

```text
active

true  = 9,500,000
false =   500,000
```

Query:

```sql
WHERE active = true
```

returns most of the table.

The optimizer may prefer a table scan.

---

# 13. Index Scan vs Table Scan

## Table Scan

```text
Table

↓

Read Many / All Rows

↓

Find Matches
```

---

## Index Access

```text
Index

↓

Find Matching Keys

↓

Fetch Required Rows
```

Indexes are usually valuable when the predicate is sufficiently selective.

---

# 14. Index Seek vs Index Scan

Terminology varies by database.

Conceptually:

## Index Seek

```text
Find Exact / Narrow Range

↓

Directly Navigate Index
```

Usually efficient.

---

## Index Scan

```text
Read Large Portion of Index

↓

Check Entries
```

Can still be perfectly valid for queries returning many rows.

---

# 15. Functions on Indexed Columns

Suppose:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

Potentially problematic:

```sql
WHERE LOWER(email) = 'user@example.com'
```

The normal index may not be usable efficiently.

---

## Solutions

Normalize data where appropriate:

```sql
WHERE email = 'user@example.com'
```

Or use a database-supported:

```text
Functional Index

Expression Index
```

Example syntax in databases that support expression indexes:

```sql
CREATE INDEX idx_employee_lower_email
ON employee(LOWER(email));
```

---

# 16. LIKE and Index

Usually index-friendly:

```sql
WHERE name LIKE 'Man%'
```

Because the prefix is known.

---

Usually harder for a normal B-tree index:

```sql
WHERE name LIKE '%Man%'
```

because the beginning of the value is unknown.

---

## For Text Search

Consider:

```text
Full-Text Index

Elasticsearch

OpenSearch
```

when substring or natural-language search is a core requirement.

---

# 17. Too Many Indexes

Indexes improve reads but make writes more expensive.

Suppose:

```text
Employee Table

10 Indexes
```

Every:

```sql
INSERT
```

may require updates to multiple index structures.

Same for:

```text
UPDATE

DELETE
```

---

## Trade-Off

```text
More Indexes

↓

Faster Reads

BUT

↓

Slower Writes

More Storage

More Maintenance
```

---

# 18. When NOT to Create an Index

Avoid blindly indexing:

```text
Very Small Tables

Columns Rarely Queried

Very Low-Selectivity Columns by Themselves

Frequently Updated Columns Without Read Benefit

Every Column
```

Indexes should be created based on actual query patterns.

---

# 19. Index and INSERT Performance

Without many indexes:

```text
INSERT

↓

Write Row
```

With many indexes:

```text
INSERT

↓

Write Row

↓

Update Index 1

↓

Update Index 2

↓

Update Index 3

...
```

Therefore heavy-write systems need careful index design.

---

# 20. Index and UPDATE

Suppose:

```sql
UPDATE employee
SET salary = 60000
WHERE id = 101;
```

If `salary` is indexed, the database may also need to update the salary index.

Indexes on frequently modified columns can therefore increase write cost.

---

# 21. Index and DELETE

```sql
DELETE FROM employee
WHERE id = 101;
```

Database must remove:

```text
Table Row

+

Related Index Entries
```

---

# 22. Pagination and Index

Bad deep pagination:

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 1000000;
```

Even with an index, deep offsets may become expensive.

---

## Keyset Pagination

```sql
SELECT *
FROM orders
WHERE id > 1000000
ORDER BY id
LIMIT 20;
```

Index:

```text
id
```

makes this pattern efficient.

---

# 23. Composite Index for Real-World Query

Suppose an e-commerce application frequently executes:

```sql
SELECT id,
       order_date,
       total
FROM orders
WHERE customer_id = ?
AND status = 'PAID'
ORDER BY order_date DESC;
```

Potential index:

```sql
CREATE INDEX idx_order_customer_status_date
ON orders(
    customer_id,
    status,
    order_date
);
```

Conceptually:

```text
customer_id

↓

status

↓

order_date

↓

Result
```

This may support:

```text
Filtering

+

Filtering

+

Ordering
```

Actual usefulness should be confirmed with the execution plan.

---

# 24. Index Maintenance

Indexes can become less efficient over time depending on the database and workload.

Database-specific maintenance may include:

```text
Statistics Update

Index Rebuild

Index Reorganization

VACUUM / ANALYZE
```

The correct operation depends on the database.

---

# 25. Statistics

## Definition

Database statistics describe the distribution of data.

The optimizer uses statistics to decide:

```text
Use Index?

OR

Use Table Scan?

Which JOIN?

Which Table First?
```

Outdated statistics can lead to poor execution plans.

---

# 26. How to Check Whether Index Is Used

Use:

```sql
EXPLAIN
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Or database-specific runtime analysis such as:

```sql
EXPLAIN ANALYZE
```

Look for:

```text
Index Seek

Index Scan

Table / Sequential Scan

Estimated Rows

Actual Rows

Execution Cost / Time
```

---

# Index Optimization Workflow

```text
Slow Query

↓

Check WHERE / JOIN / ORDER BY

↓

Run EXPLAIN

↓

Check Table Scan

↓

Check Existing Indexes

↓

Design Index

↓

Run Query Again

↓

Compare Execution Plan

↓

Measure Performance
```

---

# JPA Index Example

```java
@Entity
@Table(
    name = "employee",
    indexes = {
        @Index(
            name = "idx_employee_email",
            columnList = "email"
        ),
        @Index(
            name = "idx_employee_department_status",
            columnList = "department_id,status"
        )
    }
)
public class Employee {

    @Id
    private Long id;

    private String name;

    private String email;

    private Long departmentId;

    private String status;
}
```

---

# Index Best Practices

✔ Index frequently searched columns when selective enough.

✔ Index important JOIN columns.

✔ Consider indexes supporting `ORDER BY`.

✔ Use composite indexes for common multi-column query patterns.

✔ Understand the leftmost-prefix behavior of composite B-tree indexes.

✔ Prefer high-selectivity columns when appropriate.

✔ Avoid creating indexes blindly.

✔ Avoid unnecessary indexes on heavily updated columns.

✔ Use covering indexes for critical read-heavy queries when beneficial.

✔ Monitor unused indexes.

✔ Keep optimizer statistics current.

✔ Use `EXPLAIN` / `EXPLAIN ANALYZE`.

✔ Measure before and after adding an index.

---

# Interview Questions

## What is an Index?

An index is a database data structure that speeds up data retrieval by providing a faster lookup path to rows.

---

## What is the disadvantage of an index?

```text
Extra Storage

Slower INSERT

Slower UPDATE

Slower DELETE

Maintenance Overhead
```

---

## What is a Composite Index?

An index containing multiple columns.

Example:

```text
(customer_id, status)
```

---

## Why does column order matter?

Because B-tree composite indexes are organized from the leftmost indexed column onward, so different column orders support different query patterns.

---

## What is a Covering Index?

An index containing all data required by a query so the database may answer it without reading the base table.

---

## What is Index Selectivity?

How uniquely an indexed value identifies rows.

```text
High Selectivity

→ Email

→ ID


Low Selectivity

→ Boolean

→ Gender
```

---

## Does a Foreign Key automatically create an index?

Not in every database.

The foreign-key column often needs an explicit index when query and relationship patterns benefit from it.

---

## Can too many indexes cause problems?

Yes.

They consume storage and make write operations more expensive.

---

## When should we create indexes?

Commonly for columns used frequently in:

```text
WHERE

JOIN

ORDER BY

GROUP BY
```

when execution plans and workload measurements show that the index is beneficial.

---

# Quick Revision

```text
DATABASE INDEX

Index

↓

Faster Lookup

↓

Less Scanning


Common Types

Primary Key Index

Unique Index

Single Column Index

Composite Index

Covering Index

Clustered Index

Non-Clustered Index


Composite Index

(A, B, C)

Typical Prefixes

A ✔

A + B ✔

A + B + C ✔


Index Good For

WHERE

JOIN

ORDER BY

GROUP BY


High Selectivity

Email

Account Number

ID


Low Selectivity

Boolean

Gender


Index Trade-Off

Faster Reads

BUT

Slower Writes

+

More Storage


Golden Rule

Don't Create Index Blindly

↓

Find Slow Query

↓

EXPLAIN

↓

Design Index

↓

Measure Again
```


# SQL Execution Plans

## Definition

- An **Execution Plan** shows how the database plans to execute a SQL query.
- It explains which tables, indexes, joins, scans, sorts, and access paths the database will use to return the result.

---

## Why Do We Need It?

A SQL query may look simple but still execute slowly.

Execution plans help us understand:

```text
Why is the query slow?

Is an index being used?

Is the database scanning the whole table?

Which JOIN is expensive?

Is sorting consuming time?
```

---

## What Problem Does It Solve?

Without an execution plan:

```text
Slow Query

↓

Guess the Problem

↓

Add Random Indexes

↓

Maybe Improve / Maybe Worse
```

With an execution plan:

```text
Slow Query

↓

EXPLAIN

↓

Find Expensive Operation

↓

Optimize

↓

Measure Again
```

---

# Real-World Example

Suppose:

```text
employee table

10 Million Rows
```

Query:

```sql
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Without an index, the plan may show:

```text
Sequential Scan / Table Scan

↓

Read 10 Million Rows

↓

Find Match
```

After creating:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

the plan may change to:

```text
Index Scan / Index Seek

↓

Locate Email

↓

Return Row
```

---

# How to View Execution Plan

## PostgreSQL

```sql
EXPLAIN
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

To actually execute and measure:

```sql
EXPLAIN ANALYZE
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

---

## MySQL

```sql
EXPLAIN
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

---

## Oracle

```sql
EXPLAIN PLAN FOR
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

---

# EXPLAIN vs EXPLAIN ANALYZE

## EXPLAIN

- Shows the **estimated execution plan**.
- Does not normally execute the query.

```text
Estimated Rows

Estimated Cost

Expected Access Path
```

---

## EXPLAIN ANALYZE

- Executes the query and shows the **actual execution statistics**.
- Useful for comparing estimates with real behavior.

```text
Actual Rows

Actual Time

Loops

Execution Time
```

---

# Important Execution Plan Operations

---

# 1. Full Table Scan / Sequential Scan

## Definition

The database reads a large portion or all rows in the table to find matching records.

---

## Example

```sql
SELECT *
FROM employee
WHERE email = 'user@example.com';
```

Without a useful index:

```text
Employee Table

↓

Row 1

↓

Row 2

↓

...

↓

Row 10,000,000
```

---

## Is Table Scan Always Bad?

No.

A table scan may be correct when:

```text
Table is Small

Query Returns Most Rows

Index Lookup Would Cost More
```

---

# 2. Index Seek

## Definition

An **Index Seek** directly navigates an index to find a small set of matching rows.

---

## Example

```sql
SELECT *
FROM employee
WHERE id = 101;
```

Conceptually:

```text
Index

↓

Find 101

↓

Return Employee
```

---

## Usually Good For

```text
Primary Key Lookup

Unique Key Lookup

Highly Selective WHERE Conditions
```

---

# 3. Index Scan

## Definition

An **Index Scan** reads a larger portion of an index rather than directly seeking a narrow value.

---

## Example

```sql
SELECT email
FROM employee
ORDER BY email;
```

The database may scan the email index.

---

## Important

```text
Index Seek

→ Usually Narrow Lookup

Index Scan

→ Reads Larger Part of Index
```

An index scan is not automatically bad.

---

# 4. Nested Loop Join

## Definition

A **Nested Loop Join** takes rows from one table and repeatedly searches the other table for matches.

---

## Flow

```text
Table A Row 1

↓

Search Table B

↓

Table A Row 2

↓

Search Table B
```

---

## Good When

```text
One Table is Small

+

Other Table Has Good Index
```

---

## Example

```sql
SELECT *
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.id = 101;
```

If only one order is returned, a nested loop can be efficient.

---

# 5. Hash Join

## Definition

A **Hash Join** builds a hash table from one input and uses it to find matches from the other input.

---

## Flow

```text
Small Table

↓

Build Hash Table

↓

Large Table

↓

Probe Hash Table

↓

Matches
```

---

## Good When

```text
Large Data Sets

Equality JOIN

No Useful Ordered Index
```

---

# 6. Merge Join

## Definition

A **Merge Join** combines two sorted inputs by walking through them in order.

---

## Good When

```text
Both Inputs Are Sorted

JOIN Columns Are Indexed / Ordered

Large Data Sets
```

---

# JOIN Comparison

| Join Type | Best Use |
|-----------|----------|
| Nested Loop | Small result + indexed lookup |
| Hash Join | Large equality joins |
| Merge Join | Large sorted datasets |

The optimizer chooses based on statistics and cost.

---

# 7. Sort Operation

## Definition

A sort appears when the database must order rows.

Example:

```sql
SELECT *
FROM orders
ORDER BY created_at DESC;
```

Execution plan may show:

```text
Table Scan

↓

Sort

↓

Result
```

---

## Optimization

Potential index:

```sql
CREATE INDEX idx_orders_created
ON orders(created_at);
```

Then the optimizer may avoid a separate sort.

---

# 8. Filter

## Definition

A **Filter** means the database reads rows and then checks whether they satisfy a condition.

Example:

```sql
WHERE salary > 50000
```

If many rows are read but only a few survive:

```text
Rows Read = 1,000,000

Rows Returned = 100
```

you should check whether a useful index exists.

---

# 9. Cost

## Definition

The **Cost** is the optimizer's estimate of how expensive an operation is.

It can consider:

```text
CPU

Disk I/O

Memory

Rows Processed
```

---

## Important

Cost is usually:

```text
Relative Estimate
```

not:

```text
Milliseconds
```

---

# 10. Estimated Rows

## Definition

Estimated rows tell how many rows the optimizer expects an operation to return.

Example:

```text
Estimated Rows = 10

Actual Rows = 1,000,000
```

This large difference may indicate:

```text
Outdated Statistics

Bad Cardinality Estimate

Data Skew
```

---

# 11. Actual Rows

Available with tools such as:

```sql
EXPLAIN ANALYZE
```

It shows how many rows were actually processed.

---

# Estimated vs Actual Rows

## Good

```text
Estimated = 100

Actual = 120
```

Reasonably close.

---

## Problem

```text
Estimated = 10

Actual = 1,000,000
```

The optimizer may choose a poor plan because its estimate was wrong.

---

# 12. Cardinality

## Definition

**Cardinality** represents the number of rows expected at a step in the plan.

The optimizer uses cardinality to decide:

```text
Which JOIN?

Which Index?

Which Table First?
```

---

# 13. Statistics

## Definition

Database statistics describe how data is distributed in columns and indexes.

The optimizer uses statistics to estimate cardinality.

---

## Problem

Old statistics:

```text
Optimizer Thinks

100 Rows

Reality

10 Million Rows
```

Possible result:

```text
Bad Execution Plan
```

---

# 14. Predicate Pushdown

## Definition

Predicate pushdown means applying filters as early as possible so fewer rows move through later operations.

---

## Example

Better:

```sql
SELECT department_id,
       COUNT(*)
FROM employee
WHERE active = true
GROUP BY department_id;
```

Flow:

```text
Filter

↓

Reduce Rows

↓

Group
```

Instead of grouping unnecessary rows first.

---

# 15. Covering Index in Execution Plan

Query:

```sql
SELECT email
FROM employee
WHERE department_id = 10;
```

If an index contains all required columns:

```text
department_id

email
```

the plan may use only the index.

```text
Index-Only Scan

↓

No Table Lookup
```

This can reduce I/O.

---

# 16. Key Lookup / Bookmark Lookup

Sometimes:

```text
Index Finds Row Key

↓

Database Goes Back to Table

↓

Gets Missing Columns
```

If this happens thousands of times, it can become expensive.

---

## Possible Solution

A covering index may help.

But don't create wide indexes blindly.

---

# 17. Execution Plan for JOIN

Query:

```sql
SELECT o.id,
       c.name
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.status = 'PAID';
```

Possible flow:

```text
Orders

↓

Filter status = PAID

↓

JOIN Customer

↓

Return Result
```

Check whether:

```text
orders.status

orders.customer_id

customer.id
```

have useful indexes.

---

# 18. Bad Execution Plan Example

Suppose:

```text
Orders = 50 Million Rows
```

Plan:

```text
Full Table Scan

↓

50 Million Rows

↓

Sort

↓

JOIN

↓

Return 20 Rows
```

This is suspicious.

---

## Better Plan

```text
Index Seek

↓

20 Matching Rows

↓

Nested Loop

↓

Customer Lookup

↓

Return 20 Rows
```

---

# 19. Execution Plan and N+1

Execution plans only explain individual SQL queries.

If Hibernate executes:

```text
1 Query

+

100 Queries
```

each query may individually look fast.

But together:

```text
101 DB Round Trips
```

still cause poor performance.

Therefore also monitor:

```text
Number of Queries
```

not only individual execution plans.

---

# 20. Execution Plan Optimization Workflow

```text
API is Slow

↓

Find SQL Query

↓

Measure Query Time

↓

Run EXPLAIN

↓

Check:

Table Scan?

Index?

JOIN Type?

Sort?

Rows?

↓

Run EXPLAIN ANALYZE

↓

Compare Estimated vs Actual

↓

Check Statistics

↓

Optimize Query / Index

↓

Test Again
```

---

# What Should You Check First?

## 1. Scan Type

```text
Table Scan?

Index Scan?

Index Seek?
```

---

## 2. Rows Processed

```text
Rows Read

vs

Rows Returned
```

Huge difference may indicate inefficient filtering.

---

## 3. JOIN Operations

```text
Nested Loop

Hash Join

Merge Join
```

Check whether the chosen join fits the data size.

---

## 4. Sorting

Check expensive:

```text
SORT
```

operations.

Maybe an index can support the required order.

---

## 5. Estimates

Compare:

```text
Estimated Rows

vs

Actual Rows
```

---

## 6. Index Usage

Check:

```text
Missing Index

Unused Index

Wrong Composite Index Order
```

---

# Real-World Example

API:

```text
GET /customers/{id}/orders
```

Response time:

```text
8 Seconds
```

Query:

```sql
SELECT *
FROM orders
WHERE customer_id = 101
ORDER BY created_at DESC;
```

Execution Plan:

```text
Sequential Scan

↓

5 Million Rows

↓

Filter customer_id

↓

Sort

↓

Return 50 Rows
```

Create:

```sql
CREATE INDEX idx_order_customer_created
ON orders(customer_id, created_at);
```

New Plan:

```text
Index Scan

↓

Customer 101

↓

Already Ordered

↓

Return 50 Rows
```

Result:

```text
Less I/O

No Large Sort

Faster API
```

---

# Execution Plan Best Practices

✔ Use `EXPLAIN` for slow queries.

✔ Use `EXPLAIN ANALYZE` carefully to get actual runtime information.

✔ Check estimated vs actual rows.

✔ Look for unnecessary full scans.

✔ Don't assume every table scan is bad.

✔ Check JOIN algorithms.

✔ Check expensive sort operations.

✔ Verify indexes are actually used.

✔ Keep optimizer statistics current.

✔ Check how many rows are read vs returned.

✔ Fix N+1 separately from individual query plans.

✔ Measure before and after optimization.

---

# Interview Questions

## What is an Execution Plan?

An execution plan shows how the database intends to execute a SQL query, including indexes, scans, joins, sorting, and estimated costs.

---

## What is EXPLAIN?

`EXPLAIN` displays the optimizer's estimated execution plan.

---

## What is EXPLAIN ANALYZE?

It executes the query and reports actual runtime information such as rows and execution time.

---

## Is a Full Table Scan always bad?

No.

It can be efficient for small tables or queries returning a large percentage of the rows.

---

## Index Seek vs Index Scan?

```text
Index Seek

→ Navigate directly to matching keys


Index Scan

→ Read a larger portion of the index
```

---

## What is a Nested Loop?

For each row from one input, the database searches the other input for matches.

Best for small outer results with efficient inner lookups.

---

## What is a Hash Join?

Builds a hash structure from one input and probes it using rows from another input.

Common for large equality joins.

---

## Why are statistics important?

They help the optimizer estimate row counts and choose indexes, join types, and execution order.

---

# Quick Revision

```text
EXECUTION PLAN

SQL Query

↓

Optimizer

↓

Execution Plan

↓

Database Executes


CHECK

1. Table Scan

2. Index Scan / Seek

3. JOIN Type

4. Sort

5. Rows Processed

6. Estimated vs Actual

7. Index Usage


JOIN TYPES

Nested Loop
→ Small + Indexed

Hash Join
→ Large Equality Join

Merge Join
→ Sorted Inputs


TOOLS

EXPLAIN
→ Estimated Plan

EXPLAIN ANALYZE
→ Actual Execution


BAD SIGN

Read 1,000,000 Rows

↓

Return 10 Rows


GOLDEN RULE

Don't Guess

↓

EXPLAIN

↓

Measure

↓

Optimize

↓

Measure Again
```


# Join Optimization

## Definition

- **Join Optimization** is the process of improving SQL queries that combine data from multiple tables so they execute with less CPU, memory, disk I/O, and time.
- It mainly involves using the correct join type, proper indexes, filtering early, reducing unnecessary rows, and checking the execution plan.

---

## Why Do We Need It?

JOINs can become expensive when tables contain millions of rows.

Example:

```text
Orders Table     = 10 Million Rows
Customer Table   = 1 Million Rows
```

A poorly written JOIN can force the database to process huge amounts of data.

---

## What Problem Does It Solve?

Poor JOINs can cause:

```text
Slow API Response

High CPU

High Memory

Large Table Scans

Expensive Sorts

Long Lock Time

Database Timeouts
```

---

# Real-World Example

Suppose you want customer name and order information.

```sql
SELECT c.name,
       o.id,
       o.total
FROM customer c
JOIN orders o
ON c.id = o.customer_id;
```

Relationship:

```text
Customer

id = 101

↓

Orders

customer_id = 101
```

If `orders.customer_id` is not indexed, the database may scan many order rows.

Create:

```sql
CREATE INDEX idx_orders_customer
ON orders(customer_id);
```

Now the database can find matching orders faster.

---

# 1. Index JOIN Columns

## Definition

Columns used in JOIN conditions should often have appropriate indexes.

---

## Example

```sql
SELECT e.name,
       d.name
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

Useful indexes:

```text
department.id
→ Usually Primary Key Index

employee.department_id
→ Foreign Key Index
```

Create:

```sql
CREATE INDEX idx_employee_department
ON employee(department_id);
```

---

## Why?

Without index:

```text
Employee

↓

Search Department Repeatedly

↓

More Scanning
```

With index:

```text
Index

↓

Find Matching Department

↓

Faster Join
```

---

# 2. Choose Correct JOIN Type

## INNER JOIN

Returns only matching rows.

```sql
SELECT *
FROM employee e
INNER JOIN department d
ON e.department_id = d.id;
```

Use when:

```text
Only matching data is required.
```

---

## LEFT JOIN

Returns:

```text
All Left Rows

+

Matching Right Rows
```

Example:

```sql
SELECT *
FROM employee e
LEFT JOIN department d
ON e.department_id = d.id;
```

Use when employees must be returned even if they have no department.

---

## Best Practice

Don't use:

```text
LEFT JOIN
```

when:

```text
INNER JOIN
```

is enough.

Unnecessary outer joins may increase work and make optimization harder.

---

# 3. Filter Early

## Bad

```sql
SELECT *
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

Then filter in application:

```java
employees.stream()
        .filter(e -> e.getStatus().equals("ACTIVE"));
```

---

## Better

```sql
SELECT e.id,
       e.name,
       d.name
FROM employee e
JOIN department d
ON e.department_id = d.id
WHERE e.status = 'ACTIVE';
```

---

## Why?

```text
Filter First

↓

Fewer Rows

↓

Join Less Data

↓

Better Performance
```

---

# 4. Avoid SELECT *

## Bad

```sql
SELECT *
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

This may retrieve:

```text
Employee Columns

+

Department Columns

+

Unused Columns
```

---

## Better

```sql
SELECT e.id,
       e.name,
       d.name
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

---

## Benefits

```text
Less Network Data

Less Memory

Less Disk I/O

Less Mapping Cost
```

---

# 5. Avoid Unnecessary JOINs

## Bad

```sql
SELECT e.name
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

If you don't use:

```text
department
```

and the join is not required for filtering or correctness, remove it.

Better:

```sql
SELECT e.name
FROM employee e;
```

---

# 6. JOIN Small Result Sets

Try to reduce the amount of data before joining.

Example:

```sql
SELECT o.id,
       c.name
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.status = 'PAID'
AND o.created_at >= CURRENT_DATE - INTERVAL '7 days';
```

Flow:

```text
Orders

↓

Filter PAID

↓

Filter Last 7 Days

↓

JOIN Customer
```

Better than joining millions of historical orders first.

---

# 7. Composite Index for JOIN + Filter

Query:

```sql
SELECT o.id,
       c.name
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.status = 'PAID';
```

Potential index:

```sql
CREATE INDEX idx_orders_status_customer
ON orders(status, customer_id);
```

This may help:

```text
Filter status

+

Join customer
```

Actual usefulness depends on selectivity and execution plan.

---

# 8. JOIN + ORDER BY Optimization

Query:

```sql
SELECT o.id,
       c.name,
       o.created_at
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.customer_id = 101
ORDER BY o.created_at DESC;
```

Potential index:

```sql
CREATE INDEX idx_orders_customer_created
ON orders(customer_id, created_at);
```

This may help with:

```text
Filtering

+

Ordering

+

Join Lookup
```

---

# 9. JOIN vs Subquery

## Subquery

```sql
SELECT *
FROM employee
WHERE department_id IN (
    SELECT id
    FROM department
    WHERE location = 'Mumbai'
);
```

---

## JOIN Version

```sql
SELECT e.*
FROM employee e
JOIN department d
ON e.department_id = d.id
WHERE d.location = 'Mumbai';
```

---

## Which Is Faster?

There is no universal answer.

Modern optimizers may convert both into similar plans.

---

## Best Practice

```text
Write Clear Query

↓

EXPLAIN

↓

Compare Plans

↓

Measure
```

---

# 10. EXISTS for Existence Checks

Suppose you only need customers who have at least one order.

Instead of joining and returning duplicates:

```sql
SELECT DISTINCT c.*
FROM customer c
JOIN orders o
ON c.id = o.customer_id;
```

You can use:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

---

## Why?

If the requirement is:

```text
Does at least one matching row exist?
```

`EXISTS` expresses the intent directly.

---

# 11. Avoid Duplicate Explosion

Suppose:

```text
Customer

1 row

↓

Orders

100 rows

↓

Order Items

10 items each
```

Query:

```sql
SELECT *
FROM customer c
JOIN orders o
ON c.id = o.customer_id
JOIN order_item i
ON o.id = i.order_id;
```

Possible result:

```text
1 Customer

↓

100 Orders

↓

1000 Joined Rows
```

This is called row multiplication.

---

## Problem

```text
More Data

More Memory

More Network

More Mapping Work
```

---

## Solution

Fetch only what is required.

Use:

```text
DTO Projection

Separate Optimized Queries

Aggregation

Pagination
```

---

# 12. Avoid DISTINCT as a Band-Aid

Bad pattern:

```sql
SELECT DISTINCT c.*
FROM customer c
JOIN orders o
ON c.id = o.customer_id
JOIN order_item i
ON o.id = i.order_id;
```

`DISTINCT` may hide duplicate rows caused by an overly broad join.

But it can require:

```text
Sorting

OR

Hashing
```

---

## Better

Ask:

```text
Do I really need all joined tables?
```

---

# 13. JOIN Order

The database optimizer usually decides join order.

But your query structure and statistics affect the decision.

Conceptually, joining:

```text
Small Result

↓

Large Indexed Table
```

is usually better than processing huge intermediate datasets.

---

# 14. Nested Loop Join

## Definition

For every row from one input, the database searches the other input.

Flow:

```text
Row 1

↓

Lookup Other Table

↓

Row 2

↓

Lookup Other Table
```

---

## Good When

```text
Outer Result is Small

+

Inner Table Has Good Index
```

---

## Example

```sql
SELECT *
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.id = 101;
```

If one order is returned:

```text
1 Order

↓

Customer PK Lookup
```

Nested loop is efficient.

---

# 15. Hash Join

## Definition

The database builds a hash structure from one side and probes it with the other side.

---

## Good When

```text
Large Tables

Equality Join

Large Number of Matches
```

---

## Flow

```text
Department

↓

Build Hash

↓

Employee

↓

Probe Hash

↓

Match
```

---

# 16. Merge Join

## Definition

A **Merge Join** joins two sorted datasets by walking through them in order.

---

## Good When

```text
Large Datasets

Both Inputs Sorted

Join Columns Indexed / Ordered
```

---

# JOIN Algorithm Comparison

| Algorithm | Best Use |
|-----------|----------|
| Nested Loop | Small result + indexed lookup |
| Hash Join | Large equality joins |
| Merge Join | Large sorted inputs |

The database optimizer normally chooses the algorithm.

---

# 17. Statistics Matter

The optimizer uses statistics to estimate:

```text
Rows

Selectivity

Join Size

Best Join Algorithm
```

If statistics are outdated:

```text
Expected Rows = 10

Actual Rows = 1,000,000
```

the database may choose the wrong join strategy.

---

# 18. Check Execution Plan

Use:

```sql
EXPLAIN
SELECT ...
```

or:

```sql
EXPLAIN ANALYZE
SELECT ...
```

Check:

```text
Join Type

Rows Processed

Indexes Used

Table Scans

Estimated Rows

Actual Rows

Sort Cost
```

---

# 19. N+1 JOIN Problem in JPA

Suppose:

```java
List<Department> departments =
        departmentRepository.findAll();
```

Then:

```java
department.getEmployees();
```

for every department.

Hibernate may execute:

```text
1 Department Query

+

100 Employee Queries

↓

101 Queries
```

---

## Solution — JOIN FETCH

```java
@Query("""
       SELECT DISTINCT d
       FROM Department d
       LEFT JOIN FETCH d.employees
       """)
List<Department> findAllWithEmployees();
```

---

## Other Solutions

```text
@EntityGraph

Batch Fetching

DTO Projection
```

---

# 20. Fetch Join Carefully

Fetch joins solve N+1 but can also create huge result sets.

Example:

```text
Department

↓

Employees

↓

Projects

↓

Tasks
```

Fetching all relationships in one query may create:

```text
Massive Cartesian-like Row Multiplication
```

---

## Best Practice

Fetch only what the use case needs.

---

# 21. Pagination with JOIN

Be careful when paginating queries that fetch collection relationships.

Example:

```java
JOIN FETCH department.employees
```

with:

```text
Pageable
```

can produce unexpected or inefficient results depending on JPA provider/query shape.

---

## Better Approach

Often:

```text
1. Page Parent IDs

↓

2. Fetch Required Relationships
```

or use DTO projections.

---

# 22. Join on Correct Data Types

Avoid joining:

```text
VARCHAR

to

INTEGER
```

or columns requiring implicit conversion.

Bad:

```sql
ON CAST(e.department_id AS VARCHAR) = d.id
```

This can hurt index usage.

---

## Best Practice

JOIN columns should have compatible data types.

---

# 23. Functions on JOIN Columns

Potentially inefficient:

```sql
ON LOWER(e.email) = LOWER(c.email)
```

Normal indexes may not help effectively.

---

## Better

Normalize data when appropriate.

Or use:

```text
Functional Index
```

if supported.

---

# 24. Avoid Cartesian Product

Missing JOIN condition:

```sql
SELECT *
FROM employee e,
     department d;
```

If:

```text
Employee = 10,000 rows

Department = 100 rows
```

Result:

```text
1,000,000 Rows
```

---

## Correct

```sql
SELECT *
FROM employee e
JOIN department d
ON e.department_id = d.id;
```

---

# 25. CROSS JOIN

## Definition

A Cross Join intentionally creates every combination.

```sql
SELECT *
FROM color
CROSS JOIN size;
```

Example:

```text
3 Colors

×

4 Sizes

=

12 Combinations
```

Use only when this behavior is required.

---

# Real-World Optimization Example

Suppose API:

```text
GET /customers/101/orders
```

Slow query:

```sql
SELECT *
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE c.id = 101
ORDER BY o.created_at DESC;
```

Orders:

```text
20 Million Rows
```

No index on:

```text
orders.customer_id
```

Plan:

```text
Orders Full Scan

↓

20 Million Rows

↓

Join Customer

↓

Sort

↓

Return 20 Rows
```

Create:

```sql
CREATE INDEX idx_orders_customer_created
ON orders(customer_id, created_at);
```

New flow:

```text
Index

↓

Find Customer 101 Orders

↓

Already Ordered

↓

Join Customer

↓

Return Result
```

---

# Join Optimization Best Practices

✔ Index foreign-key/join columns when beneficial.

✔ Use the correct JOIN type.

✔ Filter rows as early as possible.

✔ Avoid `SELECT *`.

✔ Remove unnecessary JOINs.

✔ Avoid Cartesian products.

✔ Avoid unnecessary `DISTINCT`.

✔ Watch for row multiplication.

✔ Use `EXISTS` for existence checks when appropriate.

✔ Keep JOIN columns on compatible data types.

✔ Avoid functions/conversions on JOIN columns when possible.

✔ Keep optimizer statistics current.

✔ Use `EXPLAIN` / `EXPLAIN ANALYZE`.

✔ Monitor N+1 queries in JPA.

✔ Use `JOIN FETCH` only when the relationship is actually required.

✔ Use DTO projections for read-heavy APIs.

✔ Measure before and after optimization.

---

# Interview Questions

## What is Join Optimization?

Join optimization is the process of making multi-table SQL queries execute efficiently by reducing rows, using correct indexes, selecting appropriate join types, and analyzing execution plans.

---

## Which columns should be indexed for a JOIN?

Usually:

```text
Primary Key

Foreign Key / Join Column
```

when the workload benefits from the index.

---

## INNER JOIN vs LEFT JOIN?

```text
INNER JOIN

→ Matching Rows Only


LEFT JOIN

→ All Left Rows
→ Matching Right Rows
```

---

## Why can unnecessary JOINs be bad?

They increase:

```text
Rows Processed

CPU

Memory

Disk I/O

Network Data
```

---

## What is a Nested Loop Join?

A join where each row from one input is used to search the other input.

Best for small outer results with indexed lookups.

---

## What is a Hash Join?

A hash structure is created for one input and probed with rows from the other.

Common for large equality joins.

---

## What is a Merge Join?

Two sorted datasets are walked together to find matching values.

---

## What is row multiplication?

When one-to-many joins create multiple result rows for the same parent.

Example:

```text
Customer

↓

100 Orders

↓

1000 Order Items

↓

1000 Joined Rows
```

---

## What is the best way to optimize JOINs?

```text
Filter Early

↓

Use Proper Indexes

↓

Reduce Columns

↓

Reduce Joined Tables

↓

EXPLAIN

↓

Measure
```

---

# Quick Revision

```text
JOIN OPTIMIZATION

1. Index JOIN Columns

2. Filter Early

3. Avoid SELECT *

4. Remove Unnecessary JOINs

5. Use Correct JOIN Type

6. Avoid Cartesian Product

7. Watch Row Multiplication

8. Check N+1

9. Use EXPLAIN

10. Measure


JOIN TYPES

INNER
→ Matching Only

LEFT
→ All Left + Matches


JOIN ALGORITHMS

Nested Loop
→ Small + Indexed

Hash Join
→ Large Equality Join

Merge Join
→ Sorted Inputs


JPA

N+1

↓

JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection


Golden Rule

Less Data

↓

Better Index

↓

Better Plan

↓

Faster JOIN
```


# WHERE vs HAVING vs IN vs EXISTS vs NOT IN vs NOT EXISTS vs ORDER BY

---

# 1. WHERE

## Definition

- `WHERE` is used to **filter rows before grouping or aggregation happens**.
- It is the normal choice when filtering individual records.

## Use When

Use `WHERE` when filtering on normal columns.

```sql
SELECT *
FROM employee
WHERE salary > 50000;
```

## Real-World Example

```text
Get employees

↓

Only salary > 50000
```

## Best Use

```text
Normal row filtering
```

---

# 2. HAVING

## Definition

- `HAVING` is used to **filter grouped or aggregated results** after `GROUP BY`.
- It is mainly used with functions like `COUNT()`, `SUM()`, `AVG()`, `MIN()`, and `MAX()`.

## Use When

You need to filter based on an aggregate result.

```sql
SELECT department_id,
       COUNT(*) AS employee_count
FROM employee
GROUP BY department_id
HAVING COUNT(*) > 10;
```

## Real-World Example

```text
Group employees by department

↓

Count employees

↓

Return departments having more than 10 employees
```

---

# WHERE vs HAVING

```text
WHERE

→ Filters Rows
→ Before GROUP BY

HAVING

→ Filters Groups
→ After GROUP BY
```

Example:

```sql
SELECT department_id,
       AVG(salary)
FROM employee
WHERE active = true
GROUP BY department_id
HAVING AVG(salary) > 50000;
```

Here:

```text
WHERE active = true
→ Filters employees

HAVING AVG(salary) > 50000
→ Filters departments
```

---

# 3. IN

## Definition

- `IN` checks whether a column matches **one of multiple values**.
- It is cleaner than writing many `OR` conditions.

## Use When

You already have a list of values.

```sql
SELECT *
FROM employee
WHERE department_id IN (10, 20, 30);
```

Equivalent to:

```sql
WHERE department_id = 10
OR department_id = 20
OR department_id = 30;
```

## Real-World Example

```text
Get employees from

IT
HR
Finance
```

Use:

```text
IN
```

---

# 4. EXISTS

## Definition

- `EXISTS` checks whether a subquery returns **at least one matching row**.
- It returns true as soon as a matching row is found.

## Use When

You only need to know:

```text
Does related data exist?
```

Example:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

## Real-World Example

```text
Get Customers

↓

Who have at least one Order
```

Use:

```text
EXISTS
```

---

# IN vs EXISTS

## IN

```sql
SELECT *
FROM customer
WHERE id IN (
    SELECT customer_id
    FROM orders
);
```

## EXISTS

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

## When to Use What?

```text
IN

→ Fixed / Small Value List
→ Simple Membership Check


EXISTS

→ Check Related Row Exists
→ Correlated Subqueries
→ Often clearer for parent-child checks
```

Performance depends on:

```text
Database

Indexes

Statistics

Data Size

Optimizer
```

So don't assume `EXISTS` is always faster.

---

# 5. NOT IN

## Definition

- `NOT IN` returns rows whose value is **not present in a list or subquery result**.

Example:

```sql
SELECT *
FROM employee
WHERE department_id NOT IN (10, 20);
```

## Use When

You want to exclude a known list of values.

```text
Exclude

Department 10

Department 20
```

---

# Important Problem With NOT IN + NULL

Suppose subquery returns:

```text
10
20
NULL
```

Then:

```sql
WHERE id NOT IN (
    SELECT customer_id
    FROM orders
)
```

can behave unexpectedly because SQL uses three-valued logic with `NULL`.

For this reason, `NOT EXISTS` is often safer for subqueries.

---

# 6. NOT EXISTS

## Definition

- `NOT EXISTS` returns rows when **no matching row exists** in the subquery.
- It is commonly used for anti-join scenarios.

## Example

Find customers who have no orders:

```sql
SELECT c.*
FROM customer c
WHERE NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

## Real-World Example

```text
Get Customers

↓

Who Have Never Ordered
```

Use:

```text
NOT EXISTS
```

---

# NOT IN vs NOT EXISTS

```text
NOT IN

→ Exclude Known Values
→ Be Careful with NULL


NOT EXISTS

→ Check That Related Row Does Not Exist
→ Safer for nullable subquery results
```

---

# Example

## NOT IN

```sql
SELECT *
FROM employee
WHERE department_id NOT IN (10, 20, 30);
```

Good when:

```text
Fixed List
```

---

## NOT EXISTS

```sql
SELECT e.*
FROM employee e
WHERE NOT EXISTS (
    SELECT 1
    FROM bonus b
    WHERE b.employee_id = e.id
);
```

Good when:

```text
Find employees without bonus records
```

---

# 7. ORDER BY

## Definition

- `ORDER BY` sorts query results in ascending or descending order.
- Default order is generally `ASC` when direction is omitted.

## Syntax

```sql
ORDER BY salary ASC;
```

or:

```sql
ORDER BY salary DESC;
```

---

## Use When

You need sorted output.

Examples:

```text
Newest Orders First

Highest Salary First

Names Alphabetically
```

---

## Example

```sql
SELECT *
FROM employee
ORDER BY salary DESC;
```

Result:

```text
Highest Salary

↓

Lowest Salary
```

---

# Multiple ORDER BY Columns

```sql
SELECT *
FROM employee
ORDER BY department_id ASC,
         salary DESC;
```

Meaning:

```text
First

Sort by Department

↓

Inside Same Department

Sort Salary High → Low
```

---

# ORDER BY Performance

Sorting large datasets can be expensive.

Example:

```sql
SELECT *
FROM orders
WHERE customer_id = 101
ORDER BY created_at DESC;
```

Potential useful index:

```sql
CREATE INDEX idx_orders_customer_created
ON orders(customer_id, created_at);
```

This may help with:

```text
WHERE

+

ORDER BY
```

---

# Complete Example

Suppose tables:

```text
Customer

Orders
```

Requirement:

```text
Find active customers

who have orders

but don't have cancelled orders

and sort newest customers first.
```

Query:

```sql
SELECT c.*
FROM customer c
WHERE c.active = true

AND EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
)

AND NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
    AND o.status = 'CANCELLED'
)

ORDER BY c.created_at DESC;
```

---

# Quick Decision Guide

| Requirement | Use |
|---|---|
| Filter normal rows | `WHERE` |
| Filter aggregate/group result | `HAVING` |
| Match against fixed list | `IN` |
| Check related record exists | `EXISTS` |
| Exclude fixed values | `NOT IN` |
| Check related record does not exist | `NOT EXISTS` |
| Sort result | `ORDER BY` |

---

# Real-World Memory Examples

## WHERE

```text
Employees salary > 50000
```

```sql
WHERE salary > 50000
```

---

## HAVING

```text
Departments having > 10 employees
```

```sql
HAVING COUNT(*) > 10
```

---

## IN

```text
Employees in departments 10,20,30
```

```sql
WHERE department_id IN (10,20,30)
```

---

## EXISTS

```text
Customers who have orders
```

```sql
WHERE EXISTS (...)
```

---

## NOT IN

```text
Employees not in departments 10,20
```

```sql
WHERE department_id NOT IN (10,20)
```

---

## NOT EXISTS

```text
Customers who have no orders
```

```sql
WHERE NOT EXISTS (...)
```

---

## ORDER BY

```text
Newest orders first
```

```sql
ORDER BY created_at DESC
```

---

# Best Practices

✔ Use `WHERE` instead of `HAVING` when filtering normal rows.

✔ Use `HAVING` only for aggregate/group conditions.

✔ Use `IN` for small/fixed value lists.

✔ Use `EXISTS` when checking existence of related records.

✔ Prefer `NOT EXISTS` over `NOT IN` when a subquery can contain `NULL`.

✔ Index columns used frequently in `WHERE`, `EXISTS`, JOIN conditions, and `ORDER BY` when beneficial.

✔ Don't add `ORDER BY` unless ordering is actually required.

✔ Always inspect `EXPLAIN` for performance-critical queries.

---

# Interview Questions

## WHERE vs HAVING?

```text
WHERE
→ Filters Rows
→ Before GROUP BY

HAVING
→ Filters Groups
→ After GROUP BY
```

---

## IN vs EXISTS?

```text
IN
→ Membership in a List

EXISTS
→ Check Related Row Exists
```

---

## NOT IN vs NOT EXISTS?

```text
NOT IN
→ Exclude Values
→ NULL Can Cause Problems

NOT EXISTS
→ Check No Related Row Exists
→ Usually Safer for Subqueries
```

---

## Does ORDER BY happen before WHERE?

No.

Conceptually:

```text
FROM / JOIN

↓

WHERE

↓

GROUP BY

↓

HAVING

↓

SELECT

↓

ORDER BY
```

---

# Quick Revision

```text
WHERE

→ Filter Rows


HAVING

→ Filter Groups


IN

→ Match List


EXISTS

→ Related Record Exists


NOT IN

→ Exclude List
→ Watch NULL


NOT EXISTS

→ Related Record Does Not Exist


ORDER BY

→ Sort Result


Memory Trick

WHERE
→ Which Rows?

HAVING
→ Which Groups?

IN
→ In This List?

EXISTS
→ Does It Exist?

NOT IN
→ Not In This List?

NOT EXISTS
→ Doesn't Exist?

ORDER BY
→ In What Order?
```


# N+1 Query Problem

## Definition

- The **N+1 Query Problem** occurs when an application executes **1 query to fetch parent records and then N additional queries to fetch related child records**.
- It is common in ORM frameworks such as **Hibernate/JPA** when relationships are loaded inefficiently, especially with lazy loading inside a loop.

---

## Why Do We Need to Understand It?

The application may appear to execute only one repository call, but Hibernate can internally execute hundreds or thousands of SQL queries.

This causes:

```text
More Database Calls

More Network Round Trips

Higher Database Load

Slow API Response
```

---

## What Problem Does It Cause?

Suppose we have:

```text
100 Departments
```

Each department has employees.

First query:

```sql
SELECT *
FROM department;
```

This returns:

```text
100 Departments
```

Then application accesses employees:

```java
for (Department department : departments) {
    department.getEmployees().size();
}
```

Hibernate may execute:

```sql
SELECT *
FROM employee
WHERE department_id = 1;

SELECT *
FROM employee
WHERE department_id = 2;

SELECT *
FROM employee
WHERE department_id = 3;

...

SELECT *
FROM employee
WHERE department_id = 100;
```

Total:

```text
1 Parent Query

+

100 Child Queries

=

101 Queries
```

This is the:

```text
N + 1 Problem
```

---

# Entity Example

## Department

```java
@Entity
public class Department {

    @Id
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "department",
        fetch = FetchType.LAZY
    )
    private List<Employee> employees;
}
```

---

## Employee

```java
@Entity
public class Employee {

    @Id
    private Long id;

    private String name;

    @ManyToOne
    private Department department;
}
```

---

# Code Causing N+1

```java
List<Department> departments =
        departmentRepository.findAll();

for (Department department : departments) {

    System.out.println(
        department.getEmployees().size()
    );
}
```

---

# What Happens Internally?

First:

```sql
SELECT *
FROM department;
```

Then:

```text
department.getEmployees()
```

triggers lazy loading.

Hibernate executes:

```sql
SELECT *
FROM employee
WHERE department_id = ?;
```

once for each department.

---

# Flow

```text
findAll()

↓

SELECT Departments

↓

100 Departments

↓

Loop

↓

Department 1
→ SELECT Employees

Department 2
→ SELECT Employees

Department 3
→ SELECT Employees

...

Department 100
→ SELECT Employees

↓

101 Queries
```

---

# Real-World Example

Imagine an e-commerce API:

```text
GET /orders
```

It returns:

```text
100 Orders
```

Each order has:

```text
Customer
```

Application:

```java
for (Order order : orders) {

    System.out.println(
        order.getCustomer().getName()
    );
}
```

Hibernate may execute:

```text
1 Query

→ Get Orders

+

100 Queries

→ Get Customer for each Order
```

Result:

```text
101 Database Queries
```

The API becomes slow.

---

# How to Solve N+1

There are several approaches:

```text
1. JOIN FETCH

2. @EntityGraph

3. Batch Fetching

4. DTO Projection
```

---

# 1. JOIN FETCH ⭐

## Definition

`JOIN FETCH` tells Hibernate to fetch the parent and required relationship in the same query.

---

## Example

```java
@Query("""
       SELECT DISTINCT d
       FROM Department d
       LEFT JOIN FETCH d.employees
       """)
List<Department> findAllWithEmployees();
```

Hibernate generates conceptually:

```sql
SELECT d.*, e.*
FROM department d
LEFT JOIN employee e
ON d.id = e.department_id;
```

---

## Flow

Before:

```text
1 Department Query

+

100 Employee Queries

=

101 Queries
```

After:

```text
1 JOIN Query

↓

Departments + Employees
```

---

## Use When

You know that the relationship is required for the current use case.

---

# 2. @EntityGraph

## Definition

- `@EntityGraph` allows you to specify which relationships should be fetched for a particular repository operation.
- It avoids changing the entity's default fetch strategy globally.

---

## Example

```java
@EntityGraph(
    attributePaths = {"employees"}
)
@Query("SELECT d FROM Department d")
List<Department> findAllWithEmployees();
```

Flow:

```text
Department

+

Employees

↓

Fetched Together
```

---

## Use When

You want different fetching behavior for different use cases.

Example:

```text
API 1

Department Only


API 2

Department + Employees
```

---

# 3. Batch Fetching

## Definition

Batch fetching tells Hibernate to load multiple lazy relationships together instead of executing one query per parent.

---

## Example

```java
@OneToMany(
    mappedBy = "department",
    fetch = FetchType.LAZY
)
@BatchSize(size = 20)
private List<Employee> employees;
```

Instead of:

```text
100 Queries
```

Hibernate may execute something conceptually like:

```sql
SELECT *
FROM employee
WHERE department_id IN (
    1,2,3,4,5,
    ...
    20
);
```

Then another batch.

---

## Flow

```text
100 Departments

↓

Batch Size = 20

↓

Approximately 5 Employee Queries
```

Instead of:

```text
100 Employee Queries
```

---

# Global Batch Configuration

Spring Boot / Hibernate can also configure a default batch fetch size.

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=20
```

---

# 4. DTO Projection ⭐

## Definition

DTO Projection retrieves only the data required by the API instead of loading complete entities and relationships.

---

## Example

```java
public record DepartmentDTO(
        Long id,
        String departmentName,
        String employeeName) {
}
```

Repository:

```java
@Query("""
       SELECT new com.example.DepartmentDTO(
           d.id,
           d.name,
           e.name
       )
       FROM Department d
       JOIN d.employees e
       """)
List<DepartmentDTO> findDepartmentDetails();
```

---

## Advantages

```text
Less Data

Less Memory

No Unnecessary Entity Loading

Good for Read APIs
```

---

# Should We Change LAZY to EAGER?

A common mistake is:

```java
@OneToMany(fetch = FetchType.EAGER)
```

to solve N+1.

This is usually **not the best solution**.

---

## Why?

`EAGER` means:

```text
Always Load Relationship
```

even when the API doesn't need it.

This can cause:

```text
Large Queries

Unnecessary Data

Large Object Graphs

Performance Problems
```

---

## Better Approach

```text
Keep Relationships LAZY

↓

Fetch What Each Use Case Needs

↓

JOIN FETCH

@EntityGraph

DTO Projection

Batch Fetching
```

---

# LAZY Loading and N+1

Important:

```text
LAZY itself is NOT the N+1 problem.
```

The problem happens when:

```text
Load Many Parents

↓

Loop Through Parents

↓

Access Lazy Relationship

↓

One Query Per Parent
```

---

# EAGER Can Also Cause N+1

Another important interview point:

```text
EAGER does NOT guarantee one SQL JOIN.
```

Depending on the query and Hibernate behavior, eager relationships can still result in additional SQL queries.

Therefore:

```text
EAGER ≠ Automatic Solution to N+1
```

---

# JOIN FETCH with Multiple Collections

Be careful with:

```text
Department

↓

Employees

↓

Projects
```

Query:

```java
JOIN FETCH d.employees
JOIN FETCH d.projects
```

can create large row multiplication.

Example:

```text
Department

10 Employees

10 Projects

↓

10 × 10

↓

100 Joined Rows
```

---

## Best Practice

Don't fetch large collection graphs blindly.

Use:

```text
Separate Queries

DTO Projection

Batch Fetching
```

when appropriate.

---

# N+1 with Pagination

Be careful when combining:

```text
JOIN FETCH

+

@OneToMany

+

Pageable
```

because joining collections duplicates parent rows and can make pagination inefficient or incorrect at the SQL level.

---

## Better Approach

For large paginated APIs:

```text
Query 1

Page Parent IDs

↓

Query 2

Fetch Required Relationships
```

or use DTO projections.

---

# How to Detect N+1

## 1. Enable Hibernate SQL Logging

```properties
spring.jpa.show-sql=true
```

Or:

```properties
logging.level.org.hibernate.SQL=DEBUG
```

Then check whether you see:

```text
SELECT department

SELECT employees

SELECT employees

SELECT employees

SELECT employees

...
```

---

# 2. Hibernate Statistics

You can enable Hibernate statistics:

```properties
spring.jpa.properties.hibernate.generate_statistics=true
```

This helps inspect query behavior.

---

# 3. Application Performance Monitoring

Production tools can help identify excessive database queries.

Examples:

```text
Datadog

New Relic

Dynatrace

OpenTelemetry
```

---

# Real-World Optimization

Before:

```text
GET /departments

↓

1 Department Query

↓

500 Departments

↓

500 Employee Queries

↓

501 Queries
```

Response:

```text
4 Seconds
```

After using a proper fetch strategy:

```text
GET /departments

↓

Optimized Query / Queries

↓

Departments + Required Employees
```

Result:

```text
Far Fewer DB Round Trips

↓

Faster API
```

---

# Solution Comparison

| Solution | Best Use |
|----------|----------|
| `JOIN FETCH` | Relationship always needed for specific query |
| `@EntityGraph` | Dynamic/use-case-specific fetching |
| Batch Fetching | Many lazy relationships |
| DTO Projection | Read-only API/query-specific response |
| EAGER | Generally avoid as N+1 fix |

---

# Best Practices

✔ Keep collections `LAZY` by default.

✔ Don't access lazy relationships blindly inside loops.

✔ Use `JOIN FETCH` when related data is required.

✔ Use `@EntityGraph` for use-case-specific fetch plans.

✔ Use DTO projections for read-heavy APIs.

✔ Use batch fetching when appropriate.

✔ Avoid making everything `EAGER`.

✔ Be careful fetching multiple collections together.

✔ Be careful with collection fetch joins and pagination.

✔ Monitor the actual SQL generated by Hibernate.

✔ Count database queries, not only repository method calls.

---

# Interview Questions

## What is N+1 Problem?

N+1 occurs when ORM executes one query to fetch parent records and then N additional queries to fetch related records.

---

## Example?

```text
1 Query

→ 100 Departments

+

100 Queries

→ Employees

=

101 Queries
```

---

## What causes N+1?

Common cause:

```text
Fetch Multiple Parents

↓

Loop

↓

Access Relationship

↓

One Additional Query Per Parent
```

---

## How do you solve N+1?

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

---

## Does LAZY loading cause N+1?

Not automatically.

N+1 occurs when lazy relationships are repeatedly accessed for many parent entities.

---

## Does EAGER solve N+1?

Not necessarily.

Eager loading can still generate additional queries and can also load unnecessary data.

---

## How do you detect N+1?

```text
Hibernate SQL Logs

Hibernate Statistics

APM Tools

Query Count
```

---

# Quick Revision

```text
N+1 PROBLEM

1 Query

↓

Load N Parents

↓

N Additional Queries

↓

Load Children


Example

1 Department Query

+

100 Employee Queries

=

101 Queries


CAUSE

Parent List

↓

Loop

↓

Lazy Relationship Access

↓

Extra Query


SOLUTIONS

JOIN FETCH ⭐

@EntityGraph

Batch Fetching

DTO Projection


DON'T

Make Everything EAGER


BEST PRACTICE

LAZY by Default
↓
Fetch What You Need
↓
Monitor Generated SQL
↓
Count Queries
```



# Pagination

## Definition

- **Pagination** is the process of retrieving a large dataset in **small chunks/pages** instead of loading all records at once.
- It improves application performance by reducing memory usage, database load, network traffic, and response size.

---

## Why Do We Need It?

Suppose the `orders` table contains:

```text
10 Million Rows
```

If the API returns everything:

```sql
SELECT *
FROM orders;
```

Problems:

```text
Huge Memory Usage

Slow Query

Large Network Response

Slow API

Possible Timeout
```

Pagination solves this by returning only a small number of records.

---

## Real-World Example

### Amazon Product Page

Instead of loading:

```text
1,000,000 Products
```

Amazon loads:

```text
Page 1 → 20 Products

Page 2 → Next 20 Products

Page 3 → Next 20 Products
```

This is pagination.

---

# 1. OFFSET Pagination

## Definition

- OFFSET pagination skips a specific number of rows and returns the next set.
- It is simple and commonly used for normal page-based APIs.

---

## SQL Example

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 0;
```

Page 1:

```text
Rows 1 - 20
```

Page 2:

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 20;
```

Page 2:

```text
Rows 21 - 40
```

---

## Formula

```text
OFFSET = pageNumber × pageSize
```

Example:

```text
Page Number = 2

Page Size = 20

OFFSET = 2 × 20

OFFSET = 40
```

---

# Spring Data JPA Pagination

```java
Pageable pageable =
        PageRequest.of(0, 20);

Page<Order> orders =
        orderRepository.findAll(pageable);
```

---

## Repository

```java
public interface OrderRepository
        extends JpaRepository<Order, Long> {
}
```

---

# Page Object

`Page<T>` contains:

```text
Content

Current Page

Total Pages

Total Elements

Page Size

Has Next

Has Previous
```

Example:

```java
Page<Order> page =
        repository.findAll(
                PageRequest.of(0, 20)
        );

System.out.println(page.getContent());

System.out.println(page.getTotalPages());

System.out.println(page.getTotalElements());

System.out.println(page.hasNext());
```

---

# 2. Pagination with Sorting

```java
Pageable pageable =
        PageRequest.of(
                0,
                20,
                Sort.by("createdAt")
                        .descending()
        );
```

Query conceptually:

```sql
SELECT *
FROM orders
ORDER BY created_at DESC
LIMIT 20 OFFSET 0;
```

---

## Real-World Example

```text
Latest Orders First
```

Use:

```text
createdAt DESC
```

---

# 3. Pagination with Filtering

Repository:

```java
Page<Order> findByStatus(
        String status,
        Pageable pageable
);
```

Usage:

```java
Pageable pageable =
        PageRequest.of(0, 20);

Page<Order> orders =
        repository.findByStatus(
                "PAID",
                pageable
        );
```

Conceptually:

```sql
SELECT *
FROM orders
WHERE status = 'PAID'
ORDER BY id
LIMIT 20 OFFSET 0;
```

---

# 4. Why ORDER BY Is Important

Pagination should usually use a **stable deterministic order**.

Bad:

```sql
SELECT *
FROM orders
LIMIT 20 OFFSET 20;
```

Without `ORDER BY`, the database does not guarantee a stable row order.

---

## Better

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 20;
```

---

# 5. OFFSET Pagination Problem

OFFSET pagination becomes expensive for very deep pages.

Example:

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 1000000;
```

Database may need to:

```text
Find / Process 1,000,020 Rows

↓

Skip 1,000,000

↓

Return 20
```

This can become slow.

---

# Real-World Example

User requests:

```text
Page 50,000
```

Database:

```text
Skip Huge Number of Rows

↓

Return Small Number of Rows
```

Performance decreases as the offset grows.

---

# 6. Keyset Pagination / Seek Pagination ⭐

## Definition

- **Keyset Pagination** uses the last value from the previous page instead of using a large OFFSET.
- It is much more efficient for large datasets and infinite-scroll APIs.

---

## Example

First Page:

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20;
```

Suppose last returned ID is:

```text
500
```

Next Page:

```sql
SELECT *
FROM orders
WHERE id > 500
ORDER BY id
LIMIT 20;
```

---

## Flow

```text
Page 1

Last ID = 500

↓

Next Request

WHERE id > 500

↓

Next 20 Rows
```

No need to skip thousands or millions of records.

---

# Real-World Example

### Social Media Feed

Instead of:

```text
Page 1

Page 2

Page 3
```

the client sends:

```text
Last Seen Post ID
```

Server:

```text
Give me posts after this ID.
```

This is similar to keyset pagination.

---

# OFFSET vs Keyset Pagination

| OFFSET Pagination | Keyset Pagination |
|-------------------|-------------------|
| Uses page number | Uses last seen key |
| Easy to implement | Slightly more complex |
| Supports jumping to page | Best for next/previous flow |
| Slow for deep pages | Fast for large datasets |
| Good for admin screens | Good for feeds/APIs |

---

# 7. Cursor Pagination

## Definition

- Cursor pagination returns a cursor/token that represents the position of the last record.
- The client sends this cursor to fetch the next set of records.

---

## Example

Response:

```json
{
  "data": [
    {
      "id": 101
    },
    {
      "id": 102
    }
  ],
  "nextCursor": "102"
}
```

Next request:

```text
GET /orders?cursor=102&size=20
```

---

## Use When

```text
Infinite Scroll

Large Data Sets

High-Traffic APIs

Frequently Changing Data
```

---

# 8. Page vs Slice

Spring Data provides:

```text
Page<T>

Slice<T>
```

---

## Page

```java
Page<Order> findByStatus(
        String status,
        Pageable pageable
);
```

`Page` usually requires:

```text
Data Query

+

COUNT Query
```

because it needs:

```text
Total Elements

Total Pages
```

---

## Slice

```java
Slice<Order> findByStatus(
        String status,
        Pageable pageable
);
```

`Slice` mainly answers:

```text
Current Data

Is There a Next Page?
```

It doesn't need total pages in the same way.

---

# Page vs Slice

| Page | Slice |
|------|-------|
| Total count | No total count required |
| Total pages | Only knows next/previous |
| Extra COUNT query | Can avoid count query |
| Good for UI page numbers | Good for infinite scroll |
| More expensive | Often lighter |

---

# 9. Pagination with DTO Projection

If the API only needs:

```text
Order ID

Status

Total
```

don't load the full entity.

---

## DTO

```java
public record OrderSummary(
        Long id,
        String status,
        BigDecimal total) {
}
```

Repository:

```java
@Query("""
       SELECT new com.example.OrderSummary(
           o.id,
           o.status,
           o.total
       )
       FROM Order o
       WHERE o.status = :status
       """)
Page<OrderSummary> findOrderSummary(
        @Param("status") String status,
        Pageable pageable
);
```

---

## Benefits

```text
Less Data

Less Memory

Less Entity Mapping

Faster API
```

---

# 10. Pagination and Indexing

Query:

```sql
SELECT *
FROM orders
WHERE customer_id = 101
ORDER BY created_at DESC
LIMIT 20;
```

Useful index may be:

```sql
CREATE INDEX idx_orders_customer_created
ON orders(customer_id, created_at);
```

This can help:

```text
Filter Customer

+

Order by created_at

+

Return First 20
```

---

# 11. Pagination with Multiple Sort Columns

Suppose multiple orders have the same `created_at`.

Using only:

```sql
ORDER BY created_at DESC
```

may not provide a unique ordering.

Better:

```sql
ORDER BY created_at DESC,
         id DESC;
```

This provides a stable tie-breaker.

---

# Keyset Pagination with Multiple Columns

```sql
SELECT *
FROM orders
WHERE (
       created_at < :lastCreatedAt
       OR
       (
           created_at = :lastCreatedAt
           AND id < :lastId
       )
)
ORDER BY created_at DESC,
         id DESC
LIMIT 20;
```

This is common in production cursor-based APIs.

---

# 12. Pagination and JOIN FETCH

Be careful with:

```java
@OneToMany
```

and:

```java
JOIN FETCH
```

with pagination.

Example:

```java
@Query("""
       SELECT d
       FROM Department d
       JOIN FETCH d.employees
       """)
Page<Department> findDepartments(
        Pageable pageable
);
```

Problem:

```text
One Department

↓

Many Employees

↓

Multiple SQL Rows

↓

Pagination Can Become Incorrect / Expensive
```

---

## Better Approach

Often:

```text
Query 1

↓

Page Parent IDs

↓

Query 2

↓

Fetch Relationships
```

Or:

```text
DTO Projection
```

---

# 13. Pagination and N+1

Bad:

```java
Page<Order> orders =
        repository.findAll(pageable);

for (Order order : orders) {

    order.getCustomer().getName();
}
```

Can cause:

```text
1 Page Query

+

N Customer Queries
```

---

## Solutions

```text
EntityGraph

DTO Projection

Batch Fetching

Appropriate Fetch Join
```

---

# 14. Large COUNT Query Problem

For:

```java
Page<Order>
```

Spring Data may execute:

```sql
SELECT ...
FROM orders
LIMIT 20;
```

and:

```sql
SELECT COUNT(*)
FROM orders;
```

On very large/complex queries, the count query can itself be expensive.

---

## Solutions

When total count is unnecessary:

```text
Use Slice<T>
```

or cursor/keyset pagination.

---

# 15. Pagination API Example

Controller:

```java
@GetMapping("/orders")
public Page<Order> getOrders(
        @RequestParam(defaultValue = "0")
        int page,

        @RequestParam(defaultValue = "20")
        int size) {

    Pageable pageable =
            PageRequest.of(
                    page,
                    size,
                    Sort.by("createdAt")
                            .descending()
            );

    return repository.findAll(pageable);
}
```

Request:

```text
GET /orders?page=0&size=20
```

---

# Real-World Decision

## Admin Dashboard

Requirement:

```text
Page 1

Page 2

Page 10

Jump to Page 50
```

Use:

```text
OFFSET Pagination
```

---

## Instagram / Twitter Feed

Requirement:

```text
Load More

Load More

Load More
```

Use:

```text
Keyset / Cursor Pagination
```

---

## Large Order API

Requirement:

```text
Millions of Records

No Need for Total Pages
```

Use:

```text
Slice

+

Keyset Pagination
```

---

# Best Practices

✔ Never return the entire large table.

✔ Always use deterministic `ORDER BY`.

✔ Use an indexed sort/filter column when possible.

✔ Use normal OFFSET pagination for small/moderate datasets.

✔ Avoid deep OFFSET pagination.

✔ Prefer keyset/cursor pagination for very large datasets.

✔ Use `Slice` when total count isn't required.

✔ Use DTO projections when only a few columns are required.

✔ Avoid large collection `JOIN FETCH` with pagination.

✔ Watch for N+1 inside paginated results.

✔ Set a maximum allowed page size.

✔ Don't allow users to request unlimited records.

---

# Interview Questions

## What is Pagination?

Pagination divides a large result set into smaller chunks so the application doesn't load all records at once.

---

## What is OFFSET Pagination?

```text
LIMIT

+

OFFSET
```

Example:

```sql
LIMIT 20 OFFSET 40
```

---

## What is the problem with OFFSET?

Large offsets may require the database to process and skip many rows before returning the requested records.

---

## What is Keyset Pagination?

Keyset pagination uses the last seen key instead of OFFSET.

Example:

```sql
WHERE id > :lastId
ORDER BY id
LIMIT 20
```

---

## Page vs Slice?

```text
Page

→ Total Elements
→ Total Pages
→ Often COUNT Query


Slice

→ Current Data
→ Has Next
→ No Total Count Needed
```

---

## Which pagination is better for large data?

```text
Keyset / Cursor Pagination
```

---

## Which pagination is better when users need page numbers?

```text
OFFSET Pagination
```

---

# Quick Revision

```text
PAGINATION

Large Data

↓

Small Pages

↓

Better Performance


OFFSET PAGINATION

LIMIT 20 OFFSET 40

✔ Simple
✔ Page Numbers

✘ Slow for Deep Pages


KEYSET PAGINATION

WHERE id > lastId

ORDER BY id

LIMIT 20

✔ Fast
✔ Large Data
✔ Infinite Scroll


CURSOR

Last Position Token

↓

Next Records


SPRING DATA

Page<T>

→ Total Pages
→ Total Elements
→ Count Query


Slice<T>

→ Has Next
→ No Total Count Needed


BEST PRACTICE

Small Data
→ OFFSET

Large Data
→ KEYSET

Infinite Scroll
→ CURSOR

Need Total Pages
→ PAGE

Don't Need Total
→ SLICE
```


# JPA / Hibernate Fetch Strategies

## Definition

- **Fetch Strategy** defines **when and how related entities are loaded from the database**.
- In JPA/Hibernate, the main fetch strategies are **LAZY** and **EAGER** loading.

---

## Why Do We Need Fetch Strategies?

Entity relationships can contain a large amount of data.

Example:

```text
Department

↓

1000 Employees
```

If Hibernate loads all employees every time a department is loaded, it may cause:

```text
Slow Queries

High Memory Usage

Large Result Sets

Poor API Performance
```

Fetch strategies allow us to control when related data should be loaded.

---

# 1. LAZY Loading

## Definition

- **LAZY Loading** means related data is loaded **only when it is actually accessed**.
- Hibernate initially loads only the parent entity and fetches the relationship later if required.

---

## Example

```java
@Entity
public class Department {

    @Id
    private Long id;

    private String name;

    @OneToMany(
        mappedBy = "department",
        fetch = FetchType.LAZY
    )
    private List<Employee> employees;
}
```

When:

```java
Department department =
        repository.findById(1L).orElseThrow();
```

Hibernate initially loads:

```sql
SELECT *
FROM department
WHERE id = 1;
```

Employees are not loaded yet.

When:

```java
department.getEmployees();
```

Hibernate may execute:

```sql
SELECT *
FROM employee
WHERE department_id = 1;
```

---

## Flow

```text
Load Department

↓

Department Query

↓

Employees Not Loaded

↓

getEmployees()

↓

Employee Query
```

---

## Why Use LAZY?

It avoids loading unnecessary related data.

---

## Real-World Example

Suppose an API only needs:

```text
Department ID

Department Name
```

It does not need:

```text
1000 Employees
```

LAZY loading prevents unnecessary employee queries.

---

## Use When

```text
✔ Relationship data is not always required

✔ Collections contain many records

✔ Performance is important
```

---

# 2. EAGER Loading

## Definition

- **EAGER Loading** means related entities are loaded immediately when the parent entity is loaded.
- The relationship is considered required every time the entity is fetched.

---

## Example

```java
@ManyToOne(fetch = FetchType.EAGER)
private Department department;
```

Loading employee:

```java
Employee employee =
        repository.findById(1L).orElseThrow();
```

Hibernate also loads the department.

Conceptually:

```text
Employee

+

Department

↓

Loaded Together / Immediately
```

---

## Real-World Example

Suppose every employee response always requires:

```text
Employee Name

Department Name
```

Eager loading could appear useful.

However, in practice, making relationships globally EAGER can lead to unnecessary data loading.

---

# LAZY vs EAGER

| LAZY | EAGER |
|------|-------|
| Load when needed | Load immediately |
| Better control | Automatic loading |
| Lower initial query cost | Higher initial query cost |
| Can cause N+1 if misused | Can over-fetch data |
| Usually preferred for collections | Use carefully |

---

# Default JPA Fetch Types

## `@OneToMany`

Default:

```text
LAZY
```

---

## `@ManyToMany`

Default:

```text
LAZY
```

---

## `@ManyToOne`

Default:

```text
EAGER
```

---

## `@OneToOne`

Default:

```text
EAGER
```

---

# Default Summary

```text
@OneToMany
→ LAZY

@ManyToMany
→ LAZY

@ManyToOne
→ EAGER

@OneToOne
→ EAGER
```

---

# Important Interview Point

Even though JPA defaults `@ManyToOne` and `@OneToOne` to EAGER, many production applications explicitly use:

```java
fetch = FetchType.LAZY
```

to avoid unnecessary loading.

---

# 3. JOIN FETCH

## Definition

- `JOIN FETCH` tells Hibernate to load an entity and a specific relationship in the **same query**.
- It is one of the most common ways to solve the N+1 problem.

---

## Example

```java
@Query("""
       SELECT d
       FROM Department d
       LEFT JOIN FETCH d.employees
       WHERE d.id = :id
       """)
Optional<Department> findByIdWithEmployees(
        @Param("id") Long id);
```

Conceptually:

```sql
SELECT d.*, e.*
FROM department d
LEFT JOIN employee e
ON d.id = e.department_id
WHERE d.id = ?;
```

---

## Flow

```text
Department

+

Employees

↓

One Query
```

---

## Use When

The relationship is required for a specific use case.

---

# 4. EntityGraph

## Definition

- `@EntityGraph` allows you to define which relationships should be fetched for a particular repository method.
- It gives fetch control without changing the entity's default fetch strategy.

---

## Example

```java
@EntityGraph(
    attributePaths = {"employees"}
)
Optional<Department> findById(Long id);
```

Now:

```text
Department

+

Employees

↓

Fetched for this Repository Method
```

---

## Why Use It?

One API may need:

```text
Department Only
```

Another API may need:

```text
Department + Employees
```

`@EntityGraph` allows both without globally switching to EAGER.

---

# 5. Batch Fetching

## Definition

- Batch fetching allows Hibernate to load several lazy relationships in one query instead of one query for every parent.
- It reduces the N+1 problem without fetching everything in one huge JOIN.

---

## Example

```java
@OneToMany(
    mappedBy = "department",
    fetch = FetchType.LAZY
)
@BatchSize(size = 20)
private List<Employee> employees;
```

Instead of:

```text
100 Departments

↓

100 Employee Queries
```

Hibernate may do:

```text
100 Departments

↓

5 Employee Queries

Batch Size = 20
```

---

## Global Configuration

```properties
spring.jpa.properties.hibernate.default_batch_fetch_size=20
```

---

# 6. DTO Projection

## Definition

- DTO Projection fetches only the columns required for a specific response.
- It avoids loading complete entities and unnecessary relationships.

---

## Example

```java
public record EmployeeDTO(
        Long id,
        String employeeName,
        String departmentName) {
}
```

Repository:

```java
@Query("""
       SELECT new com.example.EmployeeDTO(
           e.id,
           e.name,
           d.name
       )
       FROM Employee e
       JOIN e.department d
       """)
List<EmployeeDTO> findEmployeeDetails();
```

---

## Benefits

```text
Less Data

Less Memory

Less Mapping

Faster Read APIs
```

---

# 7. N+1 Problem and Fetch Strategy

Bad:

```java
List<Department> departments =
        repository.findAll();

for (Department department : departments) {

    department.getEmployees().size();
}
```

Could produce:

```text
1 Department Query

+

N Employee Queries
```

---

## Solutions

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

---

# Important

```text
LAZY ≠ N+1
```

LAZY only means:

```text
Load When Accessed
```

N+1 happens when lazy relationships are accessed repeatedly across many parents.

---

# 8. Why EAGER Is Not a Good N+1 Fix

Changing:

```java
fetch = FetchType.LAZY
```

to:

```java
fetch = FetchType.EAGER
```

is often a mistake.

---

## Problems

```text
Loads Data Even When Not Needed

Large Object Graph

Higher Memory

More Database Work
```

Also:

```text
EAGER does not guarantee one JOIN query.
```

Hibernate may still issue additional queries.

---

# 9. Multiple Collection Fetch Problem

Suppose:

```text
Department

↓

Employees

↓

Projects
```

If:

```text
10 Employees

10 Projects
```

and both are fetched together:

```text
10 × 10

=

100 Rows
```

This is row multiplication.

---

## Problem

```text
Huge Result Set

High Memory

Duplicate Parent Data

Slow Query
```

---

## Better Approach

```text
Fetch Only Required Collection

OR

Use Multiple Queries

OR

Batch Fetching

OR

DTO Projection
```

---

# 10. LazyInitializationException

## Definition

A `LazyInitializationException` occurs when application code tries to access a lazy relationship **after the Hibernate session / persistence context has already closed**.

---

## Example

```java
Department department =
        service.findDepartment();

return department.getEmployees();
```

If the transaction has already ended:

```text
Persistence Context Closed

↓

employees not loaded

↓

Access employees

↓

LazyInitializationException
```

---

# Solution

Fetch required data inside the transaction.

Example:

```java
@Transactional(readOnly = true)
public Department getDepartment(Long id) {

    Department department =
            repository.findByIdWithEmployees(id)
                      .orElseThrow();

    return department;
}
```

Or better, return a DTO.

---

# 11. Open Session in View (OSIV)

## Definition

OSIV keeps the persistence context open during the web request so lazy relationships can be loaded later.

Spring Boot has historically enabled OSIV for web applications unless configured otherwise.

---

## Problem

It can hide bad fetching strategies.

Example:

```text
Controller

↓

JSON Serialization

↓

Lazy Relationship Access

↓

Unexpected SQL Query
```

This can lead to N+1 queries in the web layer.

---

## Best Practice

In many production systems:

```text
Fetch Required Data in Service Layer

↓

Map to DTO

↓

Return DTO
```

rather than depending on OSIV.

---

# 12. Fetch Strategy and Pagination

Be careful with:

```text
JOIN FETCH

+

@OneToMany

+

Pagination
```

because one parent may produce many SQL rows.

Example:

```text
Department 1

↓

10 Employees

↓

10 SQL Rows
```

Pagination may become inefficient or incorrect at the SQL row level.

---

## Better Approach

```text
Page Parent IDs

↓

Fetch Required Relationships Separately
```

or use:

```text
DTO Projection
```

---

# Real-World Decision Examples

## API 1

```text
GET /departments
```

Needs:

```text
Department ID

Department Name
```

Use:

```text
LAZY
```

Don't load employees.

---

## API 2

```text
GET /departments/101/details
```

Needs:

```text
Department

+

Employees
```

Use:

```text
JOIN FETCH

or

@EntityGraph
```

---

## API 3

```text
GET /employees
```

Needs:

```text
Employee Name

Department Name
```

Use:

```text
DTO Projection
```

---

## API 4

```text
100 Departments

Employees Occasionally Accessed
```

Use:

```text
LAZY

+

Batch Fetching
```

---

# Fetch Strategy Decision Guide

| Requirement | Best Approach |
|-------------|---------------|
| Relationship not always needed | LAZY |
| Relationship required in specific query | JOIN FETCH |
| Different fetch plan per repository method | `@EntityGraph` |
| Many lazy relationships | Batch Fetching |
| Read-only API with few columns | DTO Projection |
| Globally load every relationship | Avoid EAGER when possible |

---

# Best Practices

✔ Prefer LAZY for relationships unless there is a clear reason otherwise.

✔ Don't make everything EAGER.

✔ Fetch only what the current use case needs.

✔ Use `JOIN FETCH` for targeted relationship loading.

✔ Use `@EntityGraph` for flexible repository fetch plans.

✔ Use DTO projections for read-heavy APIs.

✔ Use batch fetching for collections accessed in groups.

✔ Watch for N+1 queries.

✔ Avoid fetching multiple large collections together.

✔ Be careful with fetch joins + pagination.

✔ Avoid accessing lazy data after transaction/session closes.

✔ Monitor generated Hibernate SQL.

---

# Interview Questions

## What is Fetch Strategy?

Fetch strategy defines when and how related JPA entities are loaded from the database.

---

## LAZY vs EAGER?

```text
LAZY

→ Load When Needed


EAGER

→ Load Immediately
```

---

## Default Fetch for `@OneToMany`?

```text
LAZY
```

---

## Default Fetch for `@ManyToOne`?

```text
EAGER
```

---

## What is JOIN FETCH?

A JPQL feature used to load an entity and its relationship in the same query.

---

## What is EntityGraph?

A JPA feature that defines which relationships should be fetched for a particular query/repository method.

---

## What is Batch Fetching?

Loading multiple lazy relationships together in batches instead of issuing one query per parent.

---

## Should we use EAGER to solve N+1?

Usually no.

Use targeted strategies such as:

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

---

## What is LazyInitializationException?

It occurs when a lazy relationship is accessed after the persistence context/session has closed.

---

# Quick Revision

```text
FETCH STRATEGY

LAZY

→ Load When Needed
→ Usually Preferred


EAGER

→ Load Immediately
→ Use Carefully


JOIN FETCH

→ Parent + Relationship
→ Same Query
→ Solve N+1


@EntityGraph

→ Dynamic Fetch Plan


Batch Fetch

→ Load Lazy Relationships in Groups


DTO Projection

→ Fetch Only Required Columns


N+1

1 Parent Query

+

N Child Queries


Best Practice

LAZY

↓

Fetch What You Need
↓
JOIN FETCH / EntityGraph / DTO / Batch
↓
Monitor SQL
```



# Database Best Practices

## Definition

- **Database Best Practices** are design, coding, security, and performance guidelines used to keep a database **fast, reliable, scalable, secure, and easy to maintain**.
- They help prevent common problems such as slow queries, data inconsistency, deadlocks, duplicate data, connection exhaustion, and difficult production issues.

---

# 1. Design Tables Properly

## Definition

Good table design means choosing the correct columns, keys, relationships, and data types before building queries.

---

## Why Do We Need It?

Poor table design creates:

```text
Duplicate Data

Complex Queries

Slow JOINs

Data Inconsistency
```

---

## Best Practice

✔ Use meaningful table names.

✔ Define Primary Keys.

✔ Define Foreign Keys.

✔ Use correct data types.

✔ Avoid unnecessary columns.

✔ Normalize where appropriate.

---

# 2. Use Correct Data Types

## Bad

```sql
salary VARCHAR(50)
```

---

## Better

```sql
salary DECIMAL(12,2)
```

---

## Why?

Correct data types improve:

```text
Validation

Storage

Index Performance

Query Performance
```

---

# 3. Always Use Primary Keys

## Definition

A Primary Key uniquely identifies each row.

Example:

```sql
CREATE TABLE employee (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100)
);
```

---

## Why Do We Need It?

Helps with:

```text
Fast Lookup

Relationships

Updates

Deletes

Uniqueness
```

---

# 4. Use Foreign Keys

## Definition

Foreign Keys maintain relationships between tables.

Example:

```text
Employee

department_id

↓

Department

id
```

---

## Why Do We Need It?

Prevents invalid relationships.

Example:

```text
Employee.department_id = 100

But Department 100 Does Not Exist

↓

Foreign Key Prevents It
```

---

# 5. Normalize Data

## Definition

Normalization reduces duplicate data by separating related information into proper tables.

---

## Bad Design

```text
Employee

id
name
department_name
department_location
department_manager
```

Department information is repeated for every employee.

---

## Better

```text
Employee

id
name
department_id


Department

id
name
location
manager
```

---

## Why?

Reduces:

```text
Duplicate Data

Storage

Update Problems
```

---

# 6. Don't Over-Normalize

Too much normalization can require many JOINs.

For read-heavy systems, some controlled denormalization may be useful.

---

## Best Practice

```text
Normalize for Consistency

Denormalize only when justified by performance requirements
```

---

# 7. Create Proper Indexes

Indexes improve data retrieval.

Example:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

---

## Good Candidates

Columns frequently used in:

```text
WHERE

JOIN

ORDER BY

GROUP BY
```

when the workload benefits.

---

## Avoid

```text
Index Every Column
```

because indexes increase:

```text
Storage

INSERT Cost

UPDATE Cost

DELETE Cost
```

---

# 8. Avoid SELECT *

## Bad

```sql
SELECT *
FROM employee;
```

---

## Better

```sql
SELECT id,
       name,
       email
FROM employee;
```

---

## Why?

Reduces:

```text
Network Traffic

Memory

Disk I/O

Mapping Cost
```

---

# 9. Filter Data in Database

## Bad

```java
repository.findAll()
          .stream()
          .filter(...);
```

---

## Better

```sql
SELECT *
FROM employee
WHERE status = 'ACTIVE';
```

---

## Why?

Database should return:

```text
Only Required Data
```

not millions of unnecessary rows.

---

# 10. Use Pagination

Never return huge datasets.

Bad:

```sql
SELECT *
FROM orders;
```

---

## Better

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 0;
```

---

## Spring Data

```java
Pageable pageable =
        PageRequest.of(0, 20);
```

---

# 11. Use Stable ORDER BY with Pagination

Bad:

```sql
SELECT *
FROM orders
LIMIT 20;
```

---

## Better

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20;
```

Use deterministic ordering.

---

# 12. Prefer Keyset Pagination for Large Data

Deep OFFSET:

```sql
OFFSET 1000000
```

can become expensive.

---

## Better

```sql
WHERE id > :lastId
ORDER BY id
LIMIT 20;
```

Use for:

```text
Infinite Scroll

Large Tables

High-Traffic APIs
```

---

# 13. Avoid N+1 Queries

Bad:

```text
1 Parent Query

+

100 Child Queries
```

---

## Solutions

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

---

# 14. Use LAZY Fetching Carefully

Prefer:

```java
@OneToMany(fetch = FetchType.LAZY)
```

for large relationships.

But don't blindly access lazy relationships inside loops.

---

# 15. Don't Make Everything EAGER

EAGER can cause:

```text
Large Object Graph

Unexpected Queries

High Memory

Slow API
```

---

## Better

```text
LAZY

+

Fetch Only What You Need
```

---

# 16. Use DTO Projections

If API only needs:

```text
Employee Name

Department Name
```

don't load full entities.

---

## Example

```java
public record EmployeeDTO(
        String employeeName,
        String departmentName) {
}
```

---

# 17. Keep Transactions Short

Bad:

```text
Begin Transaction

↓

Update DB

↓

Call External API

↓

Wait 10 Seconds

↓

Update DB

↓

Commit
```

Locks remain active too long.

---

## Better

```text
Begin Transaction

↓

Database Work

↓

Commit

↓

External Work
```

when the business semantics allow it.

---

# 18. Avoid External API Calls Inside Transactions

Why?

External calls can be:

```text
Slow

Unavailable

Timeout
```

Meanwhile database locks may remain active.

---

# 19. Use Correct Isolation Level

Don't always use:

```text
SERIALIZABLE
```

because it reduces concurrency.

---

## General Guideline

```text
Normal Applications
→ READ COMMITTED

Need Stable Reads
→ REPEATABLE READ

Extreme Consistency
→ SERIALIZABLE
```

Exact choice depends on database and use case.

---

# 20. Use Optimistic Locking When Conflicts Are Rare

```java
@Version
private Long version;
```

Best for:

```text
Employee Profile

Customer Profile

Product Update
```

---

# 21. Use Pessimistic Locking Carefully

Use when:

```text
High Conflict

Critical Inventory

Ticket Booking
```

Avoid unnecessary locking because it reduces concurrency.

---

# 22. Avoid Deadlocks

## Best Practices

✔ Lock rows in consistent order.

✔ Keep transactions short.

✔ Avoid unnecessary locks.

✔ Use appropriate indexes.

✔ Retry deadlock victims safely where appropriate.

---

# 23. Use Connection Pooling

Opening DB connections repeatedly is expensive.

Use:

```text
HikariCP
```

Spring Boot commonly uses it by default.

---

## Flow

```text
Application

↓

Connection Pool

↓

Reusable Connection

↓

Database
```

---

# 24. Don't Make Connection Pool Too Large

More connections are not always better.

Too many connections can overload:

```text
Database CPU

Memory

Locks
```

---

## Monitor

```text
Active Connections

Idle Connections

Waiting Threads

Timeouts
```

---

# 25. Use Prepared Statements

Bad:

```java
"SELECT * FROM user WHERE id = " + id
```

---

## Better

```sql
SELECT *
FROM user
WHERE id = ?;
```

---

## Benefits

```text
SQL Injection Protection

Cleaner SQL

Potential Plan Reuse
```

---

# 26. Avoid Functions on Indexed Columns

Potentially inefficient:

```sql
WHERE LOWER(email) = ?
```

when only a normal index on `email` exists.

---

## Better

Normalize data when possible.

Or create a functional/expression index if supported.

---

# 27. Avoid Leading Wildcard Search

Potentially expensive:

```sql
WHERE name LIKE '%Manoj%'
```

---

## Better When Possible

```sql
WHERE name LIKE 'Manoj%'
```

For complex search:

```text
Full-Text Search

Elasticsearch

OpenSearch
```

---

# 28. Use WHERE Instead of HAVING When Possible

Bad:

```sql
SELECT department_id,
       COUNT(*)
FROM employee
GROUP BY department_id
HAVING department_id = 10;
```

---

## Better

```sql
SELECT department_id,
       COUNT(*)
FROM employee
WHERE department_id = 10
GROUP BY department_id;
```

Filter earlier.

---

# 29. Use EXISTS for Existence Checks

Example:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

Use when asking:

```text
Does related data exist?
```

---

# 30. Be Careful with NOT IN and NULL

Potential issue:

```sql
WHERE id NOT IN (
    SELECT customer_id
    FROM orders
)
```

if the subquery returns `NULL`.

---

## Safer Pattern

```sql
WHERE NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

---

# 31. Avoid Unnecessary DISTINCT

`DISTINCT` may require:

```text
Sorting

OR

Hashing
```

Use it only when duplicates genuinely need to be removed.

---

# 32. Prefer UNION ALL When Duplicate Removal Isn't Needed

`UNION`

```text
Combines

+

Removes Duplicates
```

---

`UNION ALL`

```text
Combines

+

Keeps Duplicates

↓

Usually Less Work
```

---

# 33. Optimize JOINs

✔ Index useful join columns.

✔ Remove unnecessary joins.

✔ Filter before joining when possible.

✔ Avoid Cartesian products.

✔ Watch row multiplication.

---

# 34. Use EXPLAIN

For slow queries:

```sql
EXPLAIN
SELECT ...
```

or:

```sql
EXPLAIN ANALYZE
SELECT ...
```

Check:

```text
Table Scan

Index Scan

Index Seek

Join Algorithm

Rows

Sort

Cost
```

---

# 35. Keep Statistics Updated

The optimizer uses statistics to decide:

```text
Which Index?

Which JOIN?

Which Table First?
```

Bad statistics can create bad execution plans.

---

# 36. Batch Large Writes

Bad:

```text
10000 Individual INSERT Calls
```

---

## Better

```text
Batch Inserts

Batch Updates
```

This reduces database round trips.

---

# Hibernate Batch Example

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
```

---

# 37. Don't Load Huge Data into Memory

Bad:

```java
List<Order> orders =
        repository.findAll();
```

for:

```text
10 Million Orders
```

---

## Better

```text
Pagination

Streaming

Batch Processing
```

---

# 38. Use Cache for Read-Heavy Stable Data

Good candidates:

```text
Country List

Configuration

Reference Data

Product Categories
```

Possible tools:

```text
Redis

Caffeine

Hibernate Second-Level Cache
```

---

# 39. Don't Cache Everything

Bad cache candidates:

```text
Frequently Changing Balance

Highly Volatile Inventory

Data Requiring Immediate Consistency
```

unless you have a strong invalidation/consistency strategy.

---

# 40. Use Database Constraints

Application validation alone is not enough.

Use:

```text
PRIMARY KEY

FOREIGN KEY

UNIQUE

NOT NULL

CHECK
```

---

## Example

```sql
email VARCHAR(255) NOT NULL UNIQUE
```

---

# 41. Use Proper Naming Conventions

Example:

```text
employee

department

employee_id

department_id
```

Avoid unclear names:

```text
tbl1

colA

dataX
```

---

# 42. Avoid Storing Derived Data Unnecessarily

Example:

If:

```text
total = quantity × price
```

consider whether `total` truly needs storage.

Derived data can become inconsistent if one component changes.

Sometimes denormalization is justified, but it should be intentional.

---

# 43. Store Dates Properly

Don't store date values as:

```text
VARCHAR
```

Use proper:

```text
DATE

TIMESTAMP
```

types.

---

# 44. Handle Time Zones Properly

For distributed systems, a common strategy is:

```text
Store Time in UTC

↓

Convert at Presentation Boundary
```

Exact choices depend on business requirements.

---

# 45. Use Database Migrations

Don't manually change production schema.

Use:

```text
Flyway

Liquibase
```

---

## Benefits

```text
Version Controlled Schema

Repeatable Deployments

Audit Trail
```

---

# 46. Backup Database

Always have:

```text
Regular Backups

Restore Testing

Disaster Recovery Plan
```

A backup that has never been restore-tested is risky.

---

# 47. Monitor Slow Queries

Monitor:

```text
Query Duration

CPU

Memory

Disk I/O

Lock Waits

Deadlocks

Connections
```

---

# 48. Add Timeouts

Avoid queries waiting forever.

Configure appropriate:

```text
Query Timeout

Connection Timeout

Lock Timeout
```

---

# 49. Avoid Huge Transactions

Bad:

```text
Update 10 Million Rows

↓

One Huge Transaction
```

Can cause:

```text
Large Locks

Large Transaction Logs

Long Rollback

High Memory
```

Consider controlled batching when appropriate.

---

# 50. Protect Sensitive Data

Never store:

```text
Plain Passwords
```

Use secure password hashing.

Protect sensitive information using:

```text
Encryption

Access Control

Least Privilege
```

---

# 51. Use Least Privilege

Application database user should have only the permissions required.

Don't give:

```text
DROP DATABASE

CREATE USER

SUPERUSER
```

to normal application accounts.

---

# 52. Don't Log Sensitive Data

Avoid logging:

```text
Passwords

Tokens

Credit Card Details

Sensitive Personal Data
```

---

# 53. Test Queries with Production-Like Data

A query may be fast with:

```text
100 Rows
```

but slow with:

```text
10 Million Rows
```

Always performance-test important queries with realistic data volume.

---

# 54. Measure Before Optimizing

Don't optimize based only on assumptions.

Use:

```text
Metrics

Slow Query Logs

EXPLAIN

EXPLAIN ANALYZE

APM
```

---

# Database Optimization Flow

```text
API Slow

↓

Find Slow Query

↓

Measure

↓

EXPLAIN

↓

Check Index

↓

Check JOIN

↓

Check Returned Rows

↓

Check N+1

↓

Optimize

↓

Measure Again
```

---

# Real-World Production Example

Suppose:

```text
GET /orders?customerId=101
```

takes:

```text
5 Seconds
```

Investigation:

```text
20 Million Orders

↓

No customer_id Index

↓

Full Table Scan

↓

SELECT *

↓

No Pagination
```

Fix:

```sql
CREATE INDEX idx_orders_customer_created
ON orders(customer_id, created_at);
```

Query:

```sql
SELECT id,
       status,
       total,
       created_at
FROM orders
WHERE customer_id = ?
ORDER BY created_at DESC
LIMIT 20;
```

Result:

```text
Use Index

↓

Retrieve Required Columns

↓

Only 20 Rows

↓

Faster API
```

---

# Quick Decision Guide

| Requirement | Recommended Practice |
|-------------|----------------------|
| Search frequently | Index |
| Large result | Pagination |
| Deep pagination | Keyset Pagination |
| Related entities | Targeted Fetch Strategy |
| N+1 | Join Fetch / EntityGraph / DTO |
| Read-only response | DTO Projection |
| Concurrent updates rare | Optimistic Lock |
| Concurrent conflicts high | Pessimistic Lock |
| Slow SQL | EXPLAIN |
| Frequent reference reads | Cache |
| Multiple writes | Transaction |
| Schema change | Flyway / Liquibase |

---

# Interview Questions

## What are the most important database best practices?

```text
Proper Schema Design

Correct Indexing

Optimized Queries

Short Transactions

Pagination

Proper Fetch Strategy

Connection Pooling

Monitoring

Security

Backups
```

---

## Why shouldn't we use SELECT *?

Because it retrieves unnecessary columns and increases I/O, network traffic, memory usage, and mapping cost.

---

## Why shouldn't we create indexes on every column?

Because indexes increase write cost, storage, and maintenance overhead.

---

## Why keep transactions short?

Long transactions hold resources and locks longer, increasing contention and deadlock risk.

---

## Why use pagination?

To prevent loading huge datasets into memory and returning oversized responses.

---

## Why use EXPLAIN?

To understand how the database executes a query and identify inefficient scans, joins, sorts, or index usage.

---

# Best Practices Summary

```text
SCHEMA

✔ Correct Data Types
✔ Primary Keys
✔ Foreign Keys
✔ Constraints
✔ Appropriate Normalization


QUERY

✔ Avoid SELECT *
✔ Filter Early
✔ Proper JOINs
✔ Pagination
✔ EXPLAIN


INDEX

✔ WHERE
✔ JOIN
✔ ORDER BY
✔ Based on Workload


JPA

✔ LAZY by Default
✔ Fix N+1
✔ DTO Projection
✔ Batch Fetching


TRANSACTION

✔ Keep Short
✔ Correct Isolation
✔ Avoid External Calls
✔ Handle Deadlocks


PERFORMANCE

✔ Connection Pool
✔ Cache Carefully
✔ Batch Writes
✔ Monitor Slow Queries


SECURITY

✔ Prepared Statements
✔ Least Privilege
✔ Protect Sensitive Data


OPERATIONS

✔ Migrations
✔ Backups
✔ Restore Tests
✔ Monitoring
```

---

# Quick Revision

```text
DATABASE BEST PRACTICES

Design Well

↓

Index Correctly

↓

Query Only Required Data

↓

Filter Early

↓

Paginate

↓

Avoid N+1

↓

Use Proper Fetch Strategy

↓

Keep Transactions Short

↓

Use Connection Pool

↓

EXPLAIN Slow Queries

↓

Cache Carefully

↓

Monitor Production

↓

Secure Data

↓

Backup & Test Restore
```



# Database Performance Tuning

## Definition

- **Database Performance Tuning** is the process of identifying and improving slow database operations to reduce query execution time, CPU usage, memory usage, disk I/O, and database load.
- It involves analyzing **queries, execution plans, indexes, joins, pagination, connections, locking, and application database-access patterns**.

---

# Why Do We Need Performance Tuning?

A query may perform well with:

```text
1,000 Rows
```

but become slow when the table grows to:

```text
1 Million Rows

10 Million Rows

100 Million Rows
```

Poor database performance can cause:

```text
Slow APIs

High CPU

High Memory

Connection Pool Exhaustion

Timeouts

Lock Contention

Poor User Experience
```

---

# Performance Tuning Process

```text
Application Slow

↓

Find Slow API

↓

Find Slow SQL

↓

Run EXPLAIN / EXPLAIN ANALYZE

↓

Check Execution Plan

↓

Check Rows Scanned

↓

Check Indexes

↓

Check JOINs

↓

Optimize Query

↓

Run Again

↓

Compare Performance
```

---

# Real-World Query Example

Suppose we have an `orders` table containing:

```text
20 Million Rows
```

Table:

```sql
CREATE TABLE orders (

    id BIGINT PRIMARY KEY,

    customer_id BIGINT,

    status VARCHAR(20),

    total DECIMAL(12,2),

    created_at TIMESTAMP
);
```

Our API needs:

```text
Latest 20 PAID orders

for Customer 101
```

---

# Initial Query

```sql
SELECT *
FROM orders
WHERE customer_id = 101
AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;
```

Assume there is no useful index except the primary key.

---

# Run EXPLAIN

PostgreSQL example:

```sql
EXPLAIN ANALYZE
SELECT *
FROM orders
WHERE customer_id = 101
AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;
```

A simplified example plan might look like:

```text
Limit

↓

Sort

↓

Sequential Scan on orders

Filter:
customer_id = 101
AND status = 'PAID'

Rows Scanned : 20,000,000

Rows Matched : 5,000

Rows Returned: 20

Execution Time: 2500 ms
```

---

# What Is Wrong?

We can identify three major problems.

```text
Problem 1

Sequential Scan

↓

20 Million Rows Read
```

```text
Problem 2

5000 Matching Rows

↓

Sort by created_at
```

```text
Problem 3

SELECT *

↓

All Columns Retrieved
```

But we only need 20 rows.

---

# Step 1 — Add an Index

Query filters using:

```text
customer_id

status
```

and sorts using:

```text
created_at
```

Create a composite index:

```sql
CREATE INDEX idx_orders_customer_status_created
ON orders(
    customer_id,
    status,
    created_at DESC
);
```

---

# Why This Index?

Our query:

```sql
WHERE customer_id = 101
AND status = 'PAID'
ORDER BY created_at DESC
```

Index:

```text
customer_id

↓

status

↓

created_at DESC
```

matches the query pattern.

---

# New Execution Flow

```text
Index

↓

Customer 101

↓

PAID

↓

Already Ordered by created_at

↓

Take First 20
```

---

# Run EXPLAIN Again

```sql
EXPLAIN ANALYZE
SELECT *
FROM orders
WHERE customer_id = 101
AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;
```

Simplified example:

```text
Limit

↓

Index Scan using
idx_orders_customer_status_created

↓

Rows Read: 20

↓

Rows Returned: 20

Execution Time: 5 ms
```

---

# Before vs After

| Before | After |
|--------|-------|
| Sequential Scan | Index Scan |
| Millions of rows examined | Small targeted range |
| Explicit Sort | Index can provide order |
| ~2500 ms example | ~5 ms example |

> These timings are illustrative. Actual performance depends on hardware, data distribution, cache state, database engine, and workload.

---

# Step 2 — Avoid SELECT *

Suppose API only needs:

```text
Order ID

Total

Created Date
```

Don't use:

```sql
SELECT *
```

Use:

```sql
SELECT id,
       total,
       created_at
FROM orders
WHERE customer_id = 101
AND status = 'PAID'
ORDER BY created_at DESC
LIMIT 20;
```

---

# Why?

Reduces:

```text
Disk I/O

Network Traffic

Memory

Application Mapping
```

---

# Step 3 — Covering Index

For a very performance-sensitive read query, you might design an index that also covers the selected columns.

The exact syntax depends on the database.

Conceptually:

```text
Index

customer_id
status
created_at
id
total
```

Then:

```text
Query

↓

Index

↓

Result
```

The database may avoid reading the main table.

---

# Step 4 — Check Rows Read vs Rows Returned

One of the most useful performance indicators is:

```text
Rows Read

vs

Rows Returned
```

Bad:

```text
Rows Read     = 20,000,000

Rows Returned = 20
```

Potentially good:

```text
Rows Read     ≈ 20

Rows Returned = 20
```

---

# Step 5 — Check Estimated vs Actual Rows

Execution plan:

```text
Estimated Rows = 10

Actual Rows    = 500,000
```

This is suspicious.

---

## Possible Cause

```text
Outdated Statistics

Data Skew

Poor Cardinality Estimate
```

The optimizer may choose the wrong execution strategy.

---

# Step 6 — Update Statistics

The exact command depends on the database.

PostgreSQL:

```sql
ANALYZE orders;
```

The optimizer then gets updated information about the data distribution.

---

# Example 2 — Function Preventing Index Usage

Suppose:

```sql
CREATE INDEX idx_employee_email
ON employee(email);
```

Query:

```sql
SELECT *
FROM employee
WHERE LOWER(email) = 'user@example.com';
```

Possible plan:

```text
Sequential Scan

↓

Apply LOWER()

↓

Compare Every Row
```

The normal index on `email` may not help.

---

# Solution 1

Store normalized email values where appropriate:

```sql
WHERE email = 'user@example.com';
```

---

# Solution 2

If supported, create an expression index:

```sql
CREATE INDEX idx_employee_lower_email
ON employee(LOWER(email));
```

Then:

```sql
WHERE LOWER(email) = 'user@example.com'
```

may use the expression index.

---

# Example 3 — LIKE Performance

Potentially efficient:

```sql
WHERE name LIKE 'Manoj%'
```

A normal B-tree index may help with prefix searching depending on database/collation.

---

Potentially expensive:

```sql
WHERE name LIKE '%Manoj%'
```

Flow:

```text
Unknown Prefix

↓

Search Many Values

↓

Normal B-Tree Index Often Less Useful
```

For large-scale text searching, consider:

```text
Full-Text Search

Elasticsearch

OpenSearch

Database-Specific Text Index
```

---

# Example 4 — JOIN Performance

Query:

```sql
SELECT o.id,
       c.name,
       o.total
FROM orders o
JOIN customer c
ON o.customer_id = c.id
WHERE o.status = 'PAID';
```

Suppose:

```text
orders.customer_id

Not Indexed
```

Possible problem:

```text
Large Orders Scan

↓

Expensive Join
```

---

## Solution

```sql
CREATE INDEX idx_orders_customer
ON orders(customer_id);
```

Also check whether filtering by `status` justifies a composite index based on the actual workload.

---

# Example 5 — N+1 Performance Problem

Application:

```java
List<Order> orders =
        orderRepository.findAll();

for (Order order : orders) {

    System.out.println(
        order.getCustomer().getName()
    );
}
```

Possible SQL:

```text
1 Order Query

+

100 Customer Queries

=

101 Queries
```

---

## Solution

Use:

```text
JOIN FETCH

@EntityGraph

Batch Fetching

DTO Projection
```

Example:

```java
@Query("""
       SELECT o
       FROM Order o
       JOIN FETCH o.customer
       """)
List<Order> findAllWithCustomer();
```

---

# Example 6 — Pagination Performance

Bad deep pagination:

```sql
SELECT *
FROM orders
ORDER BY id
LIMIT 20 OFFSET 1000000;
```

Database may need to process a huge number of rows before returning 20.

---

## Better — Keyset Pagination

```sql
SELECT *
FROM orders
WHERE id > :lastId
ORDER BY id
LIMIT 20;
```

Flow:

```text
Last ID

↓

Index Seek

↓

Next 20 Rows
```

---

# Example 7 — WHERE vs HAVING

Potentially inefficient:

```sql
SELECT department_id,
       COUNT(*)
FROM employee
GROUP BY department_id
HAVING department_id = 10;
```

Better:

```sql
SELECT department_id,
       COUNT(*)
FROM employee
WHERE department_id = 10
GROUP BY department_id;
```

Flow:

```text
WHERE

↓

Filter Early

↓

GROUP BY Less Data
```

---

# Example 8 — EXISTS

Requirement:

```text
Find Customers Having Orders
```

Instead of joining only to test existence:

```sql
SELECT DISTINCT c.*
FROM customer c
JOIN orders o
ON c.id = o.customer_id;
```

consider:

```sql
SELECT c.*
FROM customer c
WHERE EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.id
);
```

This directly expresses:

```text
Does At Least One Order Exist?
```

---

# Example 9 — Remove Unnecessary DISTINCT

Query:

```sql
SELECT DISTINCT status
FROM orders;
```

`DISTINCT` may require:

```text
Sort

OR

Hash
```

Use it only when duplicate removal is actually required.

---

# Example 10 — UNION ALL

If duplicate removal isn't needed:

Instead of:

```sql
SELECT email
FROM customer

UNION

SELECT email
FROM employee;
```

use:

```sql
SELECT email
FROM customer

UNION ALL

SELECT email
FROM employee;
```

`UNION ALL` avoids duplicate-removal work.

---

# Query Tuning Checklist

When you see a slow query, check:

```text
1. SELECT *

2. Missing WHERE

3. Missing Index

4. Wrong Composite Index

5. Functions on Indexed Columns

6. Leading Wildcard

7. Unnecessary JOIN

8. N+1

9. Large OFFSET

10. Unnecessary DISTINCT

11. Unnecessary ORDER BY

12. Huge Result Set

13. Wrong Fetch Strategy

14. Outdated Statistics

15. Lock Waiting
```

---

# Database-Level Performance Tuning

Query optimization is only one part of performance tuning.

Also check:

```text
Connection Pool

Caching

Locking

Transactions

CPU

Memory

Disk

Network

Database Configuration
```

---

# Connection Pool

Spring Boot commonly uses:

```text
HikariCP
```

Monitor:

```text
Active Connections

Idle Connections

Waiting Threads

Connection Timeout
```

---

## Problem

If:

```text
Pool Size = 10

Active Requests = 100

Slow Queries Hold Connections
```

then:

```text
90 Requests Wait
```

Improving the query can often be more useful than simply increasing the pool size.

---

# Transaction Performance

Keep transactions short.

Bad:

```text
Begin Transaction

↓

Update DB

↓

Call External API

↓

Wait

↓

Update DB

↓

Commit
```

---

## Problem

```text
Connection Held

Locks Held

Other Transactions Wait
```

---

# Lock Performance

Monitor:

```text
Lock Wait

Deadlock

Long Transaction
```

A fast SQL statement can still appear slow if it spends most of its time waiting for a lock.

---

# Cache

For frequently read, slowly changing data:

```text
Application

↓

Cache

↓

Database
```

Examples:

```text
Redis

Caffeine

Hibernate L2 Cache
```

---

# Batch Operations

Instead of:

```text
1000 INSERT Requests
```

use:

```text
Batch Insert
```

Hibernate:

```properties
spring.jpa.properties.hibernate.jdbc.batch_size=50
```

---

# Read Only Transaction

For read operations:

```java
@Transactional(readOnly = true)
public List<Order> getOrders() {

    return repository.findAll();
}
```

This can allow framework/database optimizations and communicates that the operation should not modify persistent state.

---

# Production Performance Tuning Flow

```text
User Says API Is Slow

↓

Check API Metrics

↓

Check Application Trace

↓

Find Database Call

↓

Find SQL

↓

Check Query Count

↓

EXPLAIN ANALYZE

↓

Check:

Scan?

Index?

Join?

Sort?

Rows?

Lock Wait?

↓

Optimize

↓

Load Test

↓

Deploy

↓

Monitor
```

---

# What Should You Optimize First?

## High Priority

```text
Slow Queries

N+1

Missing / Wrong Indexes

Huge Result Sets

Deep Pagination

Long Transactions
```

---

## Next

```text
Connection Pool

Caching

Batch Processing

Database Configuration
```

---

# Performance Best Practices

✔ Measure before optimizing.

✔ Use `EXPLAIN` / `EXPLAIN ANALYZE`.

✔ Retrieve only required columns.

✔ Filter early.

✔ Create indexes based on real query patterns.

✔ Check composite index order.

✔ Avoid functions on indexed columns where possible.

✔ Fix N+1.

✔ Use pagination.

✔ Prefer keyset pagination for large/deep datasets.

✔ Keep transactions short.

✔ Avoid external API calls inside transactions when possible.

✔ Monitor connection pools.

✔ Use batching for large writes.

✔ Cache stable read-heavy data.

✔ Keep optimizer statistics current.

✔ Monitor lock waits and deadlocks.

✔ Load test with realistic data volumes.

---

# Interview Questions

## What is Database Performance Tuning?

Database performance tuning is the process of identifying and optimizing inefficient queries, indexes, transactions, connections, and database resource usage.

---

## How do you troubleshoot a slow query?

```text
Get SQL

↓

EXPLAIN ANALYZE

↓

Check Scan

↓

Check Index

↓

Check JOIN

↓

Check Rows

↓

Optimize

↓

Measure Again
```

---

## Is a full table scan always bad?

No.

For small tables or queries returning most rows, a full scan may be more efficient than using an index.

---

## What is the most important thing to check in an execution plan?

There isn't one universal item, but commonly check:

```text
Rows Read vs Returned

Scan Type

Index Usage

Join Algorithm

Sorts

Estimated vs Actual Rows
```

---

## Why can a query be slow even with an index?

Possible reasons:

```text
Low Selectivity

Wrong Composite Index Order

Function on Column

Outdated Statistics

Query Returns Most Rows

Lock Waiting
```

---

## How do you optimize a query returning millions of rows?

```text
Filter

↓

Pagination

↓

Required Columns Only

↓

Proper Index

↓

DTO Projection
```

---

# Quick Revision

```text
PERFORMANCE TUNING

Slow API

↓

Find SQL

↓

EXPLAIN ANALYZE

↓

Check

Scan

Index

Join

Sort

Rows

Locks

↓

Optimize

↓

Measure Again


Example

20M Orders

↓

Sequential Scan

↓

Sort

↓

2500 ms


Add

(customer_id, status, created_at)

↓

Index Scan

↓

No Large Sort

↓

Example: 5 ms


COMMON PROBLEMS

SELECT *

Missing Index

Wrong Index

N+1

Deep OFFSET

Huge Result

Long Transaction

Lock Wait


GOLDEN RULE

Measure

↓

Explain

↓

Optimize

↓

Measure Again
```




# Complex SQL Query — Normal vs Performance Tuned

## Scenario

Assume we need an **Order Search API**.

Tables:

```text
orders
customer
payment
shipment
coupon
sales_rep
blocked_customer
```

Requirements:

```text
3 INNER JOINs

2 LEFT JOINs

3 Filters

1 IN Condition

1 NOT EXISTS Condition

Sort Latest Orders First
```

---

# 1. Normal Query

```sql
SELECT
    o.id,
    o.order_number,
    o.status,
    o.total_amount,
    o.created_at,

    c.id AS customer_id,
    c.name AS customer_name,
    c.email,

    p.payment_status,
    p.payment_method,

    s.shipment_status,
    s.tracking_number,

    cp.coupon_code,

    sr.name AS sales_rep_name

FROM orders o

INNER JOIN customer c
        ON o.customer_id = c.id

INNER JOIN payment p
        ON o.id = p.order_id

INNER JOIN shipment s
        ON o.id = s.order_id

LEFT JOIN coupon cp
       ON o.coupon_id = cp.id

LEFT JOIN sales_rep sr
       ON o.sales_rep_id = sr.id

WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

AND c.active = true

AND p.payment_method IN (
    'CARD',
    'UPI',
    'NET_BANKING'
)

AND NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = c.id
)

ORDER BY o.created_at DESC;
```

---

# What This Query Does

```text
orders

↓

INNER JOIN customer

↓

INNER JOIN payment

↓

INNER JOIN shipment

↓

LEFT JOIN coupon

↓

LEFT JOIN sales_rep

↓

Filter Order Status

↓

Filter Date

↓

Filter Active Customer

↓

Payment Method IN (...)

↓

Remove Blocked Customers

↓

Sort Latest First
```

---

# Why Can This Become Slow?

Imagine:

```text
orders             = 50 Million Rows
customer           = 10 Million Rows
payment            = 50 Million Rows
shipment           = 40 Million Rows
blocked_customer   = 1 Million Rows
```

Possible problems:

```text
1. Missing indexes

2. Large table scans

3. Too many rows joined before filtering

4. SELECTing unnecessary columns

5. Sorting huge result set

6. NOT EXISTS subquery without index

7. JOIN columns not indexed

8. No pagination
```

---

# 2. Performance-Tuned Query

```sql
SELECT
    o.id,
    o.order_number,
    o.total_amount,
    o.created_at,

    c.name AS customer_name,

    p.payment_status,

    s.shipment_status,

    cp.coupon_code,

    sr.name AS sales_rep_name

FROM orders o

INNER JOIN customer c
        ON c.id = o.customer_id
       AND c.active = true

INNER JOIN payment p
        ON p.order_id = o.id
       AND p.payment_method IN (
           'CARD',
           'UPI',
           'NET_BANKING'
       )

INNER JOIN shipment s
        ON s.order_id = o.id

LEFT JOIN coupon cp
       ON cp.id = o.coupon_id

LEFT JOIN sales_rep sr
       ON sr.id = o.sales_rep_id

WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

AND NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = o.customer_id
)

ORDER BY o.created_at DESC

LIMIT 50;
```

---

# What Changed?

## 1. Removed Unnecessary Columns

Normal:

```sql
SELECT
    c.email,
    p.payment_method,
    s.tracking_number,
    ...
```

If the API doesn't need them, don't fetch them.

Tuned:

```sql
SELECT
    o.id,
    o.order_number,
    o.total_amount,
    o.created_at,
    c.name,
    p.payment_status,
    s.shipment_status,
    cp.coupon_code,
    sr.name
```

---

## Why?

Reduces:

```text
Disk I/O

Network Traffic

Memory

JPA Mapping Cost
```

---

# 2. Move Related Filters Close to JOIN

Original:

```sql
INNER JOIN customer c
ON o.customer_id = c.id

WHERE c.active = true
```

Tuned:

```sql
INNER JOIN customer c
ON c.id = o.customer_id
AND c.active = true
```

This makes the intent clearer.

For an `INNER JOIN`, the optimizer will often produce an equivalent plan either way.

The main benefit is:

```text
Filter Related Table Early

↓

Join Fewer Rows
```

---

# Important Note

For `INNER JOIN`:

```sql
ON c.active = true
```

and:

```sql
WHERE c.active = true
```

are often logically equivalent.

Do not assume moving the predicate alone makes the query faster.

The real improvement usually comes from:

```text
Proper Indexes

Statistics

Reduced Rows

Better Query Shape
```

---

# 3. Keep LEFT JOIN Filters in ON When Optional Data Must Stay Optional

Suppose we want all orders, even those without a coupon.

Correct:

```sql
LEFT JOIN coupon cp
ON cp.id = o.coupon_id
```

If you add:

```sql
WHERE cp.active = true
```

you may accidentally turn the effective result into an `INNER JOIN`.

---

## Better

```sql
LEFT JOIN coupon cp
ON cp.id = o.coupon_id
AND cp.active = true
```

Now:

```text
Order Without Coupon

↓

Still Returned
```

---

# 4. Add Pagination

Normal query:

```sql
ORDER BY o.created_at DESC;
```

could return:

```text
1 Million Rows
```

Tuned:

```sql
ORDER BY o.created_at DESC
LIMIT 50;
```

---

## Why?

API normally doesn't need everything.

```text
Millions of Rows

↓

Return 50
```

Massive reduction in:

```text
Memory

Network

Query Processing
```

---

# 5. Index the Main Filter + Sort

Main query:

```sql
WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

ORDER BY o.created_at DESC
```

Potential index:

```sql
CREATE INDEX idx_orders_status_created_customer
ON orders(
    status,
    created_at DESC,
    customer_id
);
```

---

## Why This Index?

Query pattern:

```text
status

↓

created_at

↓

customer_id used for JOIN / NOT EXISTS
```

This may help the database:

```text
Find COMPLETED Orders

↓

Read Recent Orders in Order

↓

Avoid Large Sort

↓

Join Customer
```

---

# 6. Index Customer Join / Filter

Primary key:

```text
customer.id
```

is normally already indexed.

But if active customer filtering is important and the database/query pattern benefits, consider an index such as:

```sql
CREATE INDEX idx_customer_active_id ON customer(active, id);
```

Whether this helps depends on:

```text
How many customers are active?

Data distribution

Execution plan
```

If:

```text
99% customers = active
```

`active` alone is low-selectivity and may not help much.

---

# 7. Index Payment JOIN + IN Filter

Query:

```sql
p.order_id = o.id

AND p.payment_method IN (...)
```

Potential index:

```sql
CREATE INDEX idx_payment_order_method
ON payment(
    order_id,
    payment_method
);
```

---

## Why?

Supports:

```text
Find Payment by Order

+

Check Payment Method
```

---

# 8. Index Shipment JOIN

Query:

```sql
s.order_id = o.id
```

Create:

```sql
CREATE INDEX idx_shipment_order
ON shipment(order_id);
```

---

# 9. Index NOT EXISTS Subquery ⭐

Query:

```sql
NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = o.customer_id
)
```

Create:

```sql
CREATE INDEX idx_blocked_customer_customer
ON blocked_customer(customer_id);
```

---

## Without Index

For every candidate order:

```text
Search Blocked Customer Table

↓

Potential Scan
```

---

## With Index

```text
Customer ID

↓

Index Lookup

↓

Exists / Doesn't Exist
```

This is very important.

---

# 10. LEFT JOIN Indexes

For:

```sql
cp.id = o.coupon_id
```

`coupon.id` is usually a PK index.

For:

```sql
sr.id = o.sales_rep_id
```

`sales_rep.id` is normally a PK index.

The foreign keys inside `orders` may already be used efficiently because the query starts from `orders`.

---

# Recommended Indexes

```sql
CREATE INDEX idx_orders_status_created_customer
ON orders(
    status,
    created_at DESC,
    customer_id
);

CREATE INDEX idx_payment_order_method
ON payment(
    order_id,
    payment_method
);

CREATE INDEX idx_shipment_order
ON shipment(order_id);

CREATE INDEX idx_blocked_customer_customer
ON blocked_customer(customer_id);
```

Potentially, depending on the real workload:

```sql
CREATE INDEX idx_customer_active_id
ON customer(active, id);
```

---

# Before Tuning — Possible Execution Flow

```text
Orders

50 Million Rows

↓

Large Scan

↓

JOIN Customer

↓

JOIN Payment

↓

JOIN Shipment

↓

LEFT JOIN Coupon

↓

LEFT JOIN Sales Rep

↓

NOT EXISTS Check

↓

Large Sort

↓

Return Huge Result
```

---

# After Tuning — Desired Flow

```text
Orders Composite Index

↓

COMPLETED

↓

Recent Orders

↓

Already in created_at Order

↓

Check Blocked Customer via Index

↓

Join Customer by PK

↓

Join Payment via order_id Index

↓

Join Shipment via order_id Index

↓

Optional Coupon Lookup

↓

Optional Sales Rep Lookup

↓

Return First 50
```

---

# Example EXPLAIN Before

Illustrative plan:

```text
Sort
  Sort Key: o.created_at DESC

  -> Hash Left Join
       -> Hash Left Join
            -> Hash Join
                 -> Hash Join
                      -> Seq Scan on orders
                           Filter:
                           status = 'COMPLETED'
                           AND created_at >= ...

                      -> Seq Scan on customer
                           Filter: active = true

                 -> Seq Scan on payment
                      Filter:
                      payment_method IN (...)

            -> Seq Scan on shipment

       -> coupon / sales_rep lookups

SubPlan NOT EXISTS
  -> Seq Scan blocked_customer
```

Potential issues:

```text
Sequential Scan orders

Sequential Scan payment

Sequential Scan shipment

Sequential Scan blocked_customer

Large Sort
```

---

# Example EXPLAIN After

Illustrative plan:

```text
Limit

↓

Nested Loop

↓

Index Scan using
idx_orders_status_created_customer

↓

Index Scan customer_pkey

↓

Index Scan
idx_payment_order_method

↓

Index Scan
idx_shipment_order

↓

Index Scan coupon_pkey

↓

Index Scan sales_rep_pkey

↓

Anti Join / Index Lookup
idx_blocked_customer_customer
```

Desired result:

```text
Far Fewer Rows Read

↓

No Huge Sort

↓

Indexed JOIN Lookups

↓

Fast NOT EXISTS

↓

Only 50 Rows Returned
```

---

# Normal Query vs Tuned Query

## Normal

```sql
SELECT
    o.*,
    c.*,
    p.*,
    s.*,
    cp.*,
    sr.*

FROM orders o

INNER JOIN customer c
ON o.customer_id = c.id

INNER JOIN payment p
ON o.id = p.order_id

INNER JOIN shipment s
ON o.id = s.order_id

LEFT JOIN coupon cp
ON o.coupon_id = cp.id

LEFT JOIN sales_rep sr
ON o.sales_rep_id = sr.id

WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

AND c.active = true

AND p.payment_method IN (
    'CARD',
    'UPI',
    'NET_BANKING'
)

AND NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = c.id
)

ORDER BY o.created_at DESC;
```

---

# Tuned

```sql
SELECT
    o.id,
    o.order_number,
    o.total_amount,
    o.created_at,

    c.name AS customer_name,

    p.payment_status,

    s.shipment_status,

    cp.coupon_code,

    sr.name AS sales_rep_name

FROM orders o

INNER JOIN customer c
        ON c.id = o.customer_id
       AND c.active = true

INNER JOIN payment p
        ON p.order_id = o.id
       AND p.payment_method IN (
           'CARD',
           'UPI',
           'NET_BANKING'
       )

INNER JOIN shipment s
        ON s.order_id = o.id

LEFT JOIN coupon cp
       ON cp.id = o.coupon_id

LEFT JOIN sales_rep sr
       ON sr.id = o.sales_rep_id

WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

AND NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = o.customer_id
)

ORDER BY o.created_at DESC

LIMIT 50;
```

---

# Further Optimization — If Only Existence Is Needed

Suppose payment information is not returned.

You only need:

```text
Order must have CARD / UPI / NET_BANKING payment.
```

Instead of:

```sql
INNER JOIN payment p
ON p.order_id = o.id
AND p.payment_method IN (...)
```

you could use:

```sql
AND EXISTS (
    SELECT 1
    FROM payment p
    WHERE p.order_id = o.id
    AND p.payment_method IN (
        'CARD',
        'UPI',
        'NET_BANKING'
    )
)
```

---

## Why?

A JOIN can create duplicate order rows if an order has multiple payment records.

If the requirement is only:

```text
Does a matching payment exist?
```

then:

```text
EXISTS
```

may express the logic better.

---

# Even Better Query for Existence-Only Payment

```sql
SELECT
    o.id,
    o.order_number,
    o.total_amount,
    o.created_at,

    c.name AS customer_name,

    s.shipment_status,

    cp.coupon_code,

    sr.name AS sales_rep_name

FROM orders o

INNER JOIN customer c
        ON c.id = o.customer_id
       AND c.active = true

INNER JOIN shipment s
        ON s.order_id = o.id

LEFT JOIN coupon cp
       ON cp.id = o.coupon_id

LEFT JOIN sales_rep sr
       ON sr.id = o.sales_rep_id

WHERE o.status = 'COMPLETED'

AND o.created_at >= '2026-01-01'

AND EXISTS (
    SELECT 1
    FROM payment p
    WHERE p.order_id = o.id
    AND p.payment_method IN (
        'CARD',
        'UPI',
        'NET_BANKING'
    )
)

AND NOT EXISTS (
    SELECT 1
    FROM blocked_customer bc
    WHERE bc.customer_id = o.customer_id
)

ORDER BY o.created_at DESC

LIMIT 50;
```

---

# When JOIN vs EXISTS?

Use:

```text
JOIN
```

when you need columns from the joined table.

Example:

```text
payment_status
```

Use:

```text
EXISTS
```

when you only need to check:

```text
Does matching row exist?
```

This can also help avoid row multiplication.

---

# What If Payment Has Multiple Rows?

Example:

```text
Order 101

Payment 1 → FAILED

Payment 2 → CARD SUCCESS

Payment 3 → UPI SUCCESS
```

Using an inner join can return:

```text
Order 101

Order 101

Order 101
```

multiple times.

If you only need to know that at least one matching payment exists:

```text
EXISTS
```

is usually clearer.

---

# Performance Tuning Checklist for Complex Queries

For a query containing:

```text
3 INNER JOIN

2 LEFT JOIN

3 Filters

IN

NOT EXISTS

ORDER BY
```

check these in order:

```text
1. Starting Table

2. WHERE Filters

3. Rows Remaining After Filter

4. JOIN Columns Indexed?

5. IN Column Indexed?

6. NOT EXISTS Column Indexed?

7. LEFT JOIN Really Needed?

8. Need Columns from Every JOIN?

9. Row Multiplication?

10. SELECT *?

11. ORDER BY Indexed?

12. Pagination?

13. Estimated vs Actual Rows?

14. Statistics Current?
```

---

# Best Practices

✔ Filter the main table as early as possible.

✔ Index important `WHERE` columns.

✔ Index JOIN foreign keys where beneficial.

✔ Index `NOT EXISTS` correlation columns.

✔ Use `EXISTS` if you only need existence.

✔ Don't use a JOIN just to check existence.

✔ Avoid `SELECT *`.

✔ Keep `LEFT JOIN` filters in `ON` when you want to preserve unmatched parent rows.

✔ Avoid unnecessary `LEFT JOIN`s.

✔ Watch for one-to-many row multiplication.

✔ Use composite indexes matching real query patterns.

✔ Support `ORDER BY` with a useful index where possible.

✔ Paginate large results.

✔ Run `EXPLAIN ANALYZE`.

✔ Compare estimated and actual row counts.

✔ Don't assume a rewritten query is faster—measure it.

---

# Interview Answer

## How would you optimize a complex query with multiple JOINs?

```text
1. Filter the driving table early.

2. Fetch only required columns.

3. Index WHERE and JOIN columns.

4. Use composite indexes for filter + sort patterns.

5. Replace JOIN with EXISTS when only existence is needed.

6. Index NOT EXISTS subquery columns.

7. Remove unnecessary JOINs.

8. Avoid row multiplication.

9. Add pagination.

10. Validate everything with EXPLAIN ANALYZE.
```

---

# Quick Revision

```text
COMPLEX QUERY

3 INNER JOIN

2 LEFT JOIN

3 FILTERS

IN

NOT EXISTS

ORDER BY


TUNING

↓

Filter Main Table

↓

Composite Index

↓

Join by Indexed Keys

↓

EXISTS for Existence Only

↓

NOT EXISTS + Index

↓

Avoid SELECT *

↓

Avoid Unnecessary LEFT JOIN

↓

Avoid Duplicate Explosion

↓

Index ORDER BY

↓

LIMIT / Pagination

↓

EXPLAIN ANALYZE


Golden Rule

Reduce Rows

BEFORE

Expensive JOINs
```



