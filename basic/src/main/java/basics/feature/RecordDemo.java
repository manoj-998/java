package basics.feature;

public class RecordDemo {

    // Record is an immutable data carrier.
    // Java automatically generates:
    // - Constructor
    // - Accessor methods (id(), name())
    // - equals()
    // - hashCode()
    // - toString()
    // Record inside the class
    record Employee(int id, String name) {

        // Compact constructor used for validation.
        // Java automatically assigns:
        // this.id = id;
        // this.name = name;
        public Employee {
            if (id <= 0) {
                throw new IllegalArgumentException("Invalid Id");
            }
        }

        // Custom business method
        public String details() {
            return id + " - " + name;
        }
    }

    public static void main(String[] args) {

        Employee emp = new Employee(101, "Rahul");

        // Access record components
        System.out.println(emp.id());
        System.out.println(emp.name());

        // Custom method
        System.out.println(emp.details());

        // toString()
        System.out.println(emp);

        // equals()
        Employee emp2 = new Employee(101, "Rahul");
        System.out.println(emp.equals(emp2));

        // hashCode()
        System.out.println(emp.hashCode());
    }
}