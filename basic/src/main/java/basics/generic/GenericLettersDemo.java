package basics.generic;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericLettersDemo {

    public static void main(String[] args) {

        // ==================================================
        // T -> Type
        // ==================================================
        Box<String> box = new Box<>();
        box.set("Java");
        System.out.println("T (Type) : " + box.get());


        // ==================================================
        // E -> Element
        // ==================================================
        SimpleList<String> list = new SimpleList<>();

        list.add("Spring Boot");
        list.add("Kafka");

        list.print();


        // ==================================================
        // K & V -> Key and Value
        // ==================================================
        KeyValue<Integer, String> map = new KeyValue<>(101, "Manoj");

        map.print();


        // ==================================================
        // N -> Number
        // ==================================================
        Calculator<Integer> calculator = new Calculator<>();

        System.out.println("N (Number) : " + calculator.square(10));


        // ==================================================
        // R -> Return Type
        // ==================================================
        Function<String, Integer> lengthFunction = str -> str.length();

        Integer length = lengthFunction.apply("Java");

        System.out.println("R (Return Type) : " + length);

        // ==================================================
        // U -> Second Generic Type
        // ==================================================
        Pair<String, Integer> pair = new Pair<>("Age", 25);
        pair.print();


        // ==================================================
        // S -> Third Generic Type
        // ==================================================
        Triple<Integer, String, Double> triple = new Triple<>(101, "Manoj", 95000.50);
        triple.print();

        // Double -> Integer
        Integer rounded = convert(99.99, value -> value.intValue());
        System.out.println("Rounded : " + rounded);
    }

    /**
     * Generic method with return type.
     * T -> Input Type
     * R -> Return Type
     */
    public static <T, R> R convert(T input, Function<T, R> function) {
        return function.apply(input);
    }

    // ==================================================
    // T -> Type
    // ==================================================
    //Box<String> box = new Box<>();
    static class Box<T> {
        private T value;
        public void set(T value) {
            this.value = value;
        }
        public T get() {
            return value;
        }
    }


    // ==================================================
    // E -> Element
    // ==================================================
    // SimpleList<String> list = new SimpleList<>();

    static class SimpleList<E> {
        private List<E> elements = new ArrayList<>();
        public void add(E element) {
            elements.add(element);
        }

        public void print() {
            System.out.println("E (Element) : " + elements);
        }
    }


    // ==================================================
    // K & V -> Key and Value
    // ==================================================
    // KeyValue<Integer, String> map = new KeyValue<>(101, "Manoj");

    static class KeyValue<K, V> {

        private K key;
        private V value;

        public KeyValue(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public void print() {
            System.out.println("K = " + key + ", V = " + value);
        }
    }


    // ==================================================
    // N -> Number
    // ==================================================
    //Calculator<Integer> calculator = new Calculator<>();

    static class Calculator<N extends Number> {

        public double square(N number) {

            return number.doubleValue() * number.doubleValue();
        }
    }


    // ==================================================
    // U -> Second Generic Type
    // ==================================================
    //    Pair<String, Integer> pair = new Pair<>("Age", 25);
    static class Pair<T, U> {

        private T first;
        private U second;

        public Pair(T first, U second) {

            this.first = first;
            this.second = second;
        }

        public void print() {

            System.out.println("T = " + first +
                    ", U = " + second);
        }
    }

    // ==================================================
    // S -> Third Generic Type
    // ==================================================
    // Triple<Integer, String, Double> triple =
    //                new Triple<>(101, "Manoj", 95000.50);
    static class Triple<T, U, S> {

        private T first;
        private U second;
        private S third;

        public Triple(T first, U second, S third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public void print() {
            System.out.println(
                    "T = " + first +
                            ", U = " + second +
                            ", S = " + third);
        }
    }



}