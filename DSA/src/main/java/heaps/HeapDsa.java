package heaps;


import java.util.ArrayList;
import java.util.List;

public class HeapDsa {
    private List<Integer> heap;

    public HeapDsa() {
        this.heap = new ArrayList<>();
    }
    /**
     * Big O(1)
     */
    public HeapDsa(Integer value) {
        heap = new ArrayList<>();
        heap.add(value);
    }

    /**
     * Big O(1)
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }

    /**
     * Big O(1)
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }

    /**
     * Big O(1)
     */
    private int pranet(int index) {
        return (index - 1) / 2;
    }

    /**
     * Big O(1)
     */
    private void swap(int firstIndex, int secondIndex) {
        int temp1 = heap.get(firstIndex);
        heap.set(firstIndex, heap.get(secondIndex));
        heap.set(secondIndex, temp1);
    }

    /**
     * Big O(log n) time complexity for the worst cases
     * The swapping in such a case is also done with O(log n) time complexity. If the number of elements to be inserted is 'n'. The time complexity 'log n' which was for a single element is multiplied by n
     */
    public void insert(int value) {
        heap.add(value);
        int current = heap.size() - 1;
        while (current > 0 && heap.get(current) > heap.get(pranet(current))) {
            swap(current, pranet(current));
            current = pranet(current);
        }
    }

    /**
     * O(log n).
     * Remove the root node in heap
     */
    public Integer remove() {
        if (heap.size() == 0) return 0;
        if (heap.size() == 1) return heap.remove(0);
        int maxValue = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        sinkDown(0);
        return maxValue;
    }

    /**
     * O(log n).
     */
    private void sinkDown(int index) {
        int maxIndex = index;
        while (true) {
            int rightChild = rightChild(maxIndex);
            int leftChild = leftChild(maxIndex);
            if (leftChild < heap.size() && heap.get(leftChild) > heap.get(maxIndex))
                maxIndex = leftChild;

            if (rightChild < heap.size() && heap.get(rightChild) > heap.get(maxIndex))
                maxIndex = rightChild;

            if (maxIndex != index) {
                swap(index, maxIndex);
                index = maxIndex;
            } else
                return;

        }
    }



    /**
     * if we give access to the actual heap variable it can we changed outside the class which will make heap invalid
     * Return a copy of a head
     */
    public List<Integer> getHeap() {
        return new ArrayList<>(heap);
    }

    public void printHeap() {
        System.out.println(getHeap());
    }
}
