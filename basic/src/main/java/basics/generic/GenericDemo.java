package basics.generic;


public class GenericDemo {

    public static void main(String[] args) {

        Employee emp = new Employee(101, "Manoj", 75000);

        ReportGenerator<Employee, Integer> report =
                new ReportGenerator<>(emp, emp.getId());

        report.printReport();
    }

    /**
     * Generic class with multiple type parameters.
     *
     * @param <T> must be a Person or child of Person
     * @param <ID> type of the identifier
     */


    static class ReportGenerator<T extends Person, ID> {

        private T person;
        private ID id;

        public ReportGenerator(T person, ID id) {
            this.person = person;
            this.id = id;
        }

        public void printReport() {
            System.out.println("ID      : " + id);
            System.out.println("Name    : " + person.getName());
            System.out.println("Details : " + person);
        }
    }

    /**
     * Parent class
     */
    static class Person {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Child class
     */
    static class Employee extends Person {

        private int id;
        private double salary;

        public Employee(int id, String name, double salary) {
            super(name);
            this.id = id;
            this.salary = salary;
        }
        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Employee{id=" + id + ", name=" + getName() + ", salary=" + salary + "}";
        }
    }
}