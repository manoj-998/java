# Memento Design Pattern

## Definition
The **Memento Design Pattern** is a **Behavioral Design Pattern** used to **save and restore the previous state of an object without exposing its internal details**.

In simple words, Memento works like **Save / Undo**. Before changing an object, we save its current state. Later, if needed, we can restore that old state.

memento should be a sealed calls it won't show what is stored for other to depend on it.
> **Memento = Save state now, restore it later.**

---

# Why Memento?
Suppose we have a text editor:

```text
Text = "Hello"
```

Then user changes it:

```text
Text = "Hello World"
```

If the user clicks:

```text
Undo
```

we need to restore:

```text
Hello
```

Instead of exposing all internal fields of `TextEditor`, Memento stores a snapshot of the state.

---

# Problem Without Memento

```java
class TextEditor {
    String text;
}
```

Client may directly store old values:

```java
String oldText = editor.text;
editor.text = "Hello World";
editor.text = oldText;
```

Problems:

- Internal state is exposed.
- Client becomes responsible for backup logic.
- Difficult when object has many fields.
- Undo/history logic gets mixed with business logic.
- Breaks encapsulation.

---

# Solution
Create a separate **Memento object** that stores the previous state.

```text
Originator
   |
   | createMemento()
   v
Memento
   |
   | stored by
   v
Caretaker
```

To restore:

```text
Caretaker
   |
   v
Memento
   |
   v
Originator.restore()
```

---

# Main Components

## 1. Originator
The object whose state needs to be saved and restored.

Example:

```text
TextEditor
```

Responsibilities:

- Maintain current state
- Create Memento
- Restore state from Memento

---

## 2. Memento
Stores a snapshot of the Originator's state.

Example:

```text
TextEditorMemento
```

The Memento should not expose internal state unnecessarily.

---

## 3. Caretaker
Stores Memento objects.

Example:

```text
History
```

Caretaker does not modify the saved state.

It only:

```text
Save Memento
Get Memento
```

---

# Structure

```text
Client
  |
  v
Originator
  |
  | createMemento()
  v
Memento
  |
  v
Caretaker
```

Restore flow:

```text
Caretaker
   |
   v
Memento
   |
   v
Originator.restore()
```

---

# Simple Java Example

```java
import java.util.Stack;

public class MementoExample {

    /**
     * MEMENTO
     * Stores a snapshot of TextEditor state.
     */
    static class TextMemento {
        private final String text;

        TextMemento(String text) {
            this.text = text;
        }

        String getText() {
            return text;
        }
    }

    /**
     * ORIGINATOR
     * Object whose state is saved/restored.
     */
    static class TextEditor {
        private String text = "";

        void setText(String text) {
            this.text = text;
        }

        void show() {
            System.out.println("Text: " + text);
        }

        /**
         * Creates snapshot of current state.
         */
        TextMemento save() {
            return new TextMemento(text);
        }

        /**
         * Restores old state.
         */
        void restore(TextMemento memento) {
            this.text = memento.getText();
        }
    }

    /**
     * CARETAKER
     * Stores history of Mementos.
     */
    static class History {
        private final Stack<TextMemento> history = new Stack<>();

        void save(TextMemento memento) {
            history.push(memento);
        }

        TextMemento undo() {
            return history.pop();
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        History history = new History();

        editor.setText("Hello");
        history.save(editor.save());

        editor.setText("Hello World");
        editor.show();

        editor.restore(history.undo());
        editor.show();
    }
}
```

Output:

```text
Text: Hello World
Text: Hello
```

---

# Internal Flow

First:

```java
editor.setText("Hello");
```

State:

```text
Hello
```

Then:

```java
history.save(editor.save());
```

Flow:

```text
TextEditor
   |
   | save()
   v
TextMemento("Hello")
   |
   v
History Stack
```

Then:

```java
editor.setText("Hello World");
```

Current state becomes:

```text
Hello World
```

When:

```java
editor.restore(history.undo());
```

runs:

```text
History
   |
   | pop()
   v
Memento("Hello")
   |
   v
TextEditor.restore()
   |
   v
Hello
```

---

# Memento With Multiple Undo
Suppose:

```text
State 1 = A
State 2 = AB
State 3 = ABC
```

Save before every change:

```java
history.save(editor.save());
```

History:

```text
TOP
 |
 v
AB
A
```

Undo:

```text
ABC → AB
```

Undo again:

```text
AB → A
```

This is why Memento is commonly used for:

```text
Undo
History
Rollback
Snapshots
```

---

# Real-World Software Examples

## Text Editor
```text
Write
Save State
Write More
Undo
```

## IDE
```text
Code State
Undo
Redo
```

## Game
Save player state:

```text
Level
Health
Score
Position
```

Then restore from checkpoint.

## Form Editing
Before updating a form:

