package basics.feature;

import java.util.function.Function;

public class FunctionDemo {

    public static void main(String[] args) {

        // ==========================================
        // Function 1
        // String -> Integer
        // ==========================================
        Function<String, Integer> lengthFunction = str -> str.length();
        System.out.println("Length : " + lengthFunction.apply("Java"));


        // ==========================================
        // Function 2
        // Integer -> String
        // ==========================================
        Function<Integer, String> messageFunction = number -> "Length is : " + number;
        System.out.println(messageFunction.apply(10));
        System.out.println();
        /*
        // Function Chaining (andThen)
        - `andThen()` executes the **current function first**, then passes its result to the next function.
                - Execution order is **Left → Right**.
         */
        Function<String, Integer> length = str -> str.length();
        Function<Integer, String> message = len -> "Length = " + len;
        Function<String, String> result = length.andThen(message);
        System.out.println("changing "+ result.apply("Spring"));

        /*
        Function Composition (compose)
        - `compose()` executes the **specified function first**, then executes the current function.
        - Execution order is **Right → Left**.
        */
        Function<String, String> upperCase = str -> str.toUpperCase();
        Function<String, Integer> composeFunction = lengthFunction.compose(upperCase);
        System.out.println("Compose Result : " + composeFunction.apply("java"));


        // ==========================================
        // Identity Function
        // ==========================================
        Function<String, String> identity = Function.identity();
        System.out.println("Identity : " + identity.apply("Manoj"));

        // ==========================================
        // Custom Object
        // ==========================================
        Employee employee = new Employee(101, "Manoj", 80000);
        Function<Employee, String> employeeName = emp -> emp.getName();
        System.out.println("Employee Name : " + employeeName.apply(employee));

        // Method returns String
        String name = execute(employee, emp->emp.getName());
        // Method returns Double
        Double salary = execute(employee, Employee::getSalary);

        System.out.println("Name   : " + name);
        System.out.println("Salary : " + salary);
    }

    /**
     * Generic method that accepts a Function
     * and returns the transformed value.
     * T -> Input Type
     * R -> Return Type
     */
    public static <T, R> R execute(
            T input,
            Function<T, R> function) {

        return function.apply(input);
    }

    /**
     * Inner Class
     */
    static class Employee {
        private int id;
        private String name;
        private double salary;
        public Employee(int id, String name, double salary) {
            this.id = id;
            this.name = name;
            this.salary = salary;
        }

        public String getName() {
            return name;
        }

        public double getSalary() {
            return salary;
        }
    }

}