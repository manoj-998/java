package creational.builder;

class Nested {

    public static void main(String[] args) {

        Employee employee = new Employee.Builder()
                .id(101)
                .name("Manoj")
                .email("manoj@gmail.com")
                .address(
                        new Address.Builder()
                                .city("Bangalore")
                                .state("Karnataka")
                                .pincode("560001")
                                .build()
                )
                .build();

        System.out.println(employee);
    }
}
class Employee {

    private int id;
    private String name;
    private String email;
    private Address address;

    private Employee(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.email = builder.email;
        this.address = builder.address;
    }

    /*
     Stack
    ------
    builder ------------------+

    Heap
    --------------------------
    Builder Object
    --------------------------
    id = 0
    name = null
    email = null
    address = null
    --------------------------
     * */
    static class Builder {

        private int id;
        private String name;
        private String email;
        private Address address;

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        /*
        - `this` refers to the current Builder object.
        - `this.address = address` assigns the method parameter to the Builder's field.
        - `return this` returns the same Builder object.
        - Returning `this` enables method chaining (Fluent API).
        - No new Builder object is created; the same object is reused until `build()` is called.
        * */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder address(Address address) {
            this.address = address;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }
    /**toString() is not part of the Builder Pattern.
    * It's only used to make objects print in a readable format.*/
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                '}';
    }
}
class Address {

    private String city;
    private String state;
    private String pincode;

    private Address(Builder builder) {
        this.city = builder.city;
        this.state = builder.state;
        this.pincode = builder.pincode;
    }

    static class Builder {

        private String city;
        private String state;
        private String pincode;

        public Builder city(String city) {
            this.city = city;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder pincode(String pincode) {
            this.pincode = pincode;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", pincode='" + pincode + '\'' +
                '}';
    }
}

