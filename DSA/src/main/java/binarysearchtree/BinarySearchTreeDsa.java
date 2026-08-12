package binarysearchtree;

import java.util.ArrayList;
import java.util.List;

public class BinarySearchTreeDsa {

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
     * Old way
     * public  BinarySearchTreeDsa(int value) {
     * Node newnode = new Node(value);
     * root = newnode;
     * }
     */

    /**
     * // Achieved this kust by initialling
     * public BinarySearchTreeDsa() {
     * root = null;
     * }
     */

    /**
     * Average Case: O(log n) Worst Case: O(n)
     * to insert a item in binary tree
     */
    public boolean insertWithDuplicate(int value) {
        Node newNode = new Node(value);

        if (root == null) {
            root = newNode;
            return true;
        }
        Node temp = root;
        while (true) {
            if (value < temp.value) {
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
            else if (newNode.value < temp.value) {
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


    /**
     * Worst case Big O(N) average case O(log N)
     * To find if item in present in the BST
     */
    public boolean contains(int value) {
        if (root == null) return false;
        Node temp = root;
        while (temp != null) {
            if (temp.value == value) return true;
            if (value < temp.value) temp = temp.left;
            else temp = temp.right;
        }
        return false;
    }

}
