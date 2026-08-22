# Java Threads - Interview Roadmap


```text
                    Object
                       │
                       ▼
                 Runnable (Interface)
                       ▲
                       │ implements
                  Thread (Class)
                       │
        ┌──────────────┼──────────────┐
        │              │              │
   MyThread      WorkerThread    DownloadThread
 (extends Thread) (extends Thread) (extends Thread)
```

## 1. Introduction to Threads
- Process vs Thread
- What is a Thread?
- Multitasking
- Thread Lifecycle (New, Runnable, Running, Waiting, Timed Waiting, Blocked, Terminated)

---

**Process** :-  is an **independent program in execution**.
- Has its own memory space.
- Has its own resources (CPU, Memory, Files).
- One process cannot directly access another process's memory.
### Example

```text
Chrome Browser
VS Code
Spotify
Notepad
```

**Thread** :-  is the **smallest unit of execution** inside a process.
- A process can have **multiple threads**.
- Threads share the same process memory.
- Threads execute tasks concurrently.
  Chrome Browser
```text
Chrome Process
├── UI Thread
├── Network Thread
├── Rendering Thread
├── Download Thread
└── JavaScript Thread
```
All threads belong to the same process.

# Process vs Thread
| Process                    | Thread                                |
|----------------------------|---------------------------------------|
| Independent program        | Smallest execution unit               |
| Own memory                 | Shares process memory                 |
| Heavyweight                | Lightweight                           |
| Slow creation              | Fast creation                         |
| Communication is expensive | Communication is easy (shared memory) |


# 3. What is Multitasking?
**Multitasking** is the ability of the operating system to execute **multiple tasks simultaneously**.
There are two types.
---
## A. Process-Based Multitasking:- Multiple **processes** execute simultaneously.
Example
```text
Chrome
VS Code
Spotify
```
Each is a separate process.
---

## B. Thread-Based Multitasking
Multiple **threads** execute inside the same process.
Example
```text
Web Server
Request 1 → Thread-1
Request 2 → Thread-2
Request 3 → Thread-3
```
Spring Boot uses thread-based multitasking.
Each incoming request is handled by a separate thread from the thread pool.
---

# Why Threads?
Without Threads
```text
Download File
↓
Play Music
↓
Open Browser
```
Everything executes one after another.
---

With Threads
```text
Download File
        │
Play Music
        │
Browse Internet
```
Everything happens simultaneously.
---


# 4. Thread Lifecycle

A thread goes through several states during its lifetime.

```text
NEW
 │
 ▼
RUNNABLE
 │
 ▼
RUNNING
 │
 ├──────────────┐
 ▼              ▼
WAITING      TIMED_WAITING
 │              │
 └──────┐       │
        ▼       ▼
      RUNNABLE
          │
          ▼
       TERMINATED
```
### NEW
The thread is created but has **not been started** yet.

### RUNNABLE
The thread is ready to run and is waiting for the CPU scheduler to allocate processor time.

### RUNNING
The thread is actively executing its task on the CPU. *(Conceptual state; Java represents it as RUNNABLE.)*

### BLOCKED
The thread is waiting to acquire a monitor lock held by another thread.

### WAITING
The thread waits indefinitely until another thread notifies or interrupts it.

### TIMED_WAITING
The thread waits for a specified period of time before becoming runnable again.

### TERMINATED
The thread has completed execution or has been stopped and cannot be restarted.
---

# Shutdown Hook (Java)
## What is a Shutdown Hook?
- A **Shutdown Hook** is a thread that the JVM executes **just before the application shuts down**.
- Used to perform **cleanup tasks** like closing files, database connections, or releasing resources.
- Registered using `Runtime.getRuntime().addShutdownHook()`.

### Example

```java
Runtime.getRuntime().addShutdownHook(new Thread(() -> {
    System.out.println("Cleaning resources...");
}));
```

### Interview Points
- Runs before the JVM exits.
- Used for resource cleanup.
- May not execute if the JVM is forcibly terminated (e.g., `kill -9`).
---

# Daemon Thread
## What is a Daemon Thread?
- A **Daemon Thread** is a **background thread** that supports user threads.
- The JVM **automatically terminates daemon threads** when all user (non-daemon) threads finish.
- Used for background tasks like **Garbage Collection**.

### Example
```java
Thread t = new Thread(() -> {
    while (true) {
        System.out.println("Background Task");
    }
});

t.setDaemon(true);
t.start();
```
### Examples of Daemon Threads

- Garbage Collector (GC)
- Finalizer (legacy)
- Background monitoring threads
---
# hutdown Hook vs Daemon Thread
| Feature    | Shutdown Hook                             | Daemon Thread        |
|------------|-------------------------------------------|----------------------|
| Purpose    | Cleanup before JVM exits                  | Background tasks     |
| Runs       | During JVM shutdown                       | While JVM is running |
| JVM Waits? | ✅ Yes (normally waits for hook to finish) | ❌ No                 |
| Example    | Close DB/File                             | Garbage Collector    |

---

# Quick Revision

```text
Shutdown Hook
✔ Executes before JVM exits
✔ Cleanup resources
✔ addShutdownHook()

Daemon Thread
✔ Background thread
✔ Supports user threads
✔ JVM exits when only daemon threads remain
✔ Example: Garbage Collector
```

## 2. Creating Threads
- Extending `Thread`
- Implementing `Runnable`
- Using `Callable` & `Future`
- Lambda Expressions

# Creating Threads in Java

## 1. Extending `Thread`

Create a class that extends `Thread` and override the `run()` method.
### Key Point
Use `start()`, not `run()` directly.  
`start()` creates a new thread, `run()` behaves like a normal method call.
### When to Use
- Rarely used in real projects.
- Suitable for **simple learning** or small demo programs.
- Avoid if your class needs to extend another class (Java supports single inheritance).
---

## 2. Implementing `Runnable`
Create a class that implements `Runnable` and pass it to a `Thread`.
### Key Point
Preferred over extending `Thread` because Java supports only single class inheritance.
### When to Use
- When the task **does not return a result**.
- Best for **background tasks** and **multi-threaded applications**.
- Preferred because your class can still extend another class.
---

## 3. Using `Callable` and `Future`
Use `Callable` when the thread should **return a result** or **throw checked exceptions**.
### Key Point
- `Runnable` cannot return value.
- `Callable` can return value using `Future`.
### When to Use
- When the task **returns a value**.
- When the task **may throw checked exceptions**.
- Used when the caller needs the result after the thread completes.
---

## 4. Lambda Expression
Since `Runnable` is a functional interface, we can use lambda.
### Key Point
Lambda makes thread creation short and clean.

### When to Use
- When the thread logic is **small and simple**.
- Mostly used with `Runnable`.
- Makes the code cleaner and shorter.
---

# One Class With All Examples
  [ThreadCreationDemo.java](ThreadCreationDemo.java)

# Quick Revision

```text
Creating Threads

1. Extends Thread
   → Override run()
   → Call start()

2. Implements Runnable
   → Better approach
   → No return value

3. Callable + Future
   → Returns value
   → Can throw exception

4. Lambda
   → Short syntax for Runnable

Important:
start() → Creates new thread
run()   → Normal method call
```

### Interview Answer
- **Runnable** → Use when you want to perform a task without returning a result.
- **Callable** → Use when you need to return a result or handle checked exceptions.
- **Thread** → Use only for simple examples; in real applications, prefer `Runnable` or `Callable`.
- **Lambda** → Use for concise thread creation when the task is simple.
---

## 3. Thread Methods
# Important Thread Methods
- **start()** :- Starts a new thread and internally invokes `run()`.  
  **Use when:** You want to execute a task concurrently in a separate thread.

- **run()** :- Contains the task executed by the thread. Calling it directly does not create a new thread.  
  **Use when:** Defining the logic that the thread should execute.

- **sleep()** :- Pauses the current thread for a specified duration.  
  **Use when:** You want to delay execution or simulate waiting.

- **join()** :- Makes the current thread wait until another thread finishes.  
  **Use when:** One thread depends on the result of another thread.

- **yield()** :- Hints the scheduler to give other threads a chance to execute.  
  **Use when:** You want to improve fairness between threads (not guaranteed).

- **interrupt()** :- Sends an interrupt signal to a thread.  
  **Use when:** You want to stop or cancel a sleeping/waiting thread gracefully.

- **isAlive()** :- Checks whether a thread is still running.  
  **Use when:** You need to verify if a thread has completed execution.

- **setPriority()** :- Sets the priority of a thread (1–10).  
  **Use when:** You want to influence thread scheduling priority.

- **getPriority()** :- Returns the priority of a thread.  
  **Use when:** You need to check the current thread priority.

