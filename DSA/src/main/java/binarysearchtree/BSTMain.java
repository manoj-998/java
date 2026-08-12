package binarysearchtree;


public class BSTMain {

    public static void main(String[] args) {
        System.out.println("--------------------------");
        System.out.println("Crete new binary tree");
        //the root will be null
        BinarySearchTreeDsa bst = new BinarySearchTreeDsa();
        bst.insert(1);
        bst.insert(0);
        bst.insert(2);
        bst.insert(3);
        System.out.println(bst.root.value);
        System.out.println(bst.root.left.value);
        System.out.println(bst.root.right.value);
        System.out.println(bst.root.right.right.value);

        System.out.println("--------------------------");
        System.out.println("insert non duplicate item in binary search tree");
        System.out.println("insert 1 : " + bst.insertWithDuplicate(1));
        System.out.println("insert 7 : " + bst.insertWithDuplicate(7));

        System.out.println("--------------------------");
        System.out.println("If the value is present in binary tree");
        System.out.println("0 = " + bst.contains(0));
        System.out.println("2 = " + bst.contains(2));
        System.out.println("7 = " + bst.contains(7));
    }
}
