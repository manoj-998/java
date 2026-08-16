package structuralDesginPattern.composite.solved;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite Design Pattern Example
 *
 * Components:
 * 1. Component -> Employee
 * 2. Leaf -> Developer
 * 3. Composite -> Manager
 * 4. Client -> main()
 */
public class CompositeExample {

    /**
     * COMPONENT
     * Common abstraction for both Leaf and Composite.
     */
    abstract static class Employee {
        protected String name;

        Employee(String name) {
            this.name = name;
        }

        /**
         * Common operation supported by both
         * Developer and Manager.
         */
        abstract void showDetails();
    }

    /**
     * LEAF
     * Individual object that does not have children.
     */
    static class Developer extends Employee {

        Developer(String name) {
            super(name);
        }

        @Override
        void showDetails() {
            System.out.println("Developer: " + name);
        }
    }

    /**
     * COMPOSITE
     * Can contain Leaf objects as well as
     * other Composite objects.
     */
    static class Manager extends Employee {

        private final List<Employee> employees = new ArrayList<>();

        Manager(String name) {
            super(name);
        }

        void add(Employee employee) {
            employees.add(employee);
        }

        void remove(Employee employee) {
            employees.remove(employee);
        }

        @Override
        void showDetails() {
            System.out.println("Manager: " + name);

            for (Employee employee : employees) {
                employee.showDetails();
            }
        }
    }

    /**
     * CLIENT
     * Treats Developer and Manager using
     * the same Employee abstraction.
     */
    public static void main(String[] args) {

        Developer dev1 = new Developer("John");
        Developer dev2 = new Developer("David");
        Developer dev3 = new Developer("Alex");

        Manager techLead = new Manager("Robert");
        techLead.add(dev1);
        techLead.add(dev2);

        Manager engineeringManager = new Manager("Michael");
        engineeringManager.add(techLead);
        engineeringManager.add(dev3);

        engineeringManager.showDetails();
    }
}