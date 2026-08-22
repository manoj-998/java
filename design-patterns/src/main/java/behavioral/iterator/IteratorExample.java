package behavioral.iterator;

import java.util.*;
public class IteratorExample {

    /**
     * Simple Employee class.
     */
    static class Employee {
        private final int id;
        private final String name;

        Employee(int id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public String toString() {
            return id + " - " + name;
        }
    }

    /**
     * ITERATOR
     * Defines methods for traversing employees.
     */
    interface Iterator {
        boolean hasNext();
        Employee next();
    }

    /**
     * CONCRETE ITERATOR
     * Handles traversal logic.
     */
    static class EmployeeIterator implements Iterator {
        private final Employee[] employees;
        private int position = 0;

        EmployeeIterator(Employee[] employees) {
            this.employees = employees;
        }

        /**
         * Checks whether another employee exists.
         */
        @Override
        public boolean hasNext() {
            return position < employees.length;
        }

        /**
         * Returns current employee and moves to next position.
         * first return and then increment the next value
         */
        @Override
        public Employee next() {
            return employees[position++];
        }
    }

    /**
     * AGGREGATE / COLLECTION
     * Stores employees and provides an Iterator.
     */
    static class EmployeeCollection {
        private final Employee[] employees = {
                new Employee(1, "John"),
                new Employee(2, "David"),
                new Employee(3, "Alex")
        };

        Iterator createIterator() {
            return new EmployeeIterator(employees);
        }
    }

    /**
     * CLIENT
     */
    public static void main(String[] args) {
        EmployeeCollection collection = new EmployeeCollection();

        // Creates Iterator with position = 0.
        Iterator iterator = collection.createIterator();

        // Traverse employees one by one.
        while (iterator.hasNext()) {
            Employee employee = iterator.next();
            System.out.println(employee);
        }
    }
}