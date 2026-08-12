package queue;

public class QueueDsa {
    private Node first;
    private Node last;
    private int length;

    public Node getFirst() {
        return first;
    }

    public void setFirst(Node first) {
        this.first = first;
    }

    public Node getLast() {
        return last;
    }

    public void setLast(Node last) {
        this.last = last;
    }

    public int getSize() {
        return length;
    }

    public void setSize(int size) {
        this.length = size;
    }

    public void printAll() {
        if (length == 0) {
            System.out.println("Queue size = " + length);
            System.out.println("First pointer = " + first);
            System.out.println("Last pointer = " + last);
            System.out.println("Queue is Empty = ");
        } else {
            System.out.println("Queue size    = " + length);
            System.out.println("First pointer = " + first.value + "  | memory -> " + first);
            System.out.println("Last pointer  = " + last.value + "  | memory -> " + last);
            System.out.println("Queue values");
            Node temp = first;
            while (temp != null) {
                System.out.println(temp.value + "        | memory - > " + temp);
                temp = temp.next;
            }
        }
    }

    class Node {
        Node next;
        int value;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * Big O(1) Create a new Queue
     */
    public QueueDsa(int value) {
        Node newNode = new Node(value);
        first = newNode;
        last = newNode;
        length++;
    }

    /**
     * Big O(1) to add item in last of queue
     */
    public void enQueue(int value) {
        Node newNode = new Node(value);
        if (length == 0) {
            first = newNode;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        length++;
    }

    /**
     * Big O(1) to remove first item from queue
     */
    public Node deQueue() {
        if (length == 0) return null;
        Node temp = first;
        first = temp.next;
        temp.next = null;
        length--;
        if (length == 0) {
            first = null;
            last = null;
        }
        return temp;
    }
}