- **currentThread()** :- Returns the currently executing thread.  
  **Use when:** You need the current thread's name, ID, or other information.

# One Class With All method Examples
[ThreadMethodsDemo.java](ThreadMethodsDemo.java)
---

## 4. Thread Synchronization

## 1. What is Synchronization?
Synchronization is a mechanism that **allows only one thread at a time to access a shared resource**, preventing data inconsistency in a multi-threaded environment.
**Use when:** Multiple threads access or modify shared data.
---

## 2. Race Condition
A **Race Condition** occurs when multiple threads access and modify shared data simultaneously, leading to unpredictable or incorrect results.
**Example:** Two threads executing `count++` at the same time.
---

## 3. Synchronized Method
A method declared with the `synchronized` keyword. Only **one thread** can execute the method on the same object at a time.
```java
public synchronized void increment() {
    count++;
}
```
**Use when:** The entire method needs thread-safe access.
---

## 4. Synchronized Block
Locks only a specific block of code instead of the entire method.
```java
synchronized(lock) {
    count++;
}
```
**Use when:** Only a small section of code requires synchronization, improving performance.
---

## 5. Static Synchronization
A `static synchronized` method locks the **Class object**, not an instance.
```java
public static synchronized void print() {

}
```
**Use when:** Protecting shared **static variables** or class-level resources.
---

## 6. Object Lock vs Class Lock

### Object Lock
- Each object has its **own lock**.
- Acquired using a synchronized instance method or `synchronized(this)`.

```java
synchronized(this) {

}
```
**Use when:** Protecting instance variables.
---
### Class Lock
- One lock per **class**, shared by all objects.
- Acquired using a `static synchronized` method or `synchronized(ClassName.class)`.

```java
synchronized(MyClass.class) {

}
```
**Use when:** Protecting static variables shared across all objects.
---

# Quick Revision

```text
Synchronization       → One thread at a time
Race Condition        → Multiple threads modify shared data
Synchronized Method   → Locks entire method
Synchronized Block    → Locks specific code block
Static Synchronization→ Locks class
Object Lock           → Per object
Class Lock            → Per class
```

## 5. Thread Communication
## `wait()`
- Causes the current thread to **release the object lock** and wait until another thread calls `notify()` or `notifyAll()`.
- Must be called inside a **synchronized** block or method.
**Use when:** A thread needs to wait for another thread to complete some work.
---

## `notify()`
- Wakes up **one waiting thread** on the same object.
- The awakened thread acquires the lock only after it becomes available.
**Use when:** Only one waiting thread needs to continue execution.
---

## `notifyAll()`
- Wakes up **all threads** waiting on the same object.
- Threads compete to acquire the object's lock.
**Use when:** Multiple waiting threads may proceed.
---

## Producer-Consumer Problem
- A classic synchronization problem where a **Producer** adds data to a shared buffer and a **Consumer** removes data.
- `wait()` is used when the buffer is **empty/full**, and `notify()`/`notifyAll()` wakes the waiting thread when the state changes.

code example 
[ProducerConsumerDemo.java](ProducerConsumerDemo.java)
---


## 6. Locks# 
Java Locks
## ReentrantLock
- A thread can acquire the **same lock multiple times** without causing a deadlock.
- Provides advanced features like `tryLock()`, `lockInterruptibly()`, and fair locking.
- **Use when:** You need more flexibility than `synchronized`.
---

## ReadWriteLock
- Allows **multiple threads to read simultaneously**, but only **one thread can write** at a time.
- Improves performance in read-heavy applications.
- **Use when:** Reads are frequent and writes are less frequent.
---

## StampedLock
- A high-performance lock that supports **read lock, write lock, and optimistic read**.
- Optimistic reads reduce locking overhead when writes are rare.
- **Use when:** Maximum performance is needed in read-heavy scenarios.
---

## Fair Lock vs Unfair Lock
### Fair Lock
- Grants the lock to threads **in the order they requested it (FIFO)**.
- **Use when:** Preventing thread starvation is important.

### Unfair Lock
- Allows any thread to acquire the lock, even if others are waiting.
- **Use when:** Higher throughput is preferred over fairness.
---

## Fine-Grained Locking
- Locks **only a small portion** of a resource instead of the entire object.
- Allows multiple threads to work concurrently on different parts of the resource.
- **Use when:** High concurrency and better performance are required (e.g., `ConcurrentHashMap`).


