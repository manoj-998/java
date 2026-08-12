package set;

import java.util.*;

public class SetMain {
    public static void main(String[] args) {
        System.out.println("------------------------------");
        System.out.println("First Non-Repeating Character");
        List<Integer> myList = List.of(1, 2, 3, 4, 1, 2, 5, 6, 7, 3, 4, 8, 9, 5);
        List<Integer> newList = removeDuplicates(myList);
        System.out.println(newList);

        System.out.println("------------------------------");
        System.out.println("Has Unique Chars");
        System.out.println(hasUniqueChars("hello")); // should return false
        System.out.println(hasUniqueChars("")); // should return true
        System.out.println(hasUniqueChars("0123456789")); // should return true
        System.out.println(hasUniqueChars("abacadaeaf"));

        System.out.println("------------------------------");
        System.out.println(" Find Pairs");
        List<int[]> pairs = findPairs(
                new int[]{1, 2, 3, 4, 5},
                new int[]{2, 4, 6, 8, 10}, 7);
        for (int[] pair : pairs) {
            System.out.println(Arrays.toString(pair));
        }

        System.out.println("------------------------------");
        System.out.println("Set: Longest Consecutive Sequence");
        System.out.println("Consecutive Integers 5");
        System.out.println(longestConsecutiveSequence(new int[]{100, 4, 200, 1, 2,3}));
        System.out.println("No Sequence Expected 1");
        System.out.println(longestConsecutiveSequence(new int[]{1, 3, 5, 7, 9}));
        System.out.println("Duplicates Expected 4");
        System.out.println(longestConsecutiveSequence(new int[]{1, 2, 2, 3, 4}));
        System.out.println("Negative Numbers Expected 5");
        System.out.println(longestConsecutiveSequence(new int[]{1, 0, -1, -2, -3}));
        System.out.println("Empty Array  Expected 0");
        System.out.println(longestConsecutiveSequence(new int[]{}));
        System.out.println("Multiple Sequences  Expected 4");
        System.out.println(longestConsecutiveSequence(new int[]{1, 2, 3, 10, 11, 12, 13}));
        System.out.println("Unordered Elements  Expected 5");
        System.out.println(longestConsecutiveSequence(new int[]{5, 1, 3, 4, 2}));
        System.out.println("Single Element  Expected 1");
        System.out.println(longestConsecutiveSequence(new int[]{1}));
        System.out.println("All Identical  Expected 1");
        System.out.println(longestConsecutiveSequence(new int[]{2, 2, 2, 2, 2}));

    }

    /**
     * O(n) + O(n) + O(n) = O(n), where n is the number of elements in the input array nums.
     * This accounts for the initial insertion into the HashSet, iterating over the set, and the while loop checks for consecutive sequences.
     * https://www.youtube.com/watch?v=P6RZZMu_maU
     */
    public static int longestConsecutiveSequence(int[] nums) {
        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }
        int longestStreak = 0;
        for (int num : numSet) {
            if (!numSet.contains(num - 1)) {
                int currentNum = num;
                int currentStreak = 1;
                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }
                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }
        return longestStreak;
    }


    /**
     * Big O(N+M)
     */
    public static List<int[]> findPairs(int[] arr1, int[] arr2, int target) {
        Set<Integer> mySet = new HashSet<>();
        List<int[]> pairs = new ArrayList<>();
        for (int num : arr1)
            mySet.add(num);

        for (int num : arr2) {
            int complement = target - num;
            if (mySet.contains(complement)) {
                pairs.add(new int[]{complement, num});
            }
        }

        return pairs;
    }

    /**
     * is O(n)
     */
    public static boolean hasUniqueChars(String string) {
        Set<Character> charSet = new HashSet<>();
        for (char ch : string.toCharArray()) {
            if (charSet.contains(ch)) {
                return false;
            }
            charSet.add(ch);
        }
        return true;
    }

    /**
     * Big O(N)
     */
    public static List<Integer> removeDuplicates(List<Integer> myList) {
        //O(1)
        Set<Integer> uniqueSet = new HashSet<>(myList);
        //O(N)
        return new ArrayList<>(uniqueSet);
    }
}
