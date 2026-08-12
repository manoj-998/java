package stack;

public class StackDsa {


    public Node getTop() {
        return top;
    }

    public void setTop(Node top) {
        this.top = top;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void printAll() {
        if (top == null) {
            System.out.println("height = " + height);
            System.out.println("top = " + null);
            System.out.println("Stack is empty");
        } else {
            System.out.println("height = " + height);
            System.out.println("top = " + top.value);
            System.out.println("Stack values");
            Node temp = top;
            while (temp != null) {
                System.out.println(temp.value);
                temp = temp.next;
            }
        }
    }

    private Node top;
    private int height;

    static class Node {
        int value;
        Node next;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * Big O(1) to create new stack
     */
    public StackDsa(int value) {
        Node newNode = new Node(value);
        top = newNode;
        height++;
    }

    /**
     * Big O(1) to push item in top
     */
    public void push(int value) {
        Node newNode = new Node(value);
        if (height == 0) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        height++;
    }

    /**
     * Big O(1) to remove item from top
     */
    public Node pop() {
        if (height == 0 || top == null) return null;
        Node temp = top;
        top = top.next;
        temp.next = null;
        height--;
        return temp;
    }

}
