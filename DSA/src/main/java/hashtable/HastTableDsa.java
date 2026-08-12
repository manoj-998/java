package hashtable;

import java.util.ArrayList;

public class HastTableDsa {
    //Space that we allocation for hash table
    private int size = 7;
    private Node[] dataMap;

    public void printAllHashTable() {
        for (int i = 0; i < dataMap.length; i++) {
            System.out.print(i + ": ");
            Node temp = dataMap[i];
            while (temp != null) {
                System.out.print(" {" + temp.key + "=" + temp.value + "}");
                temp = temp.next;
            }
            System.out.println();
        }
    }

    static class Node {
        String key;
        int value;
        Node next;

        Node(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * create a hash table of size
     */
    public HastTableDsa() {
        dataMap = new Node[size];
    }

    /**
     * Generates a hash code for a given key.
     * This method uses a simple hash function that combines the ASCII values of the characters in the key,
     * multiplied by a prime number (23), to ensure a more uniform distribution of hash codes.
     * The use of a prime number helps in reducing the number of collisions.
     * Finally, it takes the modulus of the calculated hash with the size of the hash table to ensure
     * the hash code fits within the bounds of the hash table.
     * (11%2)=1  (102938%3)=2 So every time the hash index will be within size
     *
     * @param key The key to be hashed.
     * @return The hash code for the given key.
     */
    private int hash(String key) {
        int hash = 0;
        char[] charSet = key.toCharArray();
        for (int i = 0; i < charSet.length; i++) {
            int asicValue = charSet[i];
            hash = (hash + asicValue * 23) % size;
        }
        return hash;
    }

    /**
     * Best Case: O(1) Average Case: O(1) Worst Case: O(n)
     * To set key value in hash table
     */
    public void set(String key, int value) {
        Node newNode = new Node(key, value);
        int hashCode = hash(key);

        if (dataMap[hashCode] == null) {
            dataMap[hashCode] = newNode;
        } else {
            Node temp = dataMap[hashCode];
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    /**
     * Best Case: O(1) Average Case: O(1) Worst Case: O(n)
     * To get value from hash table for a key
     */
    public int get(String key) {
        int hashCode = hash(key);
        if (dataMap[hashCode] == null) {
            return 0;
        }
        Node temp = dataMap[hashCode];
        while (temp != null) {
            if (temp.key == key) return temp.value;
            temp = temp.next;
        }
        return 0;
    }

    /**
     * Best Case: O(1) Average Case: O(1) Worst Case: O(n)
     * To get all keys from hash table
     */
    public ArrayList keys() {
        ArrayList<String> allKeys = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Node temp = dataMap[i];
            while (temp != null) {
                allKeys.add(temp.key);
                temp = temp.next;
            }
        }
        return allKeys;
    }
}