## 7. Volatile
- The `volatile` keyword ensures that a variable is **always read from and written to main memory**, making updates immediately visible to all threads.
- It guarantees **visibility** but **does not provide atomicity**.
- **Use when:** Multiple threads read/write a shared flag or status variable.
```java
volatile boolean running = true;
```

---
# Visibility Problem
- Occurs when one thread updates a shared variable, but other threads **cannot immediately see the updated value** because they are using cached copies.
- `volatile` solves this by forcing reads and writes to **main memory**.
- **Use case:** Thread stop flags, status indicators.
---

# Happens-Before Relationship
- A Java Memory Model (JMM) rule that guarantees **changes made by one thread are visible to another thread** in a specific order.
- Operations before a **`volatile` write**, `synchronized` unlock, or `Thread.start()/join()` happen-before corresponding operations in another thread.
- **Use when:** You need predictable visibility and ordering of operations between threads.
---

## 8. Atomic Classes
# AtomicInteger
- `AtomicInteger` is a **thread-safe** integer class that performs atomic operations without using `synchronized`.
- Uses **CAS (Compare-And-Set)** internally for better performance.
- **Use when:** Multiple threads update an integer counter.

```java
AtomicInteger counter = new AtomicInteger(0);
counter.incrementAndGet();
```
---

# AtomicLong

- `AtomicLong` is a **thread-safe** class for `long` values.
- Supports atomic operations like increment, decrement, and compare-and-set.
- **Use when:** Managing large counters or IDs in concurrent applications.

```java
AtomicLong id = new AtomicLong(100);
id.incrementAndGet();
```
---

# AtomicReference
- `AtomicReference` provides **atomic updates** for object references.
- Ensures that object references are updated safely in multi-threaded environments.
- **Use when:** Multiple threads update the same object reference.

```java
AtomicReference<String> ref = new AtomicReference<>("Java");
ref.set("Spring");
```

---

# CAS (Compare-And-Set)
- **CAS (Compare-And-Set)** is an atomic operation that updates a value **only if it matches the expected value**.
- Prevents race conditions without using locks, making it faster than synchronization.
- **Used internally by:** `AtomicInteger`, `AtomicLong`, `AtomicReference`, etc.
```java
AtomicInteger count = new AtomicInteger(10);
count.compareAndSet(10, 20);
```
---



## 9. Executors Framework :-
is a high-level concurrency framework in Java that **simplifies thread creation, management,
and execution** using thread pools instead of creating threads manually.

# Executor
- `Executor` is the **basic interface** for executing tasks asynchronously.
- It separates **task submission** from **task execution**.
- **Use when:** You only need to execute tasks without managing threads manually.
```java
Executor executor = Executors.newSingleThreadExecutor();
executor.execute(task);
```
---

# ExecutorService
- `ExecutorService` extends `Executor` and provides **thread pool management**.
- Supports task submission, shutdown, and returning results using `Future`.
- **Use when:** Managing multiple concurrent tasks efficiently.
```java
ExecutorService service = Executors.newFixedThreadPool(5);
```
---

# ScheduledExecutorService
- Executes tasks **after a delay** or **at fixed intervals**.
- Replaces `Timer` and `TimerTask`.
- **Use when:** Scheduling periodic or delayed tasks.

```java
ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
```

---

# ThreadPoolExecutor
- A configurable implementation of `ExecutorService`.
- Allows control over **core threads, maximum threads, queue size, and rejection policy**.
- **Use when:** Fine-grained control over thread pool behavior is required.

```java
ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
```
---

# Cached Thread Pool
- Creates new threads as needed and **reuses idle threads**.
- No fixed number of threads.
- **Use when:** Executing many short-lived asynchronous tasks.

```java
ExecutorService service = Executors.newCachedThreadPool();
```
---

# Fixed Thread Pool
- Creates a **fixed number of reusable threads**.
- Extra tasks wait in a queue until a thread becomes available.
- **Use when:** You know the required number of concurrent threads.

```java
ExecutorService service = Executors.newFixedThreadPool(5);
```
---

# Single Thread Executor

- Uses **only one worker thread** to execute tasks sequentially.
- Ensures tasks execute **one after another** in submission order.
- **Use when:** Sequential execution is required.

```java
ExecutorService service = Executors.newSingleThreadExecutor();
```
---
[ExecutorsDemo.java](ExecutorsDemo.java)
Code example 

---

## 10. Fork/Join Framework
- ForkJoinPool
- RecursiveTask
- RecursiveAction
- Work Stealing Algorithm
---

