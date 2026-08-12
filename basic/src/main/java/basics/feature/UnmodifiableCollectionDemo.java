package basics.feature;

import java.util.ArrayList;
import java.util.List;

public class UnmodifiableCollectionDemo {

    public static void main(String[] args) {

        // ======================================
        // Mutable List
        // ======================================
        List<String> technologies = new ArrayList<>();

        technologies.add("Java");
        technologies.add("Spring");
        technologies.add("Kafka");
        System.out.println("Original List : " + technologies);


        // ======================================
        // Create Unmodifiable List
        // ======================================
        List<String> readOnlyList = List.copyOf(technologies);
        System.out.println("Unmodifiable List : " + readOnlyList);


        // ======================================
        // Modify Original List
        // ======================================
        technologies.add("Docker");
        System.out.println("\nOriginal List After Modification : " + technologies);

        System.out.println("Unmodifiable List : " + readOnlyList);


        // ======================================
        // Try Modifying Unmodifiable List
        // ======================================
        try {
            readOnlyList.add("Redis");
        } catch (UnsupportedOperationException ex) {

            System.out.println("\nException : " + ex);
        }
    }
}