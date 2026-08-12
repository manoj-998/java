package hashtable;

import javax.swing.plaf.IconUIResource;
import java.awt.image.ImageProducer;
import java.util.*;

public class HastTableMain {
    public static void main(String[] args) {
        System.out.println("--------------------------");
        System.out.println("Crete a hash table ");
        HastTableDsa hastTable = new HastTableDsa();
        hastTable.printAllHashTable();

        System.out.println("--------------------------");
        System.out.println("Set value in hash table ");
        hastTable.set("a", 1);
        hastTable.set("b", 2);
        hastTable.set("c", 3);
        hastTable.set("a", 4);
        hastTable.set("g", 5);
        hastTable.set("manoj", 1998);
        hastTable.printAllHashTable();

        System.out.println("--------------------------");
        System.out.println("Get value from hash table on key");
        System.out.println("key value: manoj = " + hastTable.get("manoj"));
        System.out.println("key value: a = " + hastTable.get("a"));

        System.out.println("--------------------------");
        System.out.println("Get All keys from the hash table");
        System.out.println(hastTable.keys());

        System.out.println("------------------------------");
        System.out.println("item In Common");
        int[] arr1 = {1, 2, 3, 4};
        int[] arr2 = {1, 3, 4, 5};
        System.out.println(itemInCommon(arr1, arr2));

        System.out.println("------------------------------");
        System.out.println("Find duplicate value in Array");
        int[] nums = {1, 2, 3, 2, 1, 4, 5, 4};
        List<Integer> duplicates = findDuplicates(nums);
        System.out.println(duplicates);

        System.out.println("------------------------------");
        System.out.println("First Non-Repeating Character");
        System.out.println(firstNonRepeatingChar("leetcode"));
        System.out.println(firstNonRepeatingChar("hello"));
        System.out.println(firstNonRepeatingChar("aabbcc"));

        System.out.println("------------------------------");
        System.out.println(" Group Anagrams");
        System.out.println("1st set:");
        System.out.println(groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        System.out.println("\n2nd set:");
        System.out.println(groupAnagrams(new String[]{"abc", "cba", "bac", "foo", "bar"}));
        System.out.println("\n3rd set:");
        System.out.println(groupAnagrams(new String[]{"listen", "silent", "triangle", "integral", "garden", "ranged"}));

        System.out.println("------------------------------");
        System.out.println("Two Sum");
        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));
        System.out.println(Arrays.toString(twoSum(new int[]{3, 2, 4}, 6)));

        System.out.println("------------------------------");
        System.out.println("Subarray Sum ");

        int[] nums3 = {2, 3, 4, 5, 6};
        int target3 = 3;
        int[] result3 = subarraySum(nums3, target3);
        System.out.println("[" + result3[0] + ", " + result3[1] + "]");

        int[] nums1 = {1, 2, 3, 4, 5};
        int target1 = 9;
        int[] result1 = subarraySum(nums1, target1);
        System.out.println("[" + result1[0] + ", " + result1[1] + "]");

        int[] nums2 = {1, 4, 5, 1, 0};
        int target2 = 11;
        int[] result2 = subarraySum(nums2, target2);
        System.out.println("[" + result2[0] + ", " + result2[1] + "]");

    }

    /**
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int[] subarraySum(int[] nums, int target) {
        Map<Integer, Integer> sumIndex = new HashMap<>();
        sumIndex.put(0, -1);
        int currentSum = 0;
        for (int i = 0; i < nums.length; i++) {
            currentSum += nums[i];
            if (sumIndex.containsKey(currentSum - target)) {
                return new int[]{sumIndex.get(currentSum - target) + 1, i};
            }
            sumIndex.put(currentSum, i);
        }
        return new int[]{};
    }

    /**
     * Time Complexity: O(n)
     * Space Complexity: O(n)
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            int complement = target - num;

            if (numMap.containsKey(complement)) {
                return new int[]{numMap.get(complement), i};
            }
            numMap.put(num, i);
        }

        return new int[]{};
    }


    /**
     * Big O(O(n * k log k) because we are using Array.sort(quick sort)
     * <p>anagramGroups :
     * if the input array is ["eat", "tea", "tan", "ate", "nat", "bat"], the method should return [["eat","tea","ate"],["tan","nat"],["bat"]]</p>
     */
    public static List<List<String>> groupAnagrams(String[] strings) {
        Map<String, List<String>> anagramGroups = new HashMap<>();

        for (String string : strings) {
            char[] chars = string.toCharArray();
            Arrays.sort(chars);
            String canonical = new String(chars);

            if (anagramGroups.containsKey(canonical)) {
                anagramGroups.get(canonical).add(string);
            } else {
                List<String> group = new ArrayList<>();
                group.add(string);
                anagramGroups.put(canonical, group);
            }
        }
        return new ArrayList<>(anagramGroups.values());
    }


    /**
     * This is O(n) + O(n) = O(2n)
     * By removing constant its O(N)
     */
    public static Character firstNonRepeatingChar(String value) {
        char[] chars = value.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        for (int i = 0; i < chars.length; i++) {
            map.put(chars[i], map.getOrDefault(chars[i], 0) + 1);
        }
        for (int i = 0; i < chars.length; i++) {
            if (map.get(chars[i]) == 1) {
                return chars[i];
            }
        }
        return null;
    }

    /**
     * Find duplication value in the and return
     * this is big O(N)
     */
    public static List<Integer> findDuplicates(int[] nums) {
        List<Integer> duplicates = new ArrayList<>();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (int num : nums) {
            int count = countMap.getOrDefault(num, 0);
            if (count == 1) {
                duplicates.add(num);
            }
            countMap.put(num, count + 1);
        }
        return duplicates;
    }


    /**
     * Big O(n+m)
     * To find common b/w two list of array
     */
    public static boolean itemInCommon(int[] firstArray, int[] secondArray) {
        Map<Integer, Boolean> map = new HashMap<>();
        for (int i = 0; i < firstArray.length; i++)
            map.put(firstArray[i], true);

        for (int i = 0; i < firstArray.length; i++) {
            if (Boolean.TRUE.equals(map.get(secondArray[i])))
                return true;
        }
        return false;
    }


}
