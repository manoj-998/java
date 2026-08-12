package basics.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericDemo2 {

    /*
    Wildcards
    ?           → Any Type
    ? extends   → Read (Producer)
    ? super     → Write (Consumer)

    # Producer vs Consumer
    | Producer | Consumer |
    |-----------|----------|
    | `? extends T` | `? super T` |
    | Read data | Write data |
    | Cannot add | Can add |
    | Safe to read | Safe to write |

    | Letter | Meaning | Example |
    |--------|---------|---------|
    | `T` | **Type** | `class Box<T>` |
    | `E` | **Element** | `List<E>`, `Set<E>` |
    | `K` | **Key** | `Map<K, V>` |
    | `V` | **Value** | `Map<K, V>` |
    | `N` | **Number** | `class Calculator<N extends Number>` |
    | `R` | **Return Type / Result** | `Function<T, R>` |
    | `U` | **Second Type** | `class Pair<T, U>` |
    | `S` | **Third Type** | `class Triple<T, U, S>` |
 */
    public static void main(String[] args) {

        // ==========================
        // Producer Example
        // ==========================

        List<Integer> integers = Arrays.asList(10, 20, 30);
        List<Double> doubles = Arrays.asList(10.5, 20.5, 30.5);
        System.out.println("Producer Example");

        printNumbers(integers);

        printNumbers(doubles);


        // ==========================
        // Consumer Example
        // ==========================

        List<Number> numbers = new ArrayList<>();

        System.out.println("\nConsumer Example");

        addNumbers(numbers);

        System.out.println(numbers);

    }


    /**
     * Producer
     *
     * Reads data from the list.
     */
    public static void printNumbers(List<? extends Number> list) {

        for (Number number : list) {
            System.out.println(number);
        }

        // list.add(100); ❌ Compile Error
    }


    /**
     * Consumer
     *
     * Adds Integer values into the list.
     */
    public static void addNumbers(List<? super Integer> list) {

        list.add(100);
        list.add(200);
        list.add(300);

        // Integer value = list.get(0); ❌ Compile Error

        Object value = list.get(0);

        System.out.println("First Value : " + value);
    }

}