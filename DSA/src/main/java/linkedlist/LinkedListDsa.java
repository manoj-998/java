package linkedlist;


import java.util.HashSet;
import java.util.Set;

public class LinkedListDsa {
    private Node head;
    private Node tail;
    private int length;

    class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * Create new linked list with head and tail
     * This is big O(1) operation
     */
    public LinkedListDsa(Integer value) {
        head = new Node(value);
        tail = head;
        length += 1;
        System.out.println(head);
    }

    /**
     * Append new node to the linked list
     * This is big O(1) operation
     */
    public void append(Integer value) {
        Node newNode = new Node(value);
        if (head == null || length == 0) {
            tail = newNode;
            head = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        length++;

    }

    /**
     * To remove item from the last
     * This is big O(n)
     */
    public Node removeLast() {
        //If not Item in linkedList
        if (length == 0) {
            System.out.println("List is empty");
            return null;
        }
        Node temp = head;
        Node pre = head;
        while (temp.next != null) {
            pre = temp;
            temp = temp.next;
        }
        tail = pre;
        tail.next = null;
        length--;
        //If one item in linked list
        if (length == 0) {
            tail = null;
            head = null;
        }
        return temp;
    }

    /**
     * Add item at first
     * This is big O(1) operation
     */
    public void prepend(int value) {
        Node newNode = new Node(value);
        if (length == 0 || head == null || tail == null) {
            tail = newNode;
            head = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        length++;
    }

    /**
     * This is to remove the first item
     * This is Big O(1)
     */
    public Node removeFirst() {
        if (tail == null || head == null) return null;
        Node temp = head;
        head = head.next;
        temp.next = null;
        length--;
        if (length == 0) {
            tail = null;
            head = null;
        }
        return temp;
    }

    /**
     * To get item by index value
     * and is Big O(n) complexity
     */
    public Node getIndex(int index) {
        if (length == 0 || index < 0 || index > length) {
            return null;
        }
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        return temp;
    }

    /**
     * To set item in a index
     * This is big O(N) operation
     */
    public boolean setIndex(int index, int value) {
        if (index < 0 || index > length) return false;
        Node temp = head;
        for (int i = 0; i < index; i++) {
            temp = temp.next;
        }
        temp.value = value;
        return true;
    }

    /**
     * To insert a item in a index
     * And this is Big O(n) complexity
     */
    public boolean insertInIndex(int index, int value) {
        if (index < 0 || index > length) return false;
        Node temp = head;
        Node pre = head;
        Node newNode = new Node(value);
        if (index == 0) {
            prepend(value);
            return true;
        }
        for (int i = 0; i < index; i++) {
            pre = temp;
            temp = temp.next;
        }
        pre.next = newNode;
        newNode.next = temp;
        if (index == length) tail = newNode;
        length++;
        return true;
    }

    /**
     * To remove a item in index
     * And this is big O(n)
     */
    public Node removeIndex(int index) {
        if (index < 0 || index > length - 1) return null;
        if (index == 0) {
            return removeFirst();
        }
        Node temp = head;
        Node pre = head;
        for (int i = 0; i < index; i++) {
            pre = temp;
            temp = temp.next;
        }
        pre.next = temp.next;
        temp.next = null;
        length--;
        return temp;
    }

    /**
     * To reverse the linked list
     * This is big O(n)
     * Do using three pointer method
     */
    public void reverse() {
        if (length == 0) return;
        Node temp = head;
        head = tail;
        tail = temp;
        Node before = null;
        Node after = temp.next;
        for (int i = 0; i < length; i++) {
            after = temp.next;
            temp.next = before;
            before = temp;
            temp = after;
        }
    }


    /**
     * This is big O(n)
     */
    public Node findMiddleNode() {
        Node slow = head;
        Node fast = tail;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }


    /**
     * Floyd's cycle-finding algorithm (also known as the "tortoise and the hare" algorithm)
     * Time Big O(N) space Big O(1)
     * <p>
     * This algorithm uses two pointers: a slow pointer and a fast pointer.
     * The slow pointer moves one step at a time, while the fast pointer moves two steps at a time.
     * If there is a loop in the linked list, the two pointers will eventually meet at some point.
     * If there is no loop, the fast pointer will reach the end of the list.
     */
    public boolean hasLoop() {
        if (tail == null) return false;
        if (length == 1) return false;
        Node faster = head;
        Node slow = head;
        while (faster != null) {
            slow = slow.next;
            faster = faster.next.next;
            if (faster == slow) return true;

        }
        return false;
    }

    /**
     * Find the kth node from the end of a singly linked list.
     * Time Big O(N)
     * <p>
     * The time complexity of the given code is O(n), where n is the number of nodes in the linked list.
     * This is because we are traversing the linked list twice - once to reach the kth node from the beginning,
     * and then again to find the kth node from the end. The first traversal takes O(k) time,
     * and the second traversal takes O(n - k) time. Since k can be at most n, the overall time complexity is O(n).
     */
    public Node findKthFromEnd(int k) {
        Node slow = head;
        Node fast = head;

        for (int i = 0; i < k; i++) {
            if (fast == null) {
                return null;
            }
            fast = fast.next;
        }

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }

        return slow;
    }

    /**
     * This is Big O(N)
     */
    public void partitionList(int x) {
        if (head == null) return;

        Node dummy1 = new Node(0);
        Node dummy2 = new Node(0);
        Node prev1 = dummy1;
        Node prev2 = dummy2;
        Node current = head;

        while (current != null) {
            if (current.value < x) {
                prev1.next = current;
                prev1 = prev1.next;
            } else {
                prev2.next = current;
                prev2 = prev2.next;
            }
            current = current.next;
        }

        prev2.next = null;
        prev1.next = dummy2.next;
        head = dummy1.next;
    }

    /**
     * This is big O(N)
     */
    public void removeDuplicatesN() {
        Set<Integer> values = new HashSet<>();
        Node previous = null;
        Node current = head;
        while (current != null) {
            if (values.contains(current.value)) {
                previous.next = current.next;
                length -= 1;
            } else {
                values.add(current.value);
                previous = current;
            }
            current = current.next;
        }
    }

    /**
     * This is Big O(N^2)
     */
    public void removeDuplicatesN2() {
        Node current = head;
        while (current != null) {
            Node runner = current;
            while (runner.next != null) {
                if (runner.next.value == current.value) {
                    runner.next = runner.next.next;
                    length -= 1;
                } else {
                    runner = runner.next;
                }
            }
            current = current.next;
        }
    }

    /**
     * This is Big O(N)
     *
     * @return
     */
    public int binaryToDecimal() {
        int num = 0;
        Node current = head;
        while (current != null) {
            num = num * 2 + current.value;
            current = current.next;
        }
        return num;
    }


    public void printList() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.value);
            temp = temp.next;
        }
    }

    public void printAll() {
        if (length == 0) {
            System.out.println("Head: null");
            System.out.println("Tail: null");
        } else {
            System.out.println("Head: " + head.value);
            System.out.println("Tail: " + tail.value);
        }
        System.out.println("Length:" + length);
        System.out.println("\nLinked List:");
        if (length == 0) {
            System.out.println("empty");
        } else {
            printList();
        }
    }

    public Node getHead() {
        return head;
    }

    public Node getTail() {
        return tail;
    }

    public int getLength() {
        return length;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public void setLength(int length) {
        this.length = length;
    }

}


