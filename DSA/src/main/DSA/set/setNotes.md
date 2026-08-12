Sets are similar to HashMaps except that instead of having key/value pairs they only have the keys but not the values.

Sets store a collection of unique elements (in other words, they do not allow duplicates).

They are part of the Java Collections Framework and are useful for various operations such as finding distinct elements in a collection and performing set operations like union and intersection.

To create a set in Java, you can use the HashSet, TreeSet, or LinkedHashSet classes. For example:



javaCopy code
// Import required classes
import java.util.HashSet;
import java.util.Set;

// Create a set using HashSet
Set<Integer> mySet = new HashSet<>();

// Add elements to the set
mySet.add(1);
mySet.add(2);
mySet.add(3);
mySet.add(4);
mySet.add(5);


Once a set is defined, you can perform various operations on it, such as adding or removing elements, finding the union, intersection, or difference of two sets, and checking if a given element is a member of a set.

Here are some examples of common set operations in Java:



// Import required classes
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Add an element to a set
// If the number 6 is already in the set, it will not be added again.
mySet.add(6);

// Removing an element from a set
mySet.remove(3);

// Union of two sets
Set<Integer> otherSet = new HashSet<>();
otherSet.add(3);
otherSet.add(4);
otherSet.add(5);
otherSet.add(6);

Set<Integer> unionSet = new HashSet<>(mySet);
unionSet.addAll(otherSet);

// Intersection of two sets
Set<Integer> intersectionSet = new HashSet<>(mySet);
intersectionSet.retainAll(otherSet);

// Difference between two sets
Set<Integer> differenceSet = new HashSet<>(mySet);
differenceSet.removeAll(otherSet);

// Checking if an element is in a set
if (mySet.contains("hello")) {
System.out.println("Found hello in mySet");
}


You can learn more about Java sets by referring to the official Java documentation by clicking here.

Now let's look at some common coding interview questions that use sets!