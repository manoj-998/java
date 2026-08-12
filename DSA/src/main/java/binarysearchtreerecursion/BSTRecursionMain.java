package binarysearchtreerecursion;

public class BSTRecursionMain {

    public static void main(String[] args) {
        System.out.println("--------------------------");
        System.out.println("Crete new binary tree");
        //the root will be null
        BinarySearchTreeRecursionDsa bstR = new BinarySearchTreeRecursionDsa();
        bstR.insertR(0);
        bstR.insertR(-1);
        bstR.insertR(1);
        bstR.insertR(-2);
        bstR.insertR(2);
        bstR.insertR(3);
        System.out.println(bstR.root.value);
        System.out.println(bstR.root.left.value);
        System.out.println(bstR.root.right.value);
        System.out.println(bstR.root.left.left.value);
        System.out.println(bstR.root.right.right.value);

        System.out.println("--------------------------");
        System.out.println("Contains value");
        System.out.println("3 : " + bstR.containsR(3));
        System.out.println("4 : " + bstR.containsR(4));

        System.out.println("--------------------------");
        System.out.println("insert using recursive function");
        System.out.println(bstR.insert(5));
        System.out.println(bstR.root.value);
        System.out.println(bstR.root.left.value);
        System.out.println(bstR.root.right.value);
        System.out.println(bstR.root.left.left.value);
        System.out.println(bstR.root.right.right.value);
        System.out.println(bstR.root.right.right.right.value);

    }
}