## 11. Concurrent Collections
- ConcurrentHashMap
- CopyOnWriteArrayList
- CopyOnWriteArraySet
- ConcurrentLinkedQueue
- BlockingQueue
---

## 12. BlockingQueue
- ArrayBlockingQueue
- LinkedBlockingQueue
- PriorityBlockingQueue
- DelayQueue
- SynchronousQueue
---

## 13. Thread Safety

## Thread-Safe
- A class is **thread-safe** if multiple threads can access it simultaneously without causing data inconsistency.
- Achieved using synchronization, locks, atomic classes, or concurrent collections.
- **Examples:** `ConcurrentHashMap`, `BlockingQueue`, `Vector`.
---

## Non-Thread-Safe
- A class is **non-thread-safe** if concurrent access by multiple threads can lead to race conditions or inconsistent data.
- Requires external synchronization when shared between threads.
- **Examples:** `ArrayList`, `HashMap`, `LinkedList`.

---

# Immutable Objects
- An **immutable object** cannot be modified after it is created.
- Since its state never changes, it is inherently **thread-safe**.
- **Examples:** `String`, wrapper classes (`Integer`, `Long`), `LocalDate`.
---

# Synchronization
- **Synchronization** allows only one thread at a time to access a shared resource, preventing race conditions.
- Achieved using the `synchronized` keyword, locks, or atomic classes.
- **Use when:** Multiple threads modify shared data.
---

# Concurrent Collections

- **Concurrent Collections** are thread-safe collections optimized for high-performance concurrent access.
- They allow multiple threads to read/write with minimal blocking.
- **Examples:** `ConcurrentHashMap`, `CopyOnWriteArrayList`, `BlockingQueue`.
---


## 14. Deadlock
## What is Deadlock?
- A **Deadlock** occurs when two or more threads are **waiting indefinitely for each other to release resources**, so none of them can proceed.
- Each thread holds one resource and waits for another, causing the application to hang.
---

# Causes of Deadlock
- **Mutual Exclusion** – A resource can be used by only one thread at a time.
- **Hold and Wait** – A thread holds one resource while waiting for another.
- **No Preemption** – Resources cannot be forcibly taken from a thread.
- **Circular Wait** – Two or more threads wait in a circular chain for resources held by each other.
---

# Prevention
- Acquire locks in a **consistent order**.
- Avoid holding multiple locks simultaneously when possible.
- Use **`tryLock()`** with a timeout instead of waiting indefinitely.
- Minimize the scope of synchronized blocks.
---

# Detection
- Use **`jstack`** to analyze thread dumps.
- Use **JVisualVM** or **Java Mission Control (JMC)** to detect deadlocks.
- Programmatically detect deadlocks using **`ThreadMXBean`**.
---


## 15. Livelock & Starvation
# Livelock
- A **Livelock** occurs when two or more threads keep responding to each other but **make no actual progress**.
- Threads are **active** but continuously retry or back off instead of completing the task.
- **Example:** Two people repeatedly stepping aside to let each other pass but never moving forward.
---

# Starvation
- **Starvation** occurs when a thread is **unable to get CPU time or resources** because other threads continuously get priority.
- The thread waits indefinitely even though the system is still making progress.
- **Example:** A low-priority thread never gets CPU time because high-priority threads keep executing.

---

# Difference from Deadlock

| Deadlock                                        | Livelock                                              | Starvation                                                                             |
|-------------------------------------------------|-------------------------------------------------------|----------------------------------------------------------------------------------------|
| Threads are **blocked** waiting for each other. | Threads are **active** but make no progress.          | A thread waits indefinitely because resources are continuously given to other threads. |
| No thread makes progress.                       | Threads keep changing state but no work is completed. | Other threads continue executing normally.                                             |
| Caused by circular waiting for locks.           | Caused by continuous retries or mutual responses.     | Caused by unfair scheduling or resource allocation.                                    |

---

# Quick Revision

```text
Deadlock
→ Waiting forever
→ Threads are BLOCKED

Livelock
→ Running forever
→ Threads are ACTIVE but no progress

Starvation
→ One thread never gets CPU/resources
→ Other threads continue normally
```
---

## 16. Fail-Fast & Fail-Safe Iterators
## Fail-Fast Iterator

