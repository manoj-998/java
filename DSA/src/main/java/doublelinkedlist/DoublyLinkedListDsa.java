package doublelinkedlist;

public class DoublyLinkedListDsa {
    private int value;
    private Node head;
    private Node tail;
    private int lenght;

    public int getValue() {
        return value;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public Node getTail() {
        return tail;
    }

    public void setTail(Node tail) {
        this.tail = tail;
    }

    public void printAll() {
        if (head == null) {
            System.out.println("head = " + head.value);
            System.out.println("tail = " + tail.value);
            System.out.println("length = " + lenght);
            System.out.println("doublyLinkedList values");
            System.out.println("Empty");
        } else {
            System.out.println("head = " + head.value);
            System.out.println("tail = " + tail.value);
            System.out.println("length = " + lenght);
            System.out.println("doublyLinkedList values");
            Node temp = head;
            while (temp != null) {
                System.out.println(temp.value);
                temp = temp.next;
            }
        }
    }

    /**
     * To create new Node of doublyLinked list
     */
    class Node {
        int value;
        Node next;
        Node previous;

        public Node(int value) {
            this.value = value;
        }
    }

    public DoublyLinkedListDsa(int value) {
        Node newNode = new Node(value);
        head = newNode;
        tail = newNode;
        lenght++;
    }

    /**
     * This is Big O(1) to add item in last
     */
    public void append(int value) {
        Node newNode = new Node(value);
        if (head == null || lenght == 0) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.previous = tail;
            tail.next = newNode;
            tail = newNode;
        }
        lenght++;
    }

    /**
     * This is Big O(1) to remove last Item in DDL
     */
    public Node removeLast() {
        if (head == null || lenght == 0) return null;
        Node temp = tail;
        tail = tail.previous;
        tail.next = null;
        temp.previous = null;
        lenght--;
        if (lenght == 0) {
            head = null;
            temp = null;
        }
        return temp;
    }

    /**
     * Big O(1) to add at first of DDL
     */
    public void prepend(int value) {
        Node newNode = new Node(value);
        if (head == null || tail == null) {
            tail = newNode;
            head = newNode;
        } else {
            head.previous = newNode;
            newNode.next = head;
            head = newNode;
        }
        lenght++;
    }

    /**
     * Big O(1) to remove item at first
     *
     * @return
     */
    public Node removeFirst() {
        if (head == null || tail == null) return null;
        Node temp = head;
        if (lenght == 1) {
            tail = null;
            head = null;
        } else {
            head = temp.next;
            head.previous = null;
            temp.next = null; //try this
        }
        lenght--;
        return temp;
    }

    /**
     * Big O(N) get item in a index
     */
    public Node get(int index) {
        if (index < 0 || index >= lenght) return null;
        Node temp = null;
        if (index < lenght / 2) {
            temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = tail;
            for (int i = lenght - 1; i > index; i--) {
                temp = temp.previous;
            }
        }
        return temp;
    }

    /**
     * This is Big O(N) to set value in a index
     */
    public boolean set(int index, int value) {
        if (index < 0 || index >= lenght) return false;
        Node temp = null;
        if (index < lenght / 2) {
            temp = head;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
        } else {
            temp = tail;
            for (int i = lenght - 1; i > index; i--) {
                temp = temp.previous;
            }
        }
        temp.value = value;
        return true;
    }


    /**
     * Big O(N) to insert item in inex
     */
    public boolean insertInIndex(int index, int value) {
        if (index < 0 || index > lenght) return false;
        if (index == 0) {
            prepend(value);
            return true;
        }
        if (index == lenght) {
            append(value);
            return true;
        }
        Node newNode = new Node(value);
        Node indexItemBefore = get(index - 1);
        newNode.next = indexItemBefore.next;
        newNode.previous = indexItemBefore;
        indexItemBefore.next = newNode;
        newNode.next.previous = newNode;
        lenght++;
        return true;
    }

    /**
     * Big O(N) to remove item in a index
     */
    public Node removeInIndex(int index) {
        if (index < 0 || index >= lenght) return null;
        Node temp = head;
        if (index == 0) return removeLast();
        if (index == lenght - 1) return removeLast();

        Node indexItem = get(index);
        indexItem.next.previous = indexItem.previous;
        indexItem.previous.next = indexItem.next;
        indexItem.previous = null;
        indexItem.next = null;
        lenght--;
        return temp;
    }

    public void swapFirstLast() {
        if (lenght == 1 || head == null) return;
        int value = tail.value;
        tail.value = head.value;
        head.value = value;
    }

    public void revers() {
        if (head == null || head.next == null) return; // Handle empty or single element list

        Node current = head;
        Node temp = null;

        while (current != null) {
            temp = current.previous;
            current.previous = current.next;
            current.next = temp;
            current = current.previous; // Move to the next node in the original list
        }

        // Swap head and tail
        temp = head;
        head = tail;
        tail = temp;
    }


}
