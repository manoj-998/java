package binarysearchtreerecursion;


public class BinarySearchTreeRecursionDsa {

    Node root;

    /**
     * Create a new node for
     */
    static class Node {
        int value;
        Node left;
        Node right;

        public Node(int value) {
            this.value = value;
        }
    }

    /**
     * Average Case: O(log n) Worst Case: O(n)
     * Insert without duplicate item
     */
    public boolean insert(int value) {
        Node newNode = new Node(value);
        if (root == null) {
            root = newNode;
            return true;
        }
        Node temp = root;
        while (true) {
            if (newNode.value == temp.value) return false;

            if (newNode.value < temp.value) {
                if (temp.left == null) {
                    temp.left = newNode;
                    return true;
                }
                temp = temp.left;
            } else {
                if (temp.right == null) {
                    temp.right = newNode;
                    return true;
                }
                temp = temp.right;
            }
        }
    }

    public boolean containsR(int value) {
        return containsR(root, value);
    }

    private boolean containsR(Node currentNode, int value) {
        if (currentNode == null) return false;
        if (currentNode.value == value) return true;
        if (currentNode.value > value) {
            return containsR(currentNode.left, value);
        } else {
            return containsR(currentNode.right, value);
        }
    }

    public void insertR(int value) {
        if (root == null) {
            root = new Node(value);
        }
        insertR(root, value);
    }

    private Node insertR(Node currentNode, int value) {
        if (currentNode == null) return new Node(value);
        if (currentNode.value > value) {
            currentNode.left = insertR(currentNode.left, value);
        } else {
            currentNode.right = insertR(currentNode.right, value);
        }
        return currentNode;
    }

    private void remove(int value) {
        deleteNode(root, value);
    }

    private Node deleteNode(Node currentNode, int value) {
        if (currentNode == null) return null;
        if (value < currentNode.value) {
            currentNode = deleteNode(currentNode.left, value);
        } else if (value > currentNode.value) {
            currentNode = deleteNode(currentNode.right, value);
        } else {
            if (currentNode.left == null && currentNode.right == null)
                return null;
            else if (currentNode.left == null) {
                currentNode = currentNode.right;
            } else if (currentNode.right == null) {
                currentNode = currentNode.left;
            } else {
                int min = minValue(currentNode.right);
                currentNode.value = min;
                deleteNode(currentNode.right, min);
            }
        }
        return currentNode;
    }

    private int minValue(Node currentNode) {
        while (currentNode.left != null) {
            currentNode = currentNode.left;
        }
        return currentNode.value;
    }

}