```text
Save old values
Change form
Cancel
Restore old values
```

## Workflow State
Save process state before moving to next step.

---

# Advantages

- Supports undo/rollback.
- Preserves encapsulation.
- Keeps backup logic outside Originator.
- Easy to maintain history.
- Can restore complete object state.
- Useful for snapshots/checkpoints.

---

# Disadvantages

- Can consume a lot of memory.
- Large objects create large Mementos.
- Saving state frequently may be expensive.
- Caretaker can grow indefinitely.
- Deep object graphs can be difficult to copy.

---

# When to Use
Use Memento when:

- Undo is required.
- Object state needs rollback.
- Snapshots are needed.
- Checkpoints are required.
- Internal state should remain hidden.
- Previous versions of an object need to be restored.

Examples:

```text
Text Editor
IDE Undo
Game Save
Form Cancel
Workflow Rollback
Configuration History
```

---

# When Not to Use
Avoid Memento when:

- Object state is very large.
- History is not required.
- Saving snapshots is too expensive.
- State can easily be recomputed.
- Memory usage is more important than rollback capability.

---

# Design Considerations
Keep responsibilities separate:

```text
Originator → Creates/restores state
Memento    → Stores state
Caretaker  → Stores history
```

Do not put restore/history management completely inside the client.

Prefer:

```text
Client
  |
  v
History
  |
  v
Memento
```

---

# Memento and Encapsulation
One important goal of Memento is:

> **Save state without exposing internal fields to the client.**

Bad:

```java
String oldText = editor.text;
```

Better:

```java
TextMemento memento = editor.save();
```

The client does not need to know how `TextEditor` stores its state.

---

# Memento vs Command
Both are commonly used for undo.

```text
Command  → Stores the action
Memento  → Stores the state
```

Example:

```text
Command:
"Delete text"

Memento:
"Text before deletion"
```

Easy trick:

```text
Command = What happened?
Memento = What was the state?
```

They are often used together.

---

# Memento vs Prototype

```text
Memento   → Save object state for restore
Prototype → Create a new object by cloning
```

Memento is mainly for:

```text
Undo / History
```

Prototype is mainly for:

```text
Object Creation
```

---

# Memento vs State Pattern

```text
Memento → Stores previous state
State   → Changes object behavior based on current state
```

Example:

```text
Memento:
Restore previous document content

State:
Order behaves differently in CREATED/SHIPPED state
```

---

# Pitfalls

- Do not expose too much internal Memento data.
- Avoid unlimited history.
- Be careful with large object snapshots.
- Consider immutable Mementos.
- Deep-copy mutable fields when required.
- Avoid storing references that may later change.

---

# Interview Questions

## What is Memento Pattern?
Memento is a Behavioral Design Pattern that captures and stores an object's state so it can be restored later without exposing internal implementation.

## Which category does Memento belong to?
**Behavioral Design Pattern**

## What are the main components?
- Originator
- Memento
- Caretaker

## What is Originator?
The object whose state is saved and restored.

Example:

```text
TextEditor
```

## What is Memento?
The object containing the saved snapshot.

Example:

```text
TextMemento
```

## What is Caretaker?
The object that stores Mementos.

Example:

```text
History
```

## Does Caretaker modify Memento?
Normally, no.

Caretaker should only store and retrieve it.

## Why is Memento useful?
Mainly for:

```text
Undo
Rollback
Snapshots
History
```

## Memento vs Command?

```text
Memento → Saves state
Command → Saves action
```

## Can Memento use a Stack?
Yes.

A stack is commonly used for Undo history:

```text
push() → Save
pop()  → Undo
```

---

# Key Points

- Category: **Behavioral Design Pattern**
- Saves object state.
- Restores previous state.
- Supports undo/rollback.
- Originator creates Memento.
- Memento stores snapshot.
- Caretaker stores history.
- Preserves encapsulation.
- Stack is commonly used for undo.
- Memento should preferably be immutable.
- Main drawback is memory usage.

---

# Easy Trick to Remember
Think about a **Save Game**.

```text
Player
  |
  | Save
  v
Checkpoint
  |
  | Restore
  v
Player
```

Or a text editor:

```text
Hello
  |
 Save
  |
Hello World
  |
 Undo
  |
Hello
```

> **Memento = Take a snapshot now so you can go back later.**

Easy memory:

```text
Originator = Object
Memento    = Snapshot
Caretaker  = History
```

---

# Summary

| Aspect | Description |
|---|---|
| Pattern Type | Behavioral |
| Purpose | Save and restore object state |
| Originator | Creates/restores snapshot |
| Memento | Stores state |
| Caretaker | Stores Mementos |
| Common Data Structure | Stack |
| Main Use | Undo / Rollback |
| Main Benefit | Preserves encapsulation |
| Main Risk | High memory usage |
| Easy Trick | Memento = Snapshot |