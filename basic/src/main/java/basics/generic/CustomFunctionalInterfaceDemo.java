package basics.generic;

public class CustomFunctionalInterfaceDemo {

    // Custom Function (1 input -> 1 output)
    @FunctionalInterface
    interface MyFunction<T, R> {
        R apply(T input);
    }

    // Custom BiFunction (2 inputs -> 1 output)
    @FunctionalInterface
    interface MyBiFunction<T, U, R> {
        R apply(T t, U u);
    }

    // Custom Consumer (1 input -> no output)
    @FunctionalInterface
    interface MyConsumer<T> {
        void accept(T input);
    }

    // Custom Supplier (no input -> output)
    @FunctionalInterface
    interface MySupplier<T> {
        T get();
    }

    // Custom methods
    public static Integer getLength(String s) {
        return s.length();
    }

    public static Integer add(Integer a, Integer b) {
        return a + b;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static String message() {
        return "Hello Java";
    }

    public static void main(String[] args) {

        MyFunction<String, Integer> function = CustomFunctionalInterfaceDemo::getLength;
        System.out.println(function.apply("Hello"));

        MyBiFunction<Integer, Integer, Integer> biFunction = CustomFunctionalInterfaceDemo::add;
        System.out.println(biFunction.apply(10, 20));

        MyConsumer<String> consumer = CustomFunctionalInterfaceDemo::print;
        consumer.accept("Hello Consumer");

        MySupplier<String> supplier = CustomFunctionalInterfaceDemo::message;
        System.out.println(supplier.get());
    }
}