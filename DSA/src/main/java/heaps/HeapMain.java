package heaps;

import java.util.ArrayList;
import java.util.List;

public class HeapMain {

    public static void main(String[] args) {
        System.out.println("------------------------------");
        System.out.println("Create heap with value");
        HeapDsa heap = new HeapDsa(10);
        heap.printHeap();

        System.out.println("------------------------------");
        System.out.println("insert into heap");
        heap.insert(20);
        heap.printHeap();
        heap.insert(25);
        heap.printHeap();
        heap.insert(1);
        heap.printHeap();
        heap.insert(10);
        heap.printHeap();
        heap.insert(100);
        heap.printHeap();

        System.out.println("------------------------------");
        System.out.println("Remove root node from heap");
        heap.remove();
        heap.printHeap();

        System.out.println("------------------------------");
        System.out.println("Heap: Kth Smallest Element in an Array");
        int[] nums1 = {7, 10, 4, 3, 20, 15};
        int k1 = 3;
        System.out.println("Expected output: 7");
        System.out.println("Actual output: " + findKthSmallest(nums1, k1));
        System.out.println();
        int[] nums2 = {2, 1, 3, 5, 6, 4};
        int k2 = 2;
        System.out.println("Expected output: 2");
        System.out.println("Actual output: " + findKthSmallest(nums2, k2));


        System.out.println("------------------------------");
        System.out.println("Heap: Maximum Element in a Stream");
        int[] nums11 = {1, 5, 2, 9, 3, 6, 8};
        System.out.println("Expected output: [1, 5, 5, 9, 9, 9, 9]");
        System.out.println("Actual output: " + streamMax(nums11));
        System.out.println();
        int[] nums3 = {3, 3, 3, 3, 3};
        System.out.println("Expected output: [3, 3, 3, 3, 3]");
        System.out.println("Actual output: " + streamMax(nums3));

    }

    /**
     * O(n log k),
     * where n is the number of elements in the input array nums,
     * and k is the size limit of the heap. This accounts for inserting each of the n elements into the heap
     * (which is kept at a maximum size of k) and the cost of removal operations to maintain the heap size.
     */
    public static int findKthSmallest(int[] nums, int k) {
        HeapDsa maxHeap = new HeapDsa();

        for (int num : nums) {
            maxHeap.insert(num);
            if (maxHeap.getHeap().size() > k) {
                maxHeap.remove();
            }
        }

        return maxHeap.remove();
    }

    /**
     * O(n log k)
     * Considering these steps, the dominant factor in the time complexity is the insert operation inside the loop. Since the insert operation is called n times and each insert operation can take up to O(log n) time, the overall time complexity of the streamMax method is O(n log n). This accounts for inserting each of the n elements into the heap and the cost of maintaining the heap structure.
     */
    public static List<Integer> streamMax(int[] nums) {
        HeapDsa maxHeap = new HeapDsa();
        List<Integer> maxSteam = new ArrayList<>();
        for (int num : nums) {
            maxHeap.insert(num);
            maxSteam.add(maxHeap.getHeap().get(0));
        }
        return maxSteam;
    }
}