- A **Fail-Fast** iterator throws **`ConcurrentModificationException`** if the collection is structurally modified during iteration.
- It iterates over the **original collection** and detects modifications using **`modCount`**.
- **Examples:** `ArrayList`, `HashMap`, `HashSet`.

```java
List<String> list = new ArrayList<>();
```
---

## Fail-Safe Iterator

- A **Fail-Safe** iterator **does not throw** `ConcurrentModificationException` during iteration.
- It iterates over a **snapshot (copy)** or a **weakly consistent view** of the collection.
- **Examples:** `CopyOnWriteArrayList`, `ConcurrentHashMap`.

```java
CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
```
---

## 17. CompletableFuture (Java 8+)
- `supplyAsync()`
- `runAsync()`
- `thenApply()`
- `thenAccept()`
- `thenCombine()`
- Exception Handling

---

## 18. Java Memory Model (JMM)
- Heap
- Stack
- Method Area
- Visibility
- Happens-Before Rule

---

## 19. ThreadLocal

## What is ThreadLocal?
- `ThreadLocal` provides a **separate copy of a variable for each thread**, so each thread has its own independent value.
- Changes made by one thread are **not visible** to other threads.
- **Use when:** Thread-specific data needs to be stored without sharing between threads.

```java
ThreadLocal<String> user = new ThreadLocal<>();
```
---

# Use Cases
- Store **logged-in user/session information**.
- Database connection per thread.
- Transaction context.
- Request tracing or correlation IDs.
- Date formatting (`SimpleDateFormat`) in older Java versions.

---

# Memory Leak Considerations :- 
A memory leak occurs when objects are no longer needed but are still referenced, preventing the Garbage Collector from reclaiming their memory.**

- In thread pools, `ThreadLocal` values remain associated with the thread even after the task completes.
- Always call **`remove()`** after using a `ThreadLocal` to avoid memory leaks.
- This is especially important in application servers and Spring Boot applications using thread pools.
```java
try {
    threadLocal.set("Manoj");
    // Business Logic
} finally {
    threadLocal.remove();
}
```
* Unreleased Object References :- Objects are no longer needed but are still referenced. 
* ThreadLocal Not Removed :- Values remain attached to thread pool threads if `remove()` is not called.

```java
try {
    threadLocal.set(user);
} finally {
    threadLocal.remove();
}
```
* Static Variables :- Static objects remain in memory for the lifetime of the application. 
  * Unclosed Resources
    - Database Connections
    - File Streams
    - Socket Connections
    Use **try-with-resources**.
* Growing Collections :- Collections keep growing because objects are never removed.

* Event Listeners :- Listeners are registered but never unregistered.
* Cache Without Eviction :- Cache grows indefinitely.
Example:
```java
Map<String, Object> cache;
```
---

# Prevention
- Remove unused references.
- Call `ThreadLocal.remove()`.
- Close resources using **try-with-resources**.
- Remove unused collection entries.
- Use bounded caches (e.g., Caffeine, Guava).
---

# Detection
- Heap Dump Analysis
- Eclipse MAT (Memory Analyzer Tool)
- VisualVM
- Java Mission Control (JMC)
- JProfiler / YourKit
---

# Quick Revision
```text
Main Causes of Memory Leak

✔ Unreleased Object References ⭐
✔ ThreadLocal not removed ⭐
✔ Static Variables
✔ Unclosed Resources
✔ Event Listeners
✔ Growing Cache
✔ Incorrect equals()/hashCode()
```

## 20. Interview-Based Scenarios
- Producer-Consumer
- Dining Philosophers
- Reader-Writer Problem
- Singleton Thread Safety
- Double-Checked Locking
- Lazy Initialization
- Thread Pool Tuning
---

# High Priority for Interviews ⭐⭐⭐⭐⭐

```text
✔ Process vs Thread
✔ Thread Lifecycle
✔ Runnable vs Thread
✔ Synchronization
✔ wait(), notify(), notifyAll()
✔ synchronized
✔ volatile
✔ Atomic Classes
✔ ExecutorService
✔ Thread Pool
✔ ConcurrentHashMap
✔ BlockingQueue
✔ Deadlock
✔ CompletableFuture
✔ Thread Safety
✔ ThreadLocal
```

> **Interview Tip:** If you're targeting **4–6 years of Java/Spring Boot experience**, focus first on synchronization, `volatile`, `ExecutorService`, thread pools, concurrent collections, `CompletableFuture`, and deadlocks. These topics are asked most frequently in backend interviews.